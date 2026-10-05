package criminaldb.ui;

import criminaldb.db.CriminalDAO;
import criminaldb.utils.UITheme;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AnalyticsPanel extends JPanel {

    private final CriminalDAO dao = new CriminalDAO();
    private BarChartPanel  barChart;
    private PieChartPanel  pieChart;

    public AnalyticsPanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());
        build();
    }

    private void build() {
        JLabel hdr = new JLabel("ANALYTICS & STATISTICS");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        hdr.setBorder(BorderFactory.createEmptyBorder(18,24,10,24));
        add(hdr, BorderLayout.NORTH);

        JPanel charts = new JPanel(new GridLayout(1, 2, 20, 0));
        charts.setBackground(UITheme.BG_DARK);
        charts.setBorder(BorderFactory.createEmptyBorder(0, 24, 24, 24));

        barChart = new BarChartPanel();
        pieChart = new PieChartPanel();

        JPanel barWrap = wrap("CRIMES BY TYPE", barChart);
        JPanel pieWrap = wrap("CASE STATUS BREAKDOWN", pieChart);
        charts.add(barWrap);
        charts.add(pieWrap);
        add(charts, BorderLayout.CENTER);

        refresh();
    }

    private JPanel wrap(String title, JPanel inner) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(UITheme.BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        JLabel lbl = new JLabel(title);
        lbl.setFont(UITheme.fontTitle(13));
        lbl.setForeground(UITheme.TEXT_MUTED);
        p.add(lbl, BorderLayout.NORTH);
        p.add(inner, BorderLayout.CENTER);
        return p;
    }

    public void refresh() {
        barChart.setData(dao.getCrimeTypeStats());
        pieChart.setData(dao.getCaseStatusStats());
        barChart.repaint();
        pieChart.repaint();
    }
}

// ─── Bar Chart ──────────────────────────────────────────────────
class BarChartPanel extends JPanel {
    private Map<String,Integer> data = new LinkedHashMap<>();
    private static final Color[] COLORS = {
        new Color(0x2979FF), new Color(0xFF3D3D), new Color(0xFFAB00),
        new Color(0x00E676), new Color(0xCF6679), new Color(0x40C4FF),
        new Color(0xEA80FC), new Color(0xCCFF90)
    };

    BarChartPanel() { setBackground(UITheme.BG_CARD); setPreferredSize(new Dimension(400,300)); }

    void setData(Map<String,Integer> d) { this.data = d; }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (data.isEmpty()) return;
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = 44, w = getWidth(), h = getHeight();
        int chartW = w - pad*2, chartH = h - pad*2;
        int maxVal = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        int n = data.size();
        int barW = chartW / (n*2);

        int x = pad; int ci = 0;
        for (Map.Entry<String,Integer> e : data.entrySet()) {
            int barH = (int)((double)e.getValue()/maxVal * chartH);
            int bx = x + barW/2;
            int by = pad + chartH - barH;

            g.setColor(COLORS[ci % COLORS.length]);
            g.fillRoundRect(bx, by, barW, barH, 4, 4);

            // Value label
            g.setColor(UITheme.TEXT_PRIMARY);
            g.setFont(UITheme.fontBody(10));
            g.drawString(String.valueOf(e.getValue()), bx + barW/2 - 4, by - 4);

            // X label (rotated)
            g.setColor(UITheme.TEXT_MUTED);
            g.setFont(UITheme.fontBody(9));
            String label = e.getKey().length() > 12 ? e.getKey().substring(0,12)+"…" : e.getKey();
            java.awt.geom.AffineTransform orig = g.getTransform();
            g.translate(bx + barW/2, pad + chartH + 6);
            g.rotate(-Math.PI/4);
            g.drawString(label, 0, 0);
            g.setTransform(orig);

            x += barW*2; ci++;
        }

        // Y-axis
        g.setColor(UITheme.BORDER);
        g.drawLine(pad, pad, pad, pad+chartH);
        g.drawLine(pad, pad+chartH, pad+chartW, pad+chartH);
    }
}

// ─── Pie Chart ──────────────────────────────────────────────────
class PieChartPanel extends JPanel {
    private Map<String,Integer> data = new LinkedHashMap<>();
    private static final Color[] COLORS = {
        new Color(0xFFAB00), new Color(0x2979FF), new Color(0x00E676),
        new Color(0xFF3D3D), new Color(0xCF6679)
    };

    PieChartPanel() { setBackground(UITheme.BG_CARD); setPreferredSize(new Dimension(400,300)); }

    void setData(Map<String,Integer> d) { this.data = d; }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (data.isEmpty()) return;
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return;

        int cx = getWidth()/2 - 40, cy = getHeight()/2, r = Math.min(cx,cy) - 20;
        int startAngle = 0; int ci = 0;

        for (Map.Entry<String,Integer> e : data.entrySet()) {
            int angle = (int) Math.round((double)e.getValue()/total*360);
            g.setColor(COLORS[ci % COLORS.length]);
            g.fillArc(cx-r, cy-r, r*2, r*2, startAngle, angle);
            g.setColor(UITheme.BG_CARD);
            g.drawArc(cx-r, cy-r, r*2, r*2, startAngle, angle);
            startAngle += angle; ci++;
        }

        // Legend
        int lx = getWidth() - 130, ly = 30; ci = 0;
        for (Map.Entry<String,Integer> e : data.entrySet()) {
            g.setColor(COLORS[ci % COLORS.length]);
            g.fillRect(lx, ly, 14, 14);
            g.setColor(UITheme.TEXT_PRIMARY);
            g.setFont(UITheme.fontBody(11));
            int pct = (int) Math.round((double)e.getValue()/total*100);
            g.drawString(e.getKey()+" ("+pct+"%)", lx+18, ly+12);
            ly += 24; ci++;
        }
    }
}
