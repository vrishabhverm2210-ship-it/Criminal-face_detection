package criminaldb.utils;

import java.awt.*;

/**
 * Centralized UI theme — dark police / surveillance aesthetic.
 */
public class UITheme {

    // ── PALETTE ───────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(0x0D0F14);
    public static final Color BG_PANEL     = new Color(0x141820);
    public static final Color BG_CARD      = new Color(0x1C2130);
    public static final Color BG_SIDEBAR   = new Color(0x0A0C10);
    public static final Color ACCENT_BLUE  = new Color(0x2979FF);
    public static final Color ACCENT_RED   = new Color(0xFF3D3D);
    public static final Color ACCENT_GREEN = new Color(0x00E676);
    public static final Color ACCENT_AMBER = new Color(0xFFAB00);
    public static final Color TEXT_PRIMARY = new Color(0xECEFF4);
    public static final Color TEXT_MUTED   = new Color(0x7B88A0);
    public static final Color BORDER       = new Color(0x252D3D);
    public static final Color TABLE_ALT    = new Color(0x171E2A);
    public static final Color WANTED_RED   = new Color(0xFF3D3D);
    public static final Color SAFE_GREEN   = new Color(0x00C853);

    // ── FONTS ─────────────────────────────────────────────────
    public static Font fontTitle(int size)  { return new Font("Segoe UI", Font.BOLD, size); }
    public static Font fontBody(int size)   { return new Font("Segoe UI", Font.PLAIN, size); }
    public static Font fontMono(int size)   { return new Font("Consolas", Font.PLAIN, size); }

    // ── HELPER: rounded border ────────────────────────────────
    public static javax.swing.border.Border cardBorder() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }

    public static void styleButton(javax.swing.JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(fontBody(12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }
}
