package criminaldb.ui;

import criminaldb.db.CriminalDAO;
import criminaldb.utils.UITheme;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private final MainFrame  frame;
    private final CriminalDAO dao = new CriminalDAO();
    private JLabel lblTotal, lblWanted, lblCrimes, lblOpen, lblSolved;

    public DashboardPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());
        build();
    }

    private void build() {
        // ── Header ─────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(BorderFactory.createEmptyBorder(24, 28, 8, 28));

        JLabel title = new JLabel("SYSTEM DASHBOARD");
        title.setFont(UITheme.fontTitle(22));
        title.setForeground(UITheme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("Criminal Intelligence & Case Management System");
        sub.setFont(UITheme.fontBody(12));
        sub.setForeground(UITheme.TEXT_MUTED);
        header.add(sub, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── Stat Cards ─────────────────────────────────────────
        JPanel cards = new JPanel(new GridLayout(1, 5, 14, 0));
        cards.setBackground(UITheme.BG_DARK);
        cards.setBorder(BorderFactory.createEmptyBorder(10, 28, 20, 28));

        lblTotal  = statCard(cards, "TOTAL CRIMINALS",  "...", UITheme.ACCENT_BLUE);
        lblWanted = statCard(cards, "WANTED",           "...", UITheme.ACCENT_RED);
        lblCrimes = statCard(cards, "TOTAL CRIMES",     "...", UITheme.ACCENT_AMBER);
        lblOpen   = statCard(cards, "OPEN CASES",       "...", new Color(0xCF6679));
        lblSolved = statCard(cards, "CASES SOLVED",     "...", UITheme.ACCENT_GREEN);

        add(cards, BorderLayout.CENTER);

        // ── Quick Action Buttons ───────────────────────────────
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        actions.setBackground(UITheme.BG_DARK);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 24));

        addAction(actions, "➕  Add Criminal",  UITheme.ACCENT_BLUE,  "criminals");
        addAction(actions, "📷  Face Scanner",  UITheme.ACCENT_GREEN, "facerecog");
        addAction(actions, "📊  Analytics",     UITheme.ACCENT_AMBER, "analytics");
        addAction(actions, "📁  View Cases",    new Color(0x8C68CD),  "cases");
        add(actions, BorderLayout.SOUTH);

        refresh();
    }

    private JLabel statCard(JPanel parent, String title, String val, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(UITheme.fontBody(10));
        lTitle.setForeground(UITheme.TEXT_MUTED);

        JLabel lVal = new JLabel(val);
        lVal.setFont(UITheme.fontTitle(32));
        lVal.setForeground(accent);

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lVal,   BorderLayout.CENTER);
        parent.add(card);
        return lVal;
    }

    private void addAction(JPanel parent, String text, Color bg, String panel) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(160, 40));
        UITheme.styleButton(btn, bg);
        btn.addActionListener(e -> frame.showPanel(panel));
        parent.add(btn);
    }

    public void refresh() {
        try {
            lblTotal.setText(String.valueOf(dao.getTotalCriminals()));
            lblWanted.setText(String.valueOf(dao.getWantedCount()));
            lblCrimes.setText(String.valueOf(dao.getTotalCrimes()));
            lblOpen.setText(String.valueOf(dao.getOpenCases()));
            lblSolved.setText(String.valueOf(dao.getSolvedCases()));
        } catch (Exception e) {
            lblTotal.setText("DB ERR");
            lblTotal.setForeground(UITheme.ACCENT_RED);
        }
    }
}
