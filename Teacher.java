import java.sql.*;

public class Teacher extends DatabaseHandler {
    private String id;
    private String name;
    private int gender;
    private int title;

    public static final String[] GENDERS = {
        "男",
        "女"
    };
    public static final String[] TITLES = {
        "博士后",
        "助教",
        "讲师",
        "副教授",
        "特任教授",
        "教授",
        "助理研究员",
        "特任副研究员",
        "副研究员",
        "特任研究员",
        "研究员"
    };

    public Teacher(String id) {
        this.id = id;
    }

    public Teacher(String id, String name, int gender, int title) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.title = title;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getGender() { return gender; }
    public int getTitle() { return title; }

    public void setName(String name) { this.name = name; }
    public void setGender(int gender) { this.gender = gender; }
    public void setTitle(int title) { this.title = title; }

    public void insertTeacher() {
        String sql = "INSERT INTO Teachers (id, name, gender, title) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setInt(3, gender);
            stmt.setInt(4, title);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updateTeacher() {
        String sql = "UPDATE Teachers SET name = ?, gender = ?, title = ? WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, gender);
            stmt.setInt(3, title);
            stmt.setString(4, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deleteTeacher() {
        String sql = "DELETE FROM Teachers WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getTeacher() {
        String sql = "SELECT * FROM Teachers WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
                gender = rs.getInt("gender");
                title = rs.getInt("title");
            } else {
                throw new IllegalArgumentException("找不到教师信息");
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
               GENDERS[gender-1] + delimiter +
               TITLES[title-1];
    }
}
