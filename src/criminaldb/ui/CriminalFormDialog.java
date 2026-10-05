package criminaldb.ui;

import criminaldb.db.CriminalDAO;
import criminaldb.model.Criminal;
import criminaldb.utils.UITheme;
import javax.swing.*;
import java.awt.*;

public class CriminalFormDialog extends JDialog {

    private final Criminal existing;
    private final CriminalDAO dao;

    private JTextField tfName, tfAge, tfAddress, tfNationality, tfHeight, tfWeight;
    private JComboBox<String> cbGender, cbBlood, cbWanted;

    public CriminalFormDialog(JFrame parent, Criminal existing, CriminalDAO dao) {
        super(parent, existing == null ? "Add New Criminal" : "Edit Criminal", true);
        this.existing = existing;
        this.dao      = dao;
        buildUI();
        if (existing != null) populateFields();
        pack();
        setSize(480, 500);
        setLocationRelativeTo(parent);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(0, 16));
        main.setBackground(UITheme.BG_PANEL);
        main.setBorder(BorderFactory.createEmptyBorder(24, 28, 20, 28));

        JLabel title = new JLabel(existing == null ? "ADD CRIMINAL RECORD" : "EDIT CRIMINAL RECORD");
        title.setFont(UITheme.fontTitle(16));
        title.setForeground(UITheme.ACCENT_BLUE);
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.BG_PANEL);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        tfName        = field(); tfAge = field(); tfAddress = field();
        tfNationality = field("Indian"); tfHeight = field(); tfWeight = field();
        cbGender = combo("Male","Female","Other");
        cbBlood  = combo("A+","A-","B+","B-","O+","O-","AB+","AB-");
        cbWanted = combo("Wanted","In Custody");

        addRow(form, g, 0, "Full Name *",   tfName);
        addRow(form, g, 1, "Age",           tfAge);
        addRow(form, g, 2, "Gender",        cbGender);
        addRow(form, g, 3, "Address",       tfAddress);
        addRow(form, g, 4, "Nationality",   tfNationality);
        addRow(form, g, 5, "Height (cm)",   tfHeight);
        addRow(form, g, 6, "Weight (kg)",   tfWeight);
        addRow(form, g, 7, "Blood Group",   cbBlood);
        addRow(form, g, 8, "Status",        cbWanted);
        main.add(form, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btns.setBackground(UITheme.BG_PANEL);
        JButton save   = new JButton(existing == null ? "Save" : "Update");
        JButton cancel = new JButton("Cancel");
        UITheme.styleButton(save,   UITheme.ACCENT_BLUE);
        UITheme.styleButton(cancel, UITheme.BG_CARD);
        save.addActionListener  (e -> onSave());
        cancel.addActionListener(e -> dispose());
        btns.add(cancel); btns.add(save);
        main.add(btns, BorderLayout.SOUTH);
        setContentPane(main);
    }

    private void addRow(JPanel form, GridBagConstraints g, int row, String label, JComponent comp) {
        g.gridx=0; g.gridy=row; g.weightx=0.3;
        JLabel lbl = new JLabel(label); lbl.setForeground(UITheme.TEXT_MUTED);
        lbl.setFont(UITheme.fontBody(12));
        form.add(lbl, g);
        g.gridx=1; g.weightx=0.7;
        form.add(comp, g);
    }

    private void populateFields() {
        tfName.setText(existing.getName());
        tfAge.setText(String.valueOf(existing.getAge()));
        cbGender.setSelectedItem(existing.getGender());
        tfAddress.setText(existing.getAddress());
        tfNationality.setText(existing.getNationality());
        tfHeight.setText(String.valueOf(existing.getHeightCm()));
        tfWeight.setText(String.valueOf(existing.getWeightKg()));
        cbBlood.setSelectedItem(existing.getBloodGroup());
        cbWanted.setSelectedIndex(existing.isWanted() ? 0 : 1);
    }

    private void onSave() {
        if (tfName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Criminal c = existing == null ? new Criminal() : existing;
        c.setName(tfName.getText().trim());
        try { c.setAge(Integer.parseInt(tfAge.getText().trim())); } catch (NumberFormatException e) { c.setAge(0); }
        c.setGender((String) cbGender.getSelectedItem());
        c.setAddress(tfAddress.getText().trim());
        c.setNationality(tfNationality.getText().trim());
        try { c.setHeightCm(Integer.parseInt(tfHeight.getText().trim())); } catch (NumberFormatException e) { c.setHeightCm(0); }
        try { c.setWeightKg(Integer.parseInt(tfWeight.getText().trim())); } catch (NumberFormatException e) { c.setWeightKg(0); }
        c.setBloodGroup((String) cbBlood.getSelectedItem());
        c.setWanted(cbWanted.getSelectedIndex() == 0);

        boolean ok = existing == null ? dao.addCriminal(c) : dao.updateCriminal(c);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Saved successfully.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "DB error — check connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(UITheme.BG_CARD); f.setForeground(UITheme.TEXT_PRIMARY);
        f.setCaretColor(UITheme.TEXT_PRIMARY); f.setFont(UITheme.fontBody(12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(4,8,4,8)));
        return f;
    }

    private JTextField field(String text) { JTextField f = field(); f.setText(text); return f; }

    private JComboBox<String> combo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(UITheme.BG_CARD); cb.setForeground(UITheme.TEXT_PRIMARY);
        cb.setFont(UITheme.fontBody(12));
        return cb;
    }
}
