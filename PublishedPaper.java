import java.sql.*;

public class PublishedPaper extends DatabaseHandler {
    private String teacherId;
    private int paperId;
    private int ranking;
    private boolean contact;

    public PublishedPaper(String teacherId, String paperId) {
        this.teacherId = teacherId;
        try {
            this.paperId = Integer.parseInt(paperId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("论文序号必须是整数");
        }
    }

    public PublishedPaper(String teacherId, String paperId, String ranking, boolean contact) {
        this.teacherId = teacherId;
        try {
            this.paperId = Integer.parseInt(paperId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("论文序号必须是整数");
        }
        try {
            this.ranking = Integer.parseInt(ranking);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("排名必须是整数");
        }
        this.contact = contact;
    }

    public String getTeacherId() { return teacherId; }
    public int getPaperId() { return paperId; }
    public int getRanking() { return ranking; }
    public boolean isContact() { return contact; }

    public void setRanking(int ranking) { this.ranking = ranking; }
    public void setContact(boolean contact) { this.contact = contact; }

    public void insertPublishedPaper() {
        String sql = "INSERT INTO PublishedPapers (teacher_id, paper_id, ranking, contact) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setInt(2, paperId);
            stmt.setInt(3, ranking);
            stmt.setBoolean(4, contact);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updatePublishedPaper() {
        String sql = "UPDATE PublishedPapers SET ranking = ?, contact = ? WHERE teacher_id = ? AND paper_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ranking);
            stmt.setBoolean(2, contact);
            stmt.setString(3, teacherId);
            stmt.setInt(4, paperId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deletePublishedPaper() {
        String sql = "DELETE FROM PublishedPapers WHERE teacher_id = ? AND paper_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setInt(2, paperId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getPublishedPaper() {
        String sql = "SELECT * FROM PublishedPapers WHERE teacher_id = ? AND paper_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setInt(2, paperId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ranking = rs.getInt("ranking");
                contact = rs.getBoolean("contact");
            } else {
                throw new IllegalArgumentException("找不到发表论文信息");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return toString(", ");
    }

    public String toString(String delimiter) {
        return "排名" + ranking +
               (contact ? delimiter + "通讯作者" : "");
    }
}
