package criminaldb.ui;

import criminaldb.db.*;
import criminaldb.utils.UITheme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

// ═══════════════════════════════════════════════════════════════
//  CRIME PANEL
// ═══════════════════════════════════════════════════════════════
class CrimePanel extends JPanel {
    private final CaseDAO dao = new CaseDAO();
    private final CriminalDAO cdao = new CriminalDAO();
    private DefaultTableModel model;
    private JComboBox<String> cbCriminal;
    private List<criminaldb.model.Criminal> criminals;

    CrimePanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        // Header
        JLabel hdr = new JLabel("CRIME RECORDS");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        hdr.setBorder(BorderFactory.createEmptyBorder(18,24,8,24));
        add(hdr, BorderLayout.NORTH);

        // Filter row
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filter.setBackground(UITheme.BG_DARK);
        filter.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        criminals = cdao.getAllCriminals();
        cbCriminal = new JComboBox<>();
        cbCriminal.addItem("All Criminals");
        for (criminaldb.model.Criminal c : criminals) cbCriminal.addItem(c.getCriminalId()+": "+c.getName());
        cbCriminal.setBackground(UITheme.BG_CARD); cbCriminal.setForeground(UITheme.TEXT_PRIMARY);
        cbCriminal.setFont(UITheme.fontBody(12));
        JButton btnFilter = new JButton("Filter");
        UITheme.styleButton(btnFilter, UITheme.ACCENT_BLUE);
        btnFilter.addActionListener(e -> filterByCriminal());
        filter.add(new JLabel("Filter by Criminal:") {{ setForeground(UITheme.TEXT_MUTED); setFont(UITheme.fontBody(12)); }});
        filter.add(cbCriminal); filter.add(btnFilter);
        add(filter, BorderLayout.CENTER);

        // Table
        String[] cols = {"Crime Type","Date","Location","Status","Severity","Description"};
        model = new DefaultTableModel(cols, 0){ public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model);
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder(0,24,24,24));
        add(scroll, BorderLayout.SOUTH);

        loadAll();
    }

    private void loadAll() {
        model.setRowCount(0);
        for (criminaldb.model.Criminal c : criminals) {
            for (Object[] row : dao.getCrimesForCriminal(c.getCriminalId()))
                model.addRow(row);
        }
    }

    private void filterByCriminal() {
        int idx = cbCriminal.getSelectedIndex();
        if (idx == 0) { loadAll(); return; }
        int cid = criminals.get(idx-1).getCriminalId();
        model.setRowCount(0);
        for (Object[] row : dao.getCrimesForCriminal(cid)) model.addRow(row);
    }

    private void styleTable(JTable t) {
        t.setBackground(UITheme.BG_CARD); t.setForeground(UITheme.TEXT_PRIMARY);
        t.setFont(UITheme.fontBody(12)); t.setRowHeight(28);
        t.setGridColor(UITheme.BORDER); t.setShowHorizontalLines(true); t.setShowVerticalLines(false);
        t.getTableHeader().setBackground(UITheme.BG_SIDEBAR); t.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        t.setAutoCreateRowSorter(true);
    }
}

// ═══════════════════════════════════════════════════════════════
//  CASES PANEL
// ═══════════════════════════════════════════════════════════════
class CasesPanel extends JPanel {
    private final CaseDAO dao = new CaseDAO();
    private DefaultTableModel model;

    CasesPanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        JLabel hdr = new JLabel("CASE MANAGEMENT");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        hdr.setBorder(BorderFactory.createEmptyBorder(18,24,10,24));
        add(hdr, BorderLayout.NORTH);

        String[] cols = {"Case No.","Criminal","Assigned Officer","Title","Status","Date Opened","Court Date"};
        model = new DefaultTableModel(cols, 0){ public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD); table.setForeground(UITheme.TEXT_PRIMARY);
        table.setFont(UITheme.fontBody(12)); table.setRowHeight(28);
        table.setGridColor(UITheme.BORDER); table.setShowHorizontalLines(true); table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(UITheme.BG_SIDEBAR);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                String status = (String)t.getValueAt(r,4);
                setBackground(sel ? UITheme.ACCENT_BLUE.darker() : (r%2==0?UITheme.BG_CARD:UITheme.TABLE_ALT));
                if(c==4){
                    if("Solved".equals(status)) setForeground(UITheme.ACCENT_GREEN);
                    else if("Open".equals(status)) setForeground(UITheme.ACCENT_AMBER);
                    else setForeground(UITheme.TEXT_PRIMARY);
                } else setForeground(UITheme.TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(4,8,4,8));
                return this;
            }
        });
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder(0,24,24,24));
        add(scroll, BorderLayout.CENTER);

        for (Object[] row : dao.getAllCasesForTable()) model.addRow(row);
    }
}

// ═══════════════════════════════════════════════════════════════
//  OFFICER PANEL
// ═══════════════════════════════════════════════════════════════
class OfficerPanel extends JPanel {
    private final CaseDAO dao = new CaseDAO();
    private DefaultTableModel model;

    OfficerPanel() {
        setBackground(UITheme.BG_DARK);
        setLayout(new BorderLayout());

        JLabel hdr = new JLabel("POLICE OFFICERS");
        hdr.setFont(UITheme.fontTitle(20));
        hdr.setForeground(UITheme.TEXT_PRIMARY);
        hdr.setBorder(BorderFactory.createEmptyBorder(18,24,10,24));
        add(hdr, BorderLayout.NORTH);

        String[] cols = {"ID","Badge No.","Name","Rank","Station"};
        model = new DefaultTableModel(cols, 0){ public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model);
        table.setBackground(UITheme.BG_CARD); table.setForeground(UITheme.TEXT_PRIMARY);
        table.setFont(UITheme.fontBody(12)); table.setRowHeight(30);
        table.setGridColor(UITheme.BORDER); table.setShowHorizontalLines(true); table.setShowVerticalLines(false);
        table.getTableHeader().setBackground(UITheme.BG_SIDEBAR);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(UITheme.BG_DARK);
        scroll.setBorder(BorderFactory.createEmptyBorder(0,24,24,24));
        add(scroll, BorderLayout.CENTER);

        for (Object[] row : dao.getAllOfficers()) model.addRow(row);
    }
}
