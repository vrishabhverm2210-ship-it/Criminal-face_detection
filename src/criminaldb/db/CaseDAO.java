package criminaldb.db;

import java.sql.*;
import java.util.*;

public class CaseDAO {

    // ── ALL CASES ─────────────────────────────────────────────
    public List<Object[]> getAllCasesForTable() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT ca.case_number, cr.name AS criminal, po.name AS officer, " +
                     "ca.case_title, ca.case_status, ca.date_opened, ca.court_date " +
                     "FROM cases ca " +
                     "JOIN criminals cr ON ca.criminal_id=cr.criminal_id " +
                     "JOIN police_officers po ON ca.officer_id=po.officer_id " +
                     "ORDER BY ca.case_id DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("case_number"), rs.getString("criminal"),
                    rs.getString("officer"), rs.getString("case_title"),
                    rs.getString("case_status"), rs.getString("date_opened"),
                    rs.getString("court_date")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── CRIMES FOR CRIMINAL ───────────────────────────────────
    public List<Object[]> getCrimesForCriminal(int criminalId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT crime_type, date_occurred, location, status, severity, description " +
                     "FROM crimes WHERE criminal_id=? ORDER BY date_occurred DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, criminalId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("crime_type"), rs.getString("date_occurred"),
                    rs.getString("location"), rs.getString("status"),
                    rs.getString("severity"), rs.getString("description")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── ADD CRIME ─────────────────────────────────────────────
    public boolean addCrime(int criminalId, String type, String desc, String date, String loc, String status, String severity) {
        String sql = "INSERT INTO crimes (criminal_id,crime_type,description,date_occurred,location,status,severity) VALUES(?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt   (1, criminalId);
            ps.setString(2, type);
            ps.setString(3, desc);
            ps.setString(4, date);
            ps.setString(5, loc);
            ps.setString(6, status);
            ps.setString(7, severity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── ADD CASE ──────────────────────────────────────────────
    public boolean addCase(String caseNum, int criminalId, int officerId, String title, String status, String dateOpened) {
        String sql = "INSERT INTO cases (case_number,criminal_id,officer_id,case_title,case_status,date_opened) VALUES(?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, caseNum);
            ps.setInt   (2, criminalId);
            ps.setInt   (3, officerId);
            ps.setString(4, title);
            ps.setString(5, status);
            ps.setString(6, dateOpened);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── ALL OFFICERS ──────────────────────────────────────────
    public List<Object[]> getAllOfficers() {
        List<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT officer_id, badge_number, name, rank, station FROM police_officers ORDER BY rank")) {
            while (rs.next()) {
                list.add(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
