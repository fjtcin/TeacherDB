import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TextField txtTeacherId = new TextField();
        txtTeacherId.setPromptText("请输入教师工号");

        TextField txtStartYear = new TextField();
        txtStartYear.setPromptText("起始年份");
        txtStartYear.setPrefColumnCount(9);

        TextField txtEndYear = new TextField();
        txtEndYear.setPromptText("结束年份");
        txtEndYear.setPrefColumnCount(9);

        Button btnSummary = new Button("查询");
        Button btnExport = new Button("导出");
        TextArea txtSummary = new TextArea();
        txtSummary.setEditable(false);
        txtSummary.setWrapText(true);
        btnSummary.setOnAction(e -> {
            String teacherId = txtTeacherId.getText();
            String startYear = txtStartYear.getText();
            String endYear = txtEndYear.getText();
            String summary = DatabaseHandler.getTeacherWorkloadSummary(teacherId, startYear, endYear);
            txtSummary.setText(summary);
        });
        btnExport.setOnAction(e -> {
            String teacherId = txtTeacherId.getText();
            String startYear = txtStartYear.getText();
            String endYear = txtEndYear.getText();
            String summary = DatabaseHandler.getTeacherWorkloadSummary(teacherId, startYear, endYear);
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
            fileChooser.setInitialFileName("Untitled.txt");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file == null) return;
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(summary);
                txtSummary.setText("导出成功");
            } catch (IOException ex) {
                txtSummary.setText("导出失败: " + ex.getMessage());
            }
        });

        Button btnTeacher = new Button("教师管理");
        btnTeacher.setOnAction(e -> showTeacherScene());
        Button btnPaper = new Button("论文管理");
        btnPaper.setOnAction(e -> showPaperScene());
        Button btnProject = new Button("项目管理");
        btnProject.setOnAction(e -> showProjectScene());
        Button btnCourse = new Button("课程管理");
        btnCourse.setOnAction(e -> showCourseScene());
        Button btnPublishedPaper = new Button("发表论文管理");
        btnPublishedPaper.setOnAction(e -> showPublishedPaperScene());
        Button btnParticipatedProject = new Button("承担项目管理");
        btnParticipatedProject.setOnAction(e -> showParticipatedProjectScene());
        Button btnSelectedCourse = new Button("主讲课程管理");
        btnSelectedCourse.setOnAction(e -> showSelectedCourseScene());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        HBox entityBox = new HBox(20);
        entityBox.setAlignment(Pos.CENTER);
        entityBox.getChildren().addAll(btnTeacher, btnPaper, btnProject, btnCourse);

        HBox summaryBox = new HBox(10);
        summaryBox.getChildren().addAll(
                txtStartYear, txtEndYear,
                new Separator(),
                btnSummary, btnExport
        );

        HBox relationBox = new HBox(20);
        relationBox.setAlignment(Pos.CENTER);
        relationBox.getChildren().addAll(btnPublishedPaper, btnParticipatedProject, btnSelectedCourse);

        root.getChildren().addAll(
                entityBox,
                new Separator(),
                txtTeacherId,
                summaryBox,
                txtSummary,
                new Separator(),
                relationBox
        );

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("教学科研登记系统");
        primaryStage.getIcons().add(new Image("file:icons/main.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showTeacherScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label idLabel = new Label("教师工号: ");
        grid.add(idLabel, 0, 0);
        TextField idTextField = new TextField();
        grid.add(idTextField, 1, 0);

        Label nameLabel = new Label("教师名称: ");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label genderLabel = new Label("教师性别: ");
        grid.add(genderLabel, 0, 2);
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll(Teacher.GENDERS);
        grid.add(genderComboBox, 1, 2);

        Label titleLabel = new Label("教师职称: ");
        grid.add(titleLabel, 0, 3);
        ComboBox<String> titleComboBox = new ComboBox<>();
        titleComboBox.getItems().addAll(Teacher.TITLES);
        grid.add(titleComboBox, 1, 3);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        HBox hbBtn = new HBox(20);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton);
        grid.add(hbBtn, 0, 4, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 5, 2, 1);

        insertButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            int gender = genderComboBox.getSelectionModel().getSelectedIndex() + 1;
            int title = titleComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Teacher teacher = new Teacher(id, name, gender, title);
                teacher.insertTeacher();
                lblStatus.setText("添加成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            int gender = genderComboBox.getSelectionModel().getSelectedIndex() + 1;
            int title = titleComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Teacher teacher = new Teacher(id, name, gender, title);
                teacher.updateTeacher();
                lblStatus.setText("修改成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Teacher teacher = new Teacher(id);
                teacher.deleteTeacher();
                lblStatus.setText("删除成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Teacher teacher = new Teacher(id);
                teacher.getTeacher();
                nameTextField.setText(teacher.getName());
                genderComboBox.getSelectionModel().select(teacher.getGender() - 1);
                titleComboBox.getSelectionModel().select(teacher.getTitle() - 1);
                lblStatus.setText("查询成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage teacherStage = new Stage();
        teacherStage.setTitle("教师管理");
        teacherStage.getIcons().add(new Image("file:icons/teacher.png"));
        teacherStage.setScene(scene);
        teacherStage.show();
    }

    private void showPaperScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label idLabel = new Label("论文序号: ");
        grid.add(idLabel, 0, 0);
        TextField idTextField = new TextField();
        grid.add(idTextField, 1, 0);

        Label nameLabel = new Label("论文名称: ");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        nameTextField.setPrefColumnCount(30);
        grid.add(nameTextField, 1, 1);

        Label sourceLabel = new Label("发表来源: ");
        grid.add(sourceLabel, 0, 2);
        TextField sourceTextField = new TextField();
        grid.add(sourceTextField, 1, 2);

        Label yearLabel = new Label("发表年份: ");
        grid.add(yearLabel, 0, 3);
        TextField yearTextField = new TextField();
        grid.add(yearTextField, 1, 3);

        Label typeLabel = new Label("论文类型: ");
        grid.add(typeLabel, 0, 4);
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Paper.TYPES);
        grid.add(typeComboBox, 1, 4);

        Label levelLabel = new Label("论文级别: ");
        grid.add(levelLabel, 0, 5);
        ComboBox<String> levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll(Paper.LEVELS);
        grid.add(levelComboBox, 1, 5);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        HBox hbBtn = new HBox(20);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton);
        grid.add(hbBtn, 0, 6, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 7, 2, 1);

        insertButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String source = sourceTextField.getText();
            String year = yearTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            int level = levelComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Paper paper = new Paper(id, name, source, year, type, level);
                paper.insertPaper();
                lblStatus.setText("添加成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String source = sourceTextField.getText();
            String year = yearTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            int level = levelComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Paper paper = new Paper(id, name, source, year, type, level);
                paper.updatePaper();
                lblStatus.setText("修改成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Paper paper = new Paper(id);
                paper.deletePaper();
                lblStatus.setText("删除成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Paper paper = new Paper(id);
                paper.getPaper();
                nameTextField.setText(paper.getName());
                sourceTextField.setText(paper.getSource());
                yearTextField.setText(String.valueOf(paper.getYear()));
                typeComboBox.getSelectionModel().select(paper.getType() - 1);
                levelComboBox.getSelectionModel().select(paper.getLevel() - 1);
                lblStatus.setText("查询成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage paperStage = new Stage();
        paperStage.setTitle("论文管理");
        paperStage.setScene(scene);
        paperStage.getIcons().add(new Image("file:icons/paper.png"));
        paperStage.show();
    }

    private void showProjectScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label idLabel = new Label("项目编号: ");
        grid.add(idLabel, 0, 0);
        TextField idTextField = new TextField();
        grid.add(idTextField, 1, 0);

        Label nameLabel = new Label("项目名称: ");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        nameTextField.setPrefColumnCount(30);
        grid.add(nameTextField, 1, 1);

        Label sourceLabel = new Label("项目来源: ");
        grid.add(sourceLabel, 0, 2);
        TextField sourceTextField = new TextField();
        grid.add(sourceTextField, 1, 2);

        Label typeLabel = new Label("项目类型: ");
        grid.add(typeLabel, 0, 3);
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Project.TYPES);
        grid.add(typeComboBox, 1, 3);

        Label fundLabel = new Label("项目经费: ");
        grid.add(fundLabel, 0, 4);
        TextField fundTextField = new TextField();
        grid.add(fundTextField, 1, 4);

        Label startYearLabel = new Label("开始年份: ");
        grid.add(startYearLabel, 0, 5);
        TextField startYearTextField = new TextField();
        grid.add(startYearTextField, 1, 5);

        Label endYearLabel = new Label("结束年份: ");
        grid.add(endYearLabel, 0, 6);
        TextField endYearTextField = new TextField();
        grid.add(endYearTextField, 1, 6);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        HBox hbBtn = new HBox(20);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton);
        grid.add(hbBtn, 0, 7, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 8, 2, 1);

        insertButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String source = sourceTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            String fund = fundTextField.getText();
            String startYear = startYearTextField.getText();
            String endYear = endYearTextField.getText();
            try {
                Project project = new Project(id, name, source, type, fund, startYear, endYear);
                project.insertProject();
                lblStatus.setText("添加成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String source = sourceTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            String fund = fundTextField.getText();
            String startYear = startYearTextField.getText();
            String endYear = endYearTextField.getText();
            try {
                Project project = new Project(id, name, source, type, fund, startYear, endYear);
                project.updateProject();
                lblStatus.setText("修改成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Project project = new Project(id);
                project.deleteProject();
                lblStatus.setText("删除成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Project project = new Project(id);
                project.getProject();
                nameTextField.setText(project.getName());
                sourceTextField.setText(project.getSource());
                typeComboBox.getSelectionModel().select(project.getType() - 1);
                fundTextField.setText(String.valueOf(project.getFund()));
                startYearTextField.setText(String.valueOf(project.getStartYear()));
                endYearTextField.setText(String.valueOf(project.getEndYear()));
                lblStatus.setText("查询成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage projectStage = new Stage();
        projectStage.setTitle("项目管理");
        projectStage.getIcons().add(new Image("file:icons/project.png"));
        projectStage.setScene(scene);
        projectStage.show();
    }

    private void showCourseScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label idLabel = new Label("课程编号: ");
        grid.add(idLabel, 0, 0);
        TextField idTextField = new TextField();
        grid.add(idTextField, 1, 0);

        Label nameLabel = new Label("课程名称: ");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        Label creditLabel = new Label("课程学时: ");
        grid.add(creditLabel, 0, 2);
        TextField creditTextField = new TextField();
        grid.add(creditTextField, 1, 2);

        Label typeLabel = new Label("课程性质: ");
        grid.add(typeLabel, 0, 3);
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Course.TYPES);
        grid.add(typeComboBox, 1, 3);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        HBox hbBtn = new HBox(20);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton);
        grid.add(hbBtn, 0, 4, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 5, 2, 1);

        insertButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String credit = creditTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Course course = new Course(id, name, credit, type);
                course.insertCourse();
                lblStatus.setText("添加成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String id = idTextField.getText();
            String name = nameTextField.getText();
            String credit = creditTextField.getText();
            int type = typeComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                Course course = new Course(id, name, credit, type);
                course.updateCourse();
                lblStatus.setText("修改成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Course course = new Course(id);
                course.deleteCourse();
                lblStatus.setText("删除成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String id = idTextField.getText();
            try {
                Course course = new Course(id);
                course.getCourse();
                nameTextField.setText(course.getName());
                creditTextField.setText(String.valueOf(course.getCredit()));
                typeComboBox.getSelectionModel().select(course.getType() - 1);
                lblStatus.setText("查询成功: id=" + id);
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage courseStage = new Stage();
        courseStage.setTitle("课程管理");
        courseStage.getIcons().add(new Image("file:icons/course.png"));
        courseStage.setScene(scene);
        courseStage.show();
    }

    private void showPublishedPaperScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label teacherIdLabel = new Label("教师工号: ");
        grid.add(teacherIdLabel, 0, 0);
        TextField teacherIdTextField = new TextField();
        grid.add(teacherIdTextField, 1, 0);

        Label paperIdLabel = new Label("论文序号: ");
        grid.add(paperIdLabel, 0, 1);
        TextField paperIdTextField = new TextField();
        grid.add(paperIdTextField, 1, 1);

        Label rankingLabel = new Label("教师排名: ");
        grid.add(rankingLabel, 0, 2);
        TextField rankingTextField = new TextField();
        grid.add(rankingTextField, 1, 2);

        CheckBox chkContact = new CheckBox("通讯作者");
        grid.add(chkContact, 0, 3, 2, 1);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        HBox hbBtn = new HBox(20);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton);
        grid.add(hbBtn, 0, 4, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 5, 2, 1);

        insertButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String paperId = paperIdTextField.getText();
            String ranking = rankingTextField.getText();
            Boolean contact = chkContact.isSelected();
            try {
                PublishedPaper publishedPaper = new PublishedPaper(teacherId, paperId, ranking, contact);
                publishedPaper.insertPublishedPaper();
                lblStatus.setText("添加成功: id=(" + teacherId + "," + paperId + ")");
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String paperId = paperIdTextField.getText();
            String ranking = rankingTextField.getText();
            Boolean contact = chkContact.isSelected();
            try {
                PublishedPaper publishedPaper = new PublishedPaper(teacherId, paperId, ranking, contact);
                publishedPaper.updatePublishedPaper();
                lblStatus.setText("修改成功: id=(" + teacherId + "," + paperId + ")");
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String paperId = paperIdTextField.getText();
            try {
                PublishedPaper publishedPaper = new PublishedPaper(teacherId, paperId);
                publishedPaper.deletePublishedPaper();
                lblStatus.setText("删除成功: id=(" + teacherId + "," + paperId + ")");
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String paperId = paperIdTextField.getText();
            try {
                PublishedPaper publishedPaper = new PublishedPaper(teacherId, paperId);
                publishedPaper.getPublishedPaper();
                rankingTextField.setText(String.valueOf(publishedPaper.getRanking()));
                chkContact.setSelected(publishedPaper.isContact());
                lblStatus.setText("查询成功: id=(" + teacherId + "," + paperId + ")");
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage publishedPaperStage = new Stage();
        publishedPaperStage.setTitle("发表论文管理");
        publishedPaperStage.getIcons().add(new Image("file:icons/paper.png"));
        publishedPaperStage.setScene(scene);
        publishedPaperStage.show();
    }

    private void showParticipatedProjectScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label teacherIdLabel = new Label("教师工号: ");
        grid.add(teacherIdLabel, 0, 0);
        TextField teacherIdTextField = new TextField();
        grid.add(teacherIdTextField, 1, 0);

        Label projectIDLabel = new Label("项目编号: ");
        grid.add(projectIDLabel, 0, 1);
        TextField projectIdTextField = new TextField();
        grid.add(projectIdTextField, 1, 1);

        Label rankingLabel = new Label("教师排名: ");
        grid.add(rankingLabel, 0, 2);
        TextField rankingTextField = new TextField();
        grid.add(rankingTextField, 1, 2);

        Label fundLabel = new Label("承担经费: ");
        grid.add(fundLabel, 0, 3);
        TextField fundTextField = new TextField();
        grid.add(fundTextField, 1, 3);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        Button checkButton = new Button("检查");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton, checkButton);
        grid.add(hbBtn, 0, 4, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 5, 2, 1);

        insertButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String projectId = projectIdTextField.getText();
            String ranking = rankingTextField.getText();
            String fund = fundTextField.getText();
            try {
                ParticipatedProject participatedProject = new ParticipatedProject(teacherId, projectId, ranking, fund);
                participatedProject.insertParticipatedProject();
                lblStatus.setText("添加成功: id=(" + teacherId + "," + projectId + ")");
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String projectId = projectIdTextField.getText();
            String ranking = rankingTextField.getText();
            String fund = fundTextField.getText();
            try {
                ParticipatedProject participatedProject = new ParticipatedProject(teacherId, projectId, ranking, fund);
                participatedProject.updateParticipatedProject();
                lblStatus.setText("修改成功: id=(" + teacherId + "," + projectId + ")");
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String projectId = projectIdTextField.getText();
            try {
                ParticipatedProject participatedProject = new ParticipatedProject(teacherId, projectId);
                participatedProject.deleteParticipatedProject();
                lblStatus.setText("删除成功: id=(" + teacherId + "," + projectId + ")");
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String projectId = projectIdTextField.getText();
            try {
                ParticipatedProject participatedProject = new ParticipatedProject(teacherId, projectId);
                participatedProject.getParticipatedProject();
                rankingTextField.setText(String.valueOf(participatedProject.getRanking()));
                fundTextField.setText(String.valueOf(participatedProject.getFund()));
                lblStatus.setText("查询成功: id=(" + teacherId + "," + projectId + ")");
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        checkButton.setOnAction(event -> {
            String projectId = projectIdTextField.getText();
            try {
                float diff = ParticipatedProject.checkProjectFund(projectId);
                if (Math.abs(diff) < 1e-2) {
                    lblStatus.setText("检查成功: id=" + projectId + ", 项目经费无盈余");
                } else {
                    lblStatus.setText("检查成功: id=" + projectId + ", 项目经费盈余: " + String.format("%.2f", diff));
                }
            } catch (Exception e) {
                lblStatus.setText("检查失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage participatedProjectStage = new Stage();
        participatedProjectStage.setTitle("承担项目管理");
        participatedProjectStage.getIcons().add(new Image("file:icons/project.png"));
        participatedProjectStage.setScene(scene);
        participatedProjectStage.show();
    }

    private void showSelectedCourseScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        Label teacherIdLabel = new Label("教师工号: ");
        grid.add(teacherIdLabel, 0, 0);
        TextField teacherIdTextField = new TextField();
        grid.add(teacherIdTextField, 1, 0);

        Label courseIdLabel = new Label("课程编号: ");
        grid.add(courseIdLabel, 0, 1);
        TextField courseIdTextField = new TextField();
        grid.add(courseIdTextField, 1, 1);

        Label yearLabel = new Label("上课年份: ");
        grid.add(yearLabel, 0, 2);
        TextField yearTextField = new TextField();
        grid.add(yearTextField, 1, 2);

        Label semesterLabel = new Label("上课学期: ");
        grid.add(semesterLabel, 0, 3);
        ComboBox<String> semesterComboBox = new ComboBox<>();
        semesterComboBox.getItems().addAll(SelectedCourse.SEMESTERS);
        grid.add(semesterComboBox, 1, 3);

        Label creditLabel = new Label("承担学时: ");
        grid.add(creditLabel, 0, 4);
        TextField creditTextField = new TextField();
        grid.add(creditTextField, 1, 4);

        Button insertButton = new Button("添加");
        Button updateButton = new Button("修改");
        Button deleteButton = new Button("删除");
        Button queryButton = new Button("查询");
        Button checkButton = new Button("检查");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().addAll(insertButton, updateButton, deleteButton, queryButton, checkButton);
        grid.add(hbBtn, 0, 5, 2, 1);

        Label lblStatus = new Label("就绪");
        lblStatus.widthProperty().addListener((obs, oldWidth, newWidth) -> updateTooltip(lblStatus));
        lblStatus.textProperty().addListener((obs, oldText, newText) -> updateTooltip(lblStatus));
        HBox hblbl = new HBox();
        hblbl.setAlignment(Pos.CENTER_RIGHT);
        hblbl.getChildren().add(lblStatus);
        grid.add(hblbl, 0, 6, 2, 1);

        insertButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String courseId = courseIdTextField.getText();
            String year = yearTextField.getText();
            int semester = semesterComboBox.getSelectionModel().getSelectedIndex() + 1;
            String credit = creditTextField.getText();
            try {
                SelectedCourse selectedCourse = new SelectedCourse(teacherId, courseId, year, semester, credit);
                selectedCourse.insertSelectedCourse();
                lblStatus.setText("添加成功: id=(" + teacherId + "," + courseId + ")");
            } catch (Exception e) {
                lblStatus.setText("添加失败: " + e.getMessage());
            }
        });

        updateButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String courseId = courseIdTextField.getText();
            String year = yearTextField.getText();
            int semester = semesterComboBox.getSelectionModel().getSelectedIndex() + 1;
            String credit = creditTextField.getText();
            try {
                SelectedCourse selectedCourse = new SelectedCourse(teacherId, courseId, year, semester, credit);
                selectedCourse.updateSelectedCourse();
                lblStatus.setText("修改成功: id=(" + teacherId + "," + courseId + ")");
            } catch (Exception e) {
                lblStatus.setText("修改失败: " + e.getMessage());
            }
        });

        deleteButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String courseId = courseIdTextField.getText();
            try {
                SelectedCourse selectedCourse = new SelectedCourse(teacherId, courseId);
                selectedCourse.deleteSelectedCourse();
                lblStatus.setText("删除成功: id=(" + teacherId + "," + courseId + ")");
            } catch (Exception e) {
                lblStatus.setText("删除失败: " + e.getMessage());
            }
        });

        queryButton.setOnAction(event -> {
            String teacherId = teacherIdTextField.getText();
            String courseId = courseIdTextField.getText();
            try {
                SelectedCourse selectedCourse = new SelectedCourse(teacherId, courseId);
                selectedCourse.getSelectedCourse();
                yearTextField.setText(String.valueOf(selectedCourse.getYear()));
                semesterComboBox.getSelectionModel().select(selectedCourse.getSemester() - 1);
                creditTextField.setText(String.valueOf(selectedCourse.getCredit()));
                lblStatus.setText("查询成功: id=(" + teacherId + "," + courseId + ")");
            } catch (Exception e) {
                lblStatus.setText("查询失败: " + e.getMessage());
            }
        });

        checkButton.setOnAction(event -> {
            String courseId = courseIdTextField.getText();
            String year = yearTextField.getText();
            int semester = semesterComboBox.getSelectionModel().getSelectedIndex() + 1;
            try {
                int diff = SelectedCourse.checkCourseCredit(courseId, year, semester);
                if (diff == 0) {
                    lblStatus.setText("检查成功: id=" + courseId + ", 学时无盈余");
                } else {
                    lblStatus.setText("检查成功: id=" + courseId + ", 学时盈余: " + diff);
                }
            } catch (Exception e) {
                lblStatus.setText("检查失败: " + e.getMessage());
            }
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        Stage selectedCourseStage = new Stage();
        selectedCourseStage.setTitle("主讲课程管理");
        selectedCourseStage.getIcons().add(new Image("file:icons/course.png"));
        selectedCourseStage.setScene(scene);
        selectedCourseStage.show();
    }

    private void updateTooltip(Label label) {
        Text text = new Text(label.getText());
        double textWidth = text.getLayoutBounds().getWidth();

        if (textWidth > label.getWidth()) {
            if (label.getTooltip() == null) {
                label.setTooltip(new Tooltip(label.getText()));
            }
        } else {
            label.setTooltip(null);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
