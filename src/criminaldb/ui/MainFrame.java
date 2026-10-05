package criminaldb.ui;

import criminaldb.utils.UITheme;
import javax.swing.*;
import java.awt.*;

/**
 * Main application window — dark police system aesthetic.
 * Sidebar navigation → swaps content panels.
 */
public class MainFrame extends JFrame {

    private JPanel contentArea;
    private DashboardPanel  dashboardPanel;
    private CriminalPanel   criminalPanel;
    private CrimePanel      crimePanel;
    private CasesPanel      casesPanel;
    private OfficerPanel    officerPanel;
    private FaceRecPanel    faceRecPanel;
    private AnalyticsPanel  analyticsPanel;

    public MainFrame() {
        setTitle("CRIMINAL FACE RECOGNITION & CRIME DATABASE SYSTEM  |  Punjab Police");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1380, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        add(buildTopBar(),   BorderLayout.NORTH);
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildContent(),  BorderLayout.CENTER);

        showPanel("dashboard");
    }

    // ── TOP BAR ───────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UITheme.BG_SIDEBAR);
        bar.setPreferredSize(new Dimension(0, 52));
        bar.setBorder(BorderFactory.createMatteBorder(0,0,1,0, UITheme.BORDER));

        JLabel logo = new JLabel("  ⬛ CRIMEDB  |  Criminal Intelligence System v2.0");
        logo.setFont(UITheme.fontTitle(14));
        logo.setForeground(UITheme.ACCENT_BLUE);
        bar.add(logo, BorderLayout.WEST);

        JLabel clock = new JLabel("Punjab Police Department  ●  CONFIDENTIAL   ");
        clock.setFont(UITheme.fontBody(11));
        clock.setForeground(UITheme.TEXT_MUTED);
        bar.add(clock, BorderLayout.EAST);
        return bar;
    }

    // ── SIDEBAR ───────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(UITheme.BG_SIDEBAR);
        side.setPreferredSize(new Dimension(200, 0));
        side.setBorder(BorderFactory.createMatteBorder(0,0,0,1, UITheme.BORDER));

        side.add(Box.createVerticalStrut(16));
        side.add(sideLabel("NAVIGATION"));
        side.add(navButton("🏠  Dashboard",   "dashboard"));
        side.add(navButton("👤  Criminals",   "criminals"));
        side.add(navButton("🚔  Crimes",      "crimes"));
        side.add(navButton("📁  Cases",       "cases"));
        side.add(navButton("👮  Officers",    "officers"));
        side.add(navButton("📊  Analytics",   "analytics"));
        side.add(Box.createVerticalStrut(12));
        side.add(sideLabel("FACE RECOGNITION"));
        side.add(navButton("📷  Face Scanner", "facerecog"));
        side.add(Box.createVerticalGlue());

        JLabel ver = new JLabel("  v2.0  |  2024");
        ver.setFont(UITheme.fontBody(10));
        ver.setForeground(UITheme.TEXT_MUTED);
        ver.setAlignmentX(LEFT_ALIGNMENT);
        side.add(ver);
        side.add(Box.createVerticalStrut(10));
        return side;
    }

    private JLabel sideLabel(String text) {
        JLabel lbl = new JLabel("  " + text);
        lbl.setFont(UITheme.fontBody(9));
        lbl.setForeground(UITheme.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(8,4,4,4));
        return lbl;
    }

    private JButton navButton(String text, String panel) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 38));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(UITheme.fontBody(12));
        btn.setForeground(UITheme.TEXT_PRIMARY);
        btn.setBackground(UITheme.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 6));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(UITheme.BG_CARD); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(UITheme.BG_SIDEBAR); }
        });
        btn.addActionListener(e -> showPanel(panel));
        return btn;
    }

    // ── CONTENT AREA ─────────────────────────────────────────
    private JPanel buildContent() {
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(UITheme.BG_DARK);

        dashboardPanel  = new DashboardPanel(this);
        criminalPanel   = new CriminalPanel();
        crimePanel      = new CrimePanel();
        casesPanel      = new CasesPanel();
        officerPanel    = new OfficerPanel();
        analyticsPanel  = new AnalyticsPanel();
        faceRecPanel    = new FaceRecPanel();

        contentArea.add(dashboardPanel,  "dashboard");
        contentArea.add(criminalPanel,   "criminals");
        contentArea.add(crimePanel,      "crimes");
        contentArea.add(casesPanel,      "cases");
        contentArea.add(officerPanel,    "officers");
        contentArea.add(analyticsPanel,  "analytics");
        contentArea.add(faceRecPanel,    "facerecog");
        return contentArea;
    }

    public void showPanel(String name) {
        ((CardLayout) contentArea.getLayout()).show(contentArea, name);
        if ("analytics".equals(name))  analyticsPanel.refresh();
        if ("dashboard".equals(name))  dashboardPanel.refresh();
    }

    // ── ENTRY POINT ───────────────────────────────────────────
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            // Override with our dark theme defaults
            UIManager.put("Panel.background",      UITheme.BG_DARK);
            UIManager.put("OptionPane.background",  UITheme.BG_PANEL);
            UIManager.put("TextField.background",   UITheme.BG_CARD);
            UIManager.put("TextField.foreground",   UITheme.TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", UITheme.TEXT_PRIMARY);
            UIManager.put("ComboBox.background",    UITheme.BG_CARD);
            UIManager.put("ComboBox.foreground",    UITheme.TEXT_PRIMARY);
            UIManager.put("TextArea.background",    UITheme.BG_CARD);
            UIManager.put("TextArea.foreground",    UITheme.TEXT_PRIMARY);
            new MainFrame().setVisible(true);
        });
    }
}
