import java.sql.*;

public class Paper extends DatabaseHandler {
    private int id;
    private String name;
    private String source;
    private int year;
    private int type;
    private int level;

    public static final String[] TYPES = {
        "Full Paper",
        "Short Paper",
        "Poster Paper",
        "Demo Paper"
    };
    public static final String[] LEVELS = {
        "CCF-A",
        "CCF-B",
        "CCF-C",
        "中文 CCF-A",
        "中文 CCF-B",
        "无级别"
    };

    public Paper(String id) {
        try {
            this.id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("论文序号必须是整数");
        }
    }

    public Paper(String id, String name, String source, String year, int type, int level) {
        try {
            this.id = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("论文序号必须是整数");
        }
        this.name = name;
        this.source = source;
        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("发表年份必须是整数");
        }
        this.type = type;
        this.level = level;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSource() { return source; }
    public int getYear() { return year; }
    public int getType() { return type; }
    public int getLevel() { return level; }

    public void setName(String name) { this.name = name; }
    public void setSource(String source) { this.source = source; }
    public void setYear(int year) { this.year = year; }
    public void setType(int type) { this.type = type; }
    public void setLevel(int level) { this.level = level; }

    public void insertPaper() {
        String sql = "INSERT INTO Papers (id, name, source, year, type, level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, source);
            stmt.setInt(4, year);
            stmt.setInt(5, type);
            stmt.setInt(6, level);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updatePaper() {
        String sql = "UPDATE Papers SET name = ?, source = ?, year = ?, type = ?, level = ? WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, source);
            stmt.setInt(3, year);
            stmt.setInt(4, type);
            stmt.setInt(5, level);
            stmt.setInt(6, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deletePaper() {
        String sql = "DELETE FROM Papers WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getPaper() {
        String sql = "SELECT * FROM Papers WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
                source = rs.getString("source");
                year = rs.getInt("year");
                type = rs.getInt("type");
                level = rs.getInt("level");
            } else {
                throw new IllegalArgumentException("找不到论文信息");
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
               source + delimiter +
               year + delimiter +
               TYPES[type-1] + delimiter +
               LEVELS[level-1];
    }
}
