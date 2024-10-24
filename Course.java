import java.sql.*;

public class Course extends DatabaseHandler {
    private String id;
    private String name;
    private int credit;
    private int type;

    public static final String[] TYPES = {
        "本科生课程",
        "研究生课程"
    };

    public Course(String id) {
        this.id = id;
    }

    public Course(String id, String name, String credit, int type) {
        this.id = id;
        this.name = name;
        try {
            this.credit = Integer.parseInt(credit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("课程学时必须是整数");
        }
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCredit() { return credit; }
    public int getType() { return type; }

    public void setName(String name) { this.name = name; }
    public void setCredit(int credit) { this.credit = credit; }
    public void setType(int type) { this.type = type; }

    private void checkUpdateCourse() {
        String sql = "SELECT credit FROM Courses WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (credit != rs.getInt("credit")) {
                    throw new IllegalArgumentException("课程学时不可更改");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void insertCourse() {
        String sql = "INSERT INTO Courses (id, name, credit, type) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, credit);
            stmt.setInt(4, type);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updateCourse() {
        checkUpdateCourse();
        String sql = "UPDATE Courses SET name = ?, type = ? WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, type);
            stmt.setString(3, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deleteCourse() {
        String sql = "DELETE FROM Courses WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getCourse() {
        String sql = "SELECT * FROM Courses WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
                credit = rs.getInt("credit");
                type = rs.getInt("type");
            } else {
                throw new IllegalArgumentException("找不到课程信息");
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
        return name + delimiter +
               credit + "学时" + delimiter +
               TYPES[type-1];
    }
}
