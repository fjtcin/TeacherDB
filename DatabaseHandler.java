import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USER = "root";
    private static final String PASS = "1234";

    public static Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static String getTeacherWorkloadSummary(String teacherId, String startYearString, String endYearString) {
        int startYear, endYear;

        try {
            startYear = Integer.parseInt(startYearString);
            endYear = Integer.parseInt(endYearString);
        } catch (NumberFormatException e) {
            return "年份必须是整数";
        }

        String sql = "CALL GetTeacherWorkloadSummary(?, ?, ?)";

        try (Connection conn = connectDatabase();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, teacherId);
            stmt.setInt(2, startYear);
            stmt.setInt(3, endYear);

            ResultSet rs = stmt.executeQuery();
            teacherId = null;
            Set<String> paperIds = new HashSet<>();
            Set<String> projectIds = new HashSet<>();
            Set<String> courseIds = new HashSet<>();
            String result = "找不到教师信息";
            while (rs.next()) {
                teacherId = rs.getString("teacher_id");
                String paperId = rs.getString("published_papers");
                String projectId = rs.getString("participated_projects");
                String courseId = rs.getString("selected_courses");

                paperIds.add(paperId);
                projectIds.add(projectId);
                courseIds.add(courseId);
            }
            if (teacherId != null) {
                Teacher teacher = new Teacher(teacherId);
                teacher.getTeacher();
                result = teacher + "\n";
                Iterator<String> paperIdsIterator = paperIds.iterator();
                while (paperIdsIterator.hasNext()) {
                    String paperId = paperIdsIterator.next();
                    if (paperId == null) continue;
                    Paper paper = new Paper(paperId);
                    paper.getPaper();
                    PublishedPaper publishedPaper = new PublishedPaper(teacherId, paperId);
                    publishedPaper.getPublishedPaper();
                    result += "发表论文: " + paper + ", " + publishedPaper + "\n";
                }
                Iterator<String> projectIdsIterator = projectIds.iterator();
                while (projectIdsIterator.hasNext()) {
                    String projectId = projectIdsIterator.next();
                    if (projectId == null) continue;
                    Project project = new Project(projectId);
                    project.getProject();
                    ParticipatedProject participatedProject = new ParticipatedProject(teacherId, projectId);
                    participatedProject.getParticipatedProject();
                    result += "承担项目: " + project + ", " + participatedProject + "\n";
                }
                Iterator<String> courseIdsIterator = courseIds.iterator();
                while (courseIdsIterator.hasNext()) {
                    String courseId = courseIdsIterator.next();
                    if (courseId == null) continue;
                    Course course = new Course(courseId);
                    course.getCourse();
                    SelectedCourse selectedCourse = new SelectedCourse(teacherId, courseId);
                    selectedCourse.getSelectedCourse();
                    result += "主讲课程: " + course + ", " + selectedCourse + "\n";
                }
            }
            return result;
        } catch (SQLException e) {
            return "数据库错误: " + e.getMessage();
        }
    }
}
