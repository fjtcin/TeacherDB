import java.sql.*;

public class Project extends DatabaseHandler {
    private String id;
    private String name;
    private String source;
    private int type;
    private float fund;
    private int startYear;
    private int endYear;

    public static final String[] TYPES = {
        "国家级",
        "省部级",
        "市厅级",
        "企业合作",
        "其他"
    };

    public Project(String id) {
        this.id = id;
    }

    public Project(String id, String name, String source, int type, String fund, String startYear, String endYear) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.type = type;
        try {
            this.fund = Float.parseFloat(fund);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("项目经费必须是数");
        }
        try {
            this.startYear = Integer.parseInt(startYear);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("开始年份必须是整数");
        }
        try {
            this.endYear = Integer.parseInt(endYear);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("结束年份必须是整数");
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSource() { return source; }
    public int getType() { return type; }
    public float getFund() { return fund; }
    public int getStartYear() { return startYear; }
    public int getEndYear() { return endYear; }

    public void setName(String name) { this.name = name; }
    public void setSource(String source) { this.source = source; }
    public void setType(int type) { this.type = type; }
    public void setFund(float fund) { this.fund = fund; }
    public void setStartYear(int startYear) { this.startYear = startYear; }
    public void setEndYear(int endYear) { this.endYear = endYear; }

    private void checkUpdateProject() {
        String sql = "SELECT fund FROM Projects WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (fund != rs.getInt("fund")) {
                    throw new IllegalArgumentException("项目经费不可更改");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void insertProject() {
        String sql = "INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, source);
            stmt.setInt(4, type);
            stmt.setFloat(5, fund);
            stmt.setInt(6, startYear);
            stmt.setInt(7, endYear);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updateProject() {
        checkUpdateProject();
        String sql = "UPDATE Projects SET name = ?, source = ?, type = ?, start_year = ?, end_year = ? WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, source);
            stmt.setInt(3, type);
            stmt.setInt(4, startYear);
            stmt.setInt(5, endYear);
            stmt.setString(6, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deleteProject() {
        String sql = "DELETE FROM Projects WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getProject() {
        String sql = "SELECT * FROM Projects WHERE id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString("name");
                source = rs.getString("source");
                type = rs.getInt("type");
                fund = rs.getFloat("fund");
                startYear = rs.getInt("start_year");
                endYear = rs.getInt("end_year");
            } else {
                throw new IllegalArgumentException("找不到项目信息");
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
               TYPES[type-1] + delimiter +
               "总经费" + fund + delimiter +
               startYear + "至" +
               endYear;
    }
}
