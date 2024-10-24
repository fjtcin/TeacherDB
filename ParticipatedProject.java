import java.sql.*;

public class ParticipatedProject extends DatabaseHandler {
    private String teacherId;
    private String projectId;
    private int ranking;
    private float fund;

    public ParticipatedProject(String teacherId, String projectId) {
        this.teacherId = teacherId;
        this.projectId = projectId;
    }

    public ParticipatedProject(String teacherId, String projectId, String ranking, String fund) {
        this.teacherId = teacherId;
        this.projectId = projectId;
        try {
            this.ranking = Integer.parseInt(ranking);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("项目排名必须是整数");
        }
        try {
            this.fund = Float.parseFloat(fund);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("承担经费必须是数");
        }
    }

    public String getTeacherId() { return teacherId; }
    public String getProjectId() { return projectId; }
    public int getRanking() { return ranking; }
    public float getFund() { return fund; }

    public void setRanking(int ranking) { this.ranking = ranking; }
    public void setFund(float fund) { this.fund = fund; }

    public void insertParticipatedProject() {
        String sql = "INSERT INTO ParticipatedProjects (teacher_id, project_id, ranking, fund) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, projectId);
            stmt.setInt(3, ranking);
            stmt.setFloat(4, fund);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updateParticipatedProject() {
        String sql = "UPDATE ParticipatedProjects SET ranking = ?, fund = ? WHERE teacher_id = ? AND project_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ranking);
            stmt.setFloat(2, fund);
            stmt.setString(3, teacherId);
            stmt.setString(4, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deleteParticipatedProject() {
        String sql = "DELETE FROM ParticipatedProjects WHERE teacher_id = ? AND project_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, projectId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getParticipatedProject() {
        String sql = "SELECT * FROM ParticipatedProjects WHERE teacher_id = ? AND project_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, projectId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ranking = rs.getInt("ranking");
                fund = rs.getFloat("fund");
            } else {
                throw new IllegalArgumentException("找不到承担项目信息");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public static float checkProjectFund(String projectId) {
        String sql = "CALL CheckProjectFund(?, ?, ?)";

        try (Connection conn = connectDatabase();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, projectId);
            stmt.registerOutParameter(2, Types.FLOAT);
            stmt.registerOutParameter(3, Types.FLOAT);

            stmt.execute();

            float totalFund = stmt.getFloat(2);
            float projectFund = stmt.getFloat(3);
            return projectFund - totalFund;
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return toString(", ");
    }

    public String toString(String delimiter) {
        return "排名" + ranking + delimiter +
               "承担经费" + fund;
    }
}
