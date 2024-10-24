import java.sql.*;

public class SelectedCourse extends DatabaseHandler {
    private String teacherId;
    private String courseId;
    private int year;
    private int semester;
    private int credit;

    public static final String[] SEMESTERS = {
        "春季学期",
        "夏季学期",
        "秋季学期"
    };

    public SelectedCourse(String teacherId, String courseId) {
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    public SelectedCourse(String teacherId, String courseId, String year, int semester, String credit) {
        this.teacherId = teacherId;
        this.courseId = courseId;
        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("上课年份必须是整数");
        }
        this.semester = semester;
        try {
            this.credit = Integer.parseInt(credit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("承担学时必须是整数");
        }
    }

    public String getTeacherId() { return teacherId; }
    public String getCourseId() { return courseId; }
    public int getYear() { return year; }
    public int getSemester() { return semester; }
    public int getCredit() { return credit; }

    public void setYear(int year) { this.year = year; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setCredit(int credit) { this.credit = credit; }

    public void insertSelectedCourse() {
        String sql = "INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, courseId);
            stmt.setInt(3, year);
            stmt.setInt(4, semester);
            stmt.setInt(5, credit);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void updateSelectedCourse() {
        String sql = "UPDATE SelectedCourses SET year = ?, semester = ?, credit = ? WHERE teacher_id = ? AND course_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            stmt.setInt(2, semester);
            stmt.setInt(3, credit);
            stmt.setString(4, teacherId);
            stmt.setString(5, courseId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void deleteSelectedCourse() {
        String sql = "DELETE FROM SelectedCourses WHERE teacher_id = ? AND course_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, courseId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public void getSelectedCourse() {
        String sql = "SELECT * FROM SelectedCourses WHERE teacher_id = ? AND course_id = ?";

        try (Connection conn = connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, teacherId);
            stmt.setString(2, courseId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                year = rs.getInt("year");
                semester = rs.getInt("semester");
                credit = rs.getInt("credit");
            } else {
                throw new IllegalArgumentException("找不到主讲课程信息");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    public static int checkCourseCredit(String courseId, String yearString, int semester) {
        int year;

        try {
            year = Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("上课年份必须是整数");
        }
        if (semester == 0) {
            throw new IllegalArgumentException("请选择上课学期");
        }

        String sql = "CALL CheckCourseCredit(?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, courseId);
            stmt.setInt(2, year);
            stmt.setInt(3, semester);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.registerOutParameter(5, Types.INTEGER);

            stmt.execute();

            int totalCredit = stmt.getInt(4);
            int courseCredit = stmt.getInt(5);
            return courseCredit - totalCredit;
        } catch (SQLException e) {
            throw new IllegalArgumentException("数据库错误: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return toString(", ");
    }

    public String toString(String delimiter) {
        return year + delimiter +
               SEMESTERS[semester-1] + delimiter +
               "承担" + credit + "学时";
    }
}
