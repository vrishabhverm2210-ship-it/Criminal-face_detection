package criminaldb.ui;

import criminaldb.db.CriminalDAO;
import criminaldb.model.Criminal;
import criminaldb.utils.UITheme;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * Face Recognition Panel.
 *
 * INTEGRATION NOTES:
 * ─────────────────────────────────────────────────────────────────
 *  This panel is UI-ready. To add REAL face recognition:
 *
 *  Option A — OpenCV (Java):
 *    1. Download opencv-4xx.jar + .dll/.so from opencv.org
 *    2. Add to /lib
 *    3. Use VideoCapture for live feed
 *    4. Use CascadeClassifier for face detection
 *    5. Compare descriptors against stored face_encodings in DB
 *
 *  Option B — Python bridge:
 *    1. Write face_match.py using face_recognition library
 *    2. Call from Java: Runtime.getRuntime().exec("python face_match.py --image path")
 *    3. Read stdout for matched criminal_id
 *
 *  Option C — DeepFace (Python):
 *    DeepFace.find(img_path, db_path="resources/images/")
 * ─────────────────────────────────────────────────────────────────
 *
 *  For the demo/viva, the panel shows:
 *  - Start/Stop camera buttons (simulated with screenshot)
 *  - "Scan Face" which does a mock match from DB
 *  - Result card showing matched criminal data
 */
public class FaceRecPanel extends JPanel {

    private final CriminalDAO dao = new CriminalDAO();
    private JLabel cameraFeed;
    private JLabel statusLabel;
    private JPanel resultCard;
    private JLabel lblResultName, lblResultAge, lblResultCrime, lblResultStatus;
    private boolean cameraOn = false;
    private Timer frameTimer;

    public FaceRecPanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());
        build();
    }

    private void build() {
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(18,24,10,24));
        JLabel hdr = new JLabel("FACE RECOGNITION SCANNER");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        header.add(hdr, BorderLayout.WEST);
        JLabel sub = new JLabel("Live identification against criminal database");
        sub.setFont(UITheme.fontBody(11));
        sub.setForeground(UITheme.TEXT_MUTED);
        header.add(sub, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Main layout: camera feed | result panel
        JPanel main = new JPanel(new GridLayout(1, 2, 20, 0));
        main.setBackground(UITheme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(0,24,24,24));

        // ── Left: Camera feed ──────────────────────────────────
        JPanel camPanel = new JPanel(new BorderLayout(0,12));
        camPanel.setBackground(UITheme.BG_CARD);
        camPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(16,16,16,16)
        ));

        JLabel camTitle = new JLabel("LIVE CAMERA FEED");
        camTitle.setFont(UITheme.fontTitle(13));
        camTitle.setForeground(UITheme.TEXT_MUTED);
        camPanel.add(camTitle, BorderLayout.NORTH);

        cameraFeed = new JLabel("Camera OFF", SwingConstants.CENTER);
        cameraFeed.setPreferredSize(new Dimension(480, 360));
        cameraFeed.setBackground(new Color(0x05070A));
        cameraFeed.setForeground(UITheme.TEXT_MUTED);
        cameraFeed.setFont(UITheme.fontMono(14));
        cameraFeed.setOpaque(true);
        cameraFeed.setBorder(BorderFactory.createLineBorder(UITheme.ACCENT_BLUE, 1));
        camPanel.add(cameraFeed, BorderLayout.CENTER);

        // Camera controls
        JPanel camCtrl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        camCtrl.setBackground(UITheme.BG_CARD);
        JButton btnStart = makeBtn("▶  START CAMERA", UITheme.ACCENT_GREEN);
        JButton btnStop  = makeBtn("⏹  STOP CAMERA",  UITheme.ACCENT_RED);
        JButton btnScan  = makeBtn("🔍  SCAN FACE",   UITheme.ACCENT_BLUE);

        btnStart.addActionListener(e -> startCamera());
        btnStop.addActionListener (e -> stopCamera());
        btnScan.addActionListener (e -> scanFace());

        camCtrl.add(btnStart); camCtrl.add(btnStop); camCtrl.add(btnScan);
        camPanel.add(camCtrl, BorderLayout.SOUTH);

        // Status bar
        statusLabel = new JLabel("  Status: Camera OFF", SwingConstants.LEFT);
        statusLabel.setFont(UITheme.fontMono(11));
        statusLabel.setForeground(UITheme.ACCENT_AMBER);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(UITheme.BG_SIDEBAR);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6,8,6,8));

        JPanel leftWrap = new JPanel(new BorderLayout(0,0));
        leftWrap.setBackground(UITheme.BG_DARK);
        leftWrap.add(camPanel, BorderLayout.CENTER);
        leftWrap.add(statusLabel, BorderLayout.SOUTH);

        // ── Right: Result panel ────────────────────────────────
        resultCard = new JPanel(new BorderLayout(0,14));
        resultCard.setBackground(UITheme.BG_CARD);
        resultCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(20,20,20,20)
        ));

        JLabel resTitle = new JLabel("IDENTIFICATION RESULT");
        resTitle.setFont(UITheme.fontTitle(13));
        resTitle.setForeground(UITheme.TEXT_MUTED);
        resultCard.add(resTitle, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(0,1,0,12));
        fields.setBackground(UITheme.BG_CARD);
        lblResultName   = resultField("–");
        lblResultAge    = resultField("–");
        lblResultCrime  = resultField("–");
        lblResultStatus = resultField("–");
        fields.add(labeledRow("NAME:",   lblResultName));
        fields.add(labeledRow("AGE:",    lblResultAge));
        fields.add(labeledRow("CRIMES:", lblResultCrime));
        fields.add(labeledRow("STATUS:", lblResultStatus));

        // Instruction text
        JTextArea hint = new JTextArea(
            "How to add YOUR face for recognition:\n\n" +
            "1. Go to 'Criminals' panel\n" +
            "2. Add yourself as a criminal entry\n" +
            "3. Click 'Upload Photo' or 'Capture Frame'\n" +
            "4. Come back here and click 'Scan Face'\n\n" +
            "For real OpenCV face matching, see\n" +
            "the integration notes in FaceRecPanel.java"
        );
        hint.setEditable(false);
        hint.setBackground(UITheme.BG_SIDEBAR);
        hint.setForeground(UITheme.TEXT_MUTED);
        hint.setFont(UITheme.fontBody(11));
        hint.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        fields.add(hint);
        resultCard.add(fields, BorderLayout.CENTER);

        main.add(leftWrap);
        main.add(resultCard);
        add(main, BorderLayout.CENTER);
    }

    private void startCamera() {
        if (cameraOn) return;
        cameraOn = true;
        statusLabel.setText("  Status: ● CAMERA LIVE  |  Ready to scan...");
        statusLabel.setForeground(UITheme.ACCENT_GREEN);

        // Simulate scanning animation
        frameTimer = new Timer(500, e -> {
            cameraFeed.setText("[ CAMERA ACTIVE — Connect OpenCV for live feed ]");
            cameraFeed.setForeground(UITheme.ACCENT_GREEN);
        });
        frameTimer.start();
    }

    private void stopCamera() {
        cameraOn = false;
        if (frameTimer != null) frameTimer.stop();
        cameraFeed.setText("Camera OFF");
        cameraFeed.setForeground(UITheme.TEXT_MUTED);
        statusLabel.setText("  Status: Camera OFF");
        statusLabel.setForeground(UITheme.ACCENT_AMBER);
    }

    private void scanFace() {
        if (!cameraOn) {
            JOptionPane.showMessageDialog(this, "Start the camera first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        statusLabel.setText("  Status: ⟳ SCANNING... Comparing against database...");
        statusLabel.setForeground(UITheme.ACCENT_BLUE);

        // Simulate processing delay
        Timer scanTimer = new Timer(1800, e -> {
            List<Criminal> criminals = dao.getAllCriminals();
            if (criminals.isEmpty()) {
                statusLabel.setText("  No criminals in database to match.");
                return;
            }
            // Demo: return a random "wanted" criminal
            List<Criminal> wanted = criminals.stream()
                .filter(Criminal::isWanted)
                .collect(java.util.stream.Collectors.toList());
            Criminal match = wanted.isEmpty() ? criminals.get(0) :
                             wanted.get((int)(Math.random()*wanted.size()));

            lblResultName.setText(match.getName());
            lblResultAge.setText(match.getAge()+" yrs  |  "+match.getGender());
            lblResultCrime.setText(match.getAddress());
            lblResultStatus.setText(match.isWanted() ? "⚠  WANTED — Alert Authorities!" : "✔  In Custody");
            lblResultStatus.setForeground(match.isWanted() ? UITheme.ACCENT_RED : UITheme.ACCENT_GREEN);

            statusLabel.setText("  Status: ✔ MATCH FOUND — ID #"+match.getCriminalId());
            statusLabel.setForeground(UITheme.ACCENT_RED);

            resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(match.isWanted()?UITheme.ACCENT_RED:UITheme.ACCENT_GREEN, 2),
                BorderFactory.createEmptyBorder(20,20,20,20)
            ));
        });
        scanTimer.setRepeats(false);
        scanTimer.start();
    }

    private JLabel resultField(String val) {
        JLabel l = new JLabel(val);
        l.setFont(UITheme.fontTitle(15));
        l.setForeground(UITheme.TEXT_PRIMARY);
        return l;
    }

    private JPanel labeledRow(String label, JLabel val) {
        JPanel p = new JPanel(new GridLayout(1,2));
        p.setBackground(UITheme.BG_CARD);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.fontBody(11));
        lbl.setForeground(UITheme.TEXT_MUTED);
        p.add(lbl); p.add(val);
        return p;
    }

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        UITheme.styleButton(b, bg);
        b.setPreferredSize(new Dimension(160,36));
        return b;
    }
}
