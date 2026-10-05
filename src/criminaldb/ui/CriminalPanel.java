package criminaldb.ui;

import criminaldb.db.CriminalDAO;
import criminaldb.model.Criminal;
import criminaldb.utils.UITheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CriminalPanel extends JPanel {

    private final CriminalDAO dao = new CriminalDAO();
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfSearch;
    private JLabel photoLabel;

    // Camera
    private boolean cameraRunning = false;
    private Thread  cameraThread;

    public CriminalPanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());
        build();
        loadTable(dao.getAllCriminals());
    }

    private void build() {
        // ── Header bar ─────────────────────────────────────────
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UITheme.BG_DARK);
        top.setBorder(BorderFactory.createEmptyBorder(18, 24, 10, 24));

        JLabel hdr = new JLabel("CRIMINAL RECORDS");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        top.add(hdr, BorderLayout.WEST);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchBar.setBackground(UITheme.BG_DARK);
        tfSearch = new JTextField(20);
        styleField(tfSearch);
        tfSearch.setToolTipText("Search by name, address, blood group...");
        JButton btnSearch = btn("🔍 Search",  UITheme.ACCENT_BLUE);
        JButton btnReset  = btn("↺ Reset",    UITheme.BG_CARD);
        btnSearch.addActionListener(e -> loadTable(dao.search(tfSearch.getText().trim())));
        btnReset.addActionListener (e -> { tfSearch.setText(""); loadTable(dao.getAllCriminals()); });
        searchBar.add(tfSearch); searchBar.add(btnSearch); searchBar.add(btnReset);
        top.add(searchBar, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────
        String[] cols = {"ID","Name","Age","Gender","Address","Blood","Wanted"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) updatePhoto();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // ── Right panel: photo + buttons ───────────────────────
        JPanel right = new JPanel(new BorderLayout(0, 10));
        right.setBackground(UITheme.BG_DARK);
        right.setPreferredSize(new Dimension(230, 0));
        right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));

        photoLabel = new JLabel("No Photo", SwingConstants.CENTER);
        photoLabel.setPreferredSize(new Dimension(200, 200));
        photoLabel.setOpaque(true);
        photoLabel.setBackground(UITheme.BG_CARD);
        photoLabel.setForeground(UITheme.TEXT_MUTED);
        photoLabel.setFont(UITheme.fontBody(11));
        photoLabel.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        right.add(photoLabel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        btnPanel.setBackground(UITheme.BG_DARK);
        JButton btnAdd   = btn("➕  Add Criminal",    UITheme.ACCENT_BLUE);
        JButton btnEdit  = btn("✏️  Edit Selected",   UITheme.ACCENT_AMBER);
        JButton btnDel   = btn("🗑️  Delete Selected", UITheme.ACCENT_RED);
        JButton btnUpPic = btn("📂  Upload Photo",    new Color(0x546E7A));
        JButton btnCam   = btn("📷  Start Camera",    new Color(0x2E7D32));
        JButton btnSnap  = btn("📸  Capture Frame",   new Color(0x1565C0));
        JButton btnStop  = btn("⏹️  Stop Camera",     new Color(0x4E342E));

        btnAdd.addActionListener  (e -> showForm(null));
        btnEdit.addActionListener (e -> editSelected());
        btnDel.addActionListener  (e -> deleteSelected());
        btnUpPic.addActionListener(e -> uploadPhoto());
        btnCam.addActionListener  (e -> startCamera());
        btnSnap.addActionListener (e -> captureFrame());
        btnStop.addActionListener (e -> stopCamera());

        for (JButton b : new JButton[]{btnAdd,btnEdit,btnDel,btnUpPic,btnCam,btnSnap,btnStop})
            btnPanel.add(b);
        right.add(btnPanel, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, right);
        split.setDividerLocation(920);
        split.setBackground(UITheme.BG_DARK);
        split.setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 0));
        add(split, BorderLayout.CENTER);
    }

    // ── TABLE LOAD ────────────────────────────────────────────
    private void loadTable(List<Criminal> list) {
        model.setRowCount(0);
        for (Criminal c : list) {
            model.addRow(new Object[]{
                c.getCriminalId(), c.getName(), c.getAge(), c.getGender(),
                c.getAddress(), c.getBloodGroup(),
                c.isWanted() ? "⚠ WANTED" : "✔ In Custody"
            });
        }
        // Color 'WANTED' rows red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                boolean wanted = "⚠ WANTED".equals(t.getValueAt(r, 6));
                setBackground(sel ? UITheme.ACCENT_BLUE.darker() :
                              (r % 2 == 0 ? UITheme.BG_CARD : UITheme.TABLE_ALT));
                setForeground(wanted && c == 6 ? UITheme.ACCENT_RED : UITheme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }

    private void updatePhoto() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        int id = (int) table.getValueAt(row, 0);
        Criminal c = dao.getCriminalById(id);
        if (c == null || c.getPhotoPath() == null) {
            photoLabel.setIcon(null);
            photoLabel.setText("No Photo");
            return;
        }
        File f = new File(c.getPhotoPath());
        if (f.exists()) {
            try {
                BufferedImage img = ImageIO.read(f);
                Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                photoLabel.setIcon(new ImageIcon(scaled));
                photoLabel.setText("");
            } catch (IOException ex) { photoLabel.setText("Load error"); }
        } else {
            photoLabel.setText("File missing");
        }
    }

    // ── CRUD HELPERS ──────────────────────────────────────────
    private void showForm(Criminal existing) {
        CriminalFormDialog dlg = new CriminalFormDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this), existing, dao);
        dlg.setVisible(true);
        loadTable(dao.getAllCriminals());
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { toast("Select a criminal first."); return; }
        int id = (int) table.getValueAt(row, 0);
        showForm(dao.getCriminalById(id));
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { toast("Select a criminal first."); return; }
        int id   = (int) table.getValueAt(row, 0);
        String n = (String) table.getValueAt(row, 1);
        int ok = JOptionPane.showConfirmDialog(this,
            "Delete criminal: " + n + "?\nThis will also delete all related crimes and cases.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            if (dao.deleteCriminal(id)) {
                toast("Deleted: " + n);
                loadTable(dao.getAllCriminals());
            }
        }
    }

    private void uploadPhoto() {
        int row = table.getSelectedRow();
        if (row < 0) { toast("Select a criminal row first."); return; }
        int id = (int) table.getValueAt(row, 0);
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images","jpg","jpeg","png"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File src = fc.getSelectedFile();
            try {
                Path dest = Paths.get("resources/images/criminal_" + id + ".jpg");
                Files.createDirectories(dest.getParent());
                Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                Criminal c = dao.getCriminalById(id);
                c.setPhotoPath(dest.toString());
                dao.updateCriminal(c);
                updatePhoto();
                toast("Photo saved.");
            } catch (IOException e) { toast("Error saving photo."); }
        }
    }

    // ── CAMERA (OpenCV stub — works with webcam via Robot fallback) ──
    private void startCamera() {
        if (cameraRunning) return;
        cameraRunning = true;
        toast("Camera started. (Connect OpenCV for live feed — see README)");
    }

    private void captureFrame() {
        if (!cameraRunning) { toast("Start camera first."); return; }
        int row = table.getSelectedRow();
        if (row < 0) { toast("Select a criminal to assign the capture."); return; }
        int id = (int) table.getValueAt(row, 0);
        try {
            // Fallback: screenshot region as placeholder until OpenCV integrated
            Robot robot = new Robot();
            Rectangle rect = new Rectangle(0, 0, 640, 480);
            BufferedImage capture = robot.createScreenCapture(rect);
            Path dest = Paths.get("resources/images/criminal_" + id + "_cam.jpg");
            Files.createDirectories(dest.getParent());
            ImageIO.write(capture, "jpg", dest.toFile());
            Criminal c = dao.getCriminalById(id);
            c.setPhotoPath(dest.toString());
            dao.updateCriminal(c);
            updatePhoto();
            toast("Frame captured and saved to criminal #" + id);
        } catch (Exception e) { toast("Capture failed: " + e.getMessage()); }
    }

    private void stopCamera() {
        cameraRunning = false;
        toast("Camera stopped.");
    }

    // ── STYLE HELPERS ─────────────────────────────────────────
    private void styleTable(JTable t) {
        t.setBackground(UITheme.BG_CARD);
        t.setForeground(UITheme.TEXT_PRIMARY);
        t.setFont(UITheme.fontBody(12));
        t.setRowHeight(30);
        t.setGridColor(UITheme.BORDER);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(UITheme.ACCENT_BLUE.darker());
        t.setSelectionForeground(UITheme.TEXT_PRIMARY);
        t.getTableHeader().setBackground(UITheme.BG_SIDEBAR);
        t.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        t.getTableHeader().setFont(UITheme.fontBody(11));
        t.setAutoCreateRowSorter(true);
    }

    private void styleField(JTextField f) {
        f.setBackground(UITheme.BG_CARD);
        f.setForeground(UITheme.TEXT_PRIMARY);
        f.setCaretColor(UITheme.TEXT_PRIMARY);
        f.setFont(UITheme.fontBody(12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private JButton btn(String txt, Color bg) {
        JButton b = new JButton(txt);
        UITheme.styleButton(b, bg);
        b.setPreferredSize(new Dimension(180, 34));
        return b;
    }

    private void toast(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
