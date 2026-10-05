package criminaldb.db;

import criminaldb.model.Criminal;
import java.sql.*;
import java.util.*;

public class CriminalDAO {

    // ── INSERT ────────────────────────────────────────────────
    public boolean addCriminal(Criminal c) {
        String sql = "INSERT INTO criminals (name,age,gender,address,nationality,height_cm,weight_kg,blood_group,photo_path,is_wanted) VALUES(?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString (1, c.getName());
            ps.setInt    (2, c.getAge());
            ps.setString (3, c.getGender());
            ps.setString (4, c.getAddress());
            ps.setString (5, c.getNationality());
            ps.setInt    (6, c.getHeightCm());
            ps.setInt    (7, c.getWeightKg());
            ps.setString (8, c.getBloodGroup());
            ps.setString (9, c.getPhotoPath());
            ps.setBoolean(10, c.isWanted());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── UPDATE ────────────────────────────────────────────────
    public boolean updateCriminal(Criminal c) {
        String sql = "UPDATE criminals SET name=?,age=?,gender=?,address=?,nationality=?,height_cm=?,weight_kg=?,blood_group=?,photo_path=?,is_wanted=? WHERE criminal_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString (1, c.getName());
            ps.setInt    (2, c.getAge());
            ps.setString (3, c.getGender());
            ps.setString (4, c.getAddress());
            ps.setString (5, c.getNationality());
            ps.setInt    (6, c.getHeightCm());
            ps.setInt    (7, c.getWeightKg());
            ps.setString (8, c.getBloodGroup());
            ps.setString (9, c.getPhotoPath());
            ps.setBoolean(10, c.isWanted());
            ps.setInt    (11, c.getCriminalId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── DELETE ────────────────────────────────────────────────
    public boolean deleteCriminal(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM criminals WHERE criminal_id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ── GET ALL ───────────────────────────────────────────────
    public List<Criminal> getAllCriminals() {
        List<Criminal> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM criminals ORDER BY criminal_id DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── GET BY ID ─────────────────────────────────────────────
    public Criminal getCriminalById(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM criminals WHERE criminal_id=?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // ── SEARCH ────────────────────────────────────────────────
    public List<Criminal> search(String keyword) {
        List<Criminal> list = new ArrayList<>();
        String sql = "SELECT * FROM criminals WHERE name LIKE ? OR address LIKE ? OR blood_group LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ── ANALYTICS ─────────────────────────────────────────────
    public int getTotalCriminals()  { return count("SELECT COUNT(*) FROM criminals"); }
    public int getWantedCount()     { return count("SELECT COUNT(*) FROM criminals WHERE is_wanted=1"); }
    public int getTotalCrimes()     { return count("SELECT COUNT(*) FROM crimes"); }
    public int getOpenCases()       { return count("SELECT COUNT(*) FROM cases WHERE case_status NOT IN ('Solved','Closed')"); }
    public int getSolvedCases()     { return count("SELECT COUNT(*) FROM cases WHERE case_status='Solved'"); }

    private int count(String sql) {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    /** Returns Map<crimeType, count> for bar chart */
    public Map<String, Integer> getCrimeTypeStats() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT crime_type, COUNT(*) AS cnt FROM crimes GROUP BY crime_type ORDER BY cnt DESC LIMIT 8";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) map.put(rs.getString("crime_type"), rs.getInt("cnt"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    /** Returns Map<status, count> for pie chart */
    public Map<String, Integer> getCaseStatusStats() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT case_status, COUNT(*) AS cnt FROM cases GROUP BY case_status";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) map.put(rs.getString("case_status"), rs.getInt("cnt"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    private Criminal mapRow(ResultSet rs) throws SQLException {
        return new Criminal(
            rs.getInt("criminal_id"), rs.getString("name"),
            rs.getInt("age"), rs.getString("gender"), rs.getString("address"),
            rs.getString("nationality"), rs.getInt("height_cm"),
            rs.getInt("weight_kg"), rs.getString("blood_group"),
            rs.getString("photo_path"), rs.getBoolean("is_wanted")
        );
    }
}
