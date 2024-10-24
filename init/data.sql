INSERT INTO Teachers (id, name, gender, title) VALUE('43754', '夏斌', 2, 11);
INSERT INTO Teachers (id, name, gender, title) VALUE('70435', '刘丽丽', 1, 2);
INSERT INTO Teachers (id, name, gender, title) VALUE('42316', '杨勇', 2, 3);
INSERT INTO Teachers (id, name, gender, title) VALUE('93607', '周敏', 1, 6);
INSERT INTO Teachers (id, name, gender, title) VALUE('46450', '韩磊', 1, 10);

INSERT INTO Papers (id, name, source, year, type, level) VALUE('741314854', 'disintermediate visionary action-items', 'CSSCI', 2016, 4, 4);
INSERT INTO Papers (id, name, source, year, type, level) VALUE('718377720', 'architect one-to-one info-mediaries', 'SCI', 2020, 2, 3);
INSERT INTO Papers (id, name, source, year, type, level) VALUE('759471248', 'matrix world-class synergies', 'CSSCI', 2019, 3, 2);
INSERT INTO Papers (id, name, source, year, type, level) VALUE('40605251', 'optimize collaborative deliverables', 'EI', 2011, 4, 5);
INSERT INTO Papers (id, name, source, year, type, level) VALUE('526701218', 'empower customized ROI', 'SCI', 2016, 3, 1);

INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('K5708826', 'Triple-buffered attitude-oriented emulation', '973', 1, 62.17, 2011, 2017);
INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('U3560432', 'Progressive coherent initiative', 'EU', 5, 94.27, 2012, 2018);
INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('B4939840', 'Extended well-modulated system engine', 'EU', 2, 91.82, 2015, 2016);
INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('Y0792391', 'Synergistic 6thgeneration portal', 'UNDP', 5, 90.71, 2015, 2019);
INSERT INTO Projects (id, name, source, type, fund, start_year, end_year) VALUE('O3764596', 'Versatile scalable matrices', 'UNDP', 2, 62.76, 2015, 2017);

INSERT INTO Courses (id, name, credit, type) VALUE('KY963508', '作品日期介绍', 60, 2);
INSERT INTO Courses (id, name, credit, type) VALUE('NV078063', '最大您的关系公司免费', 20, 2);
INSERT INTO Courses (id, name, credit, type) VALUE('QY908542', '一起参加觉得日本不断', 20, 2);
INSERT INTO Courses (id, name, credit, type) VALUE('JD640016', '公司只有注册', 40, 1);
INSERT INTO Courses (id, name, credit, type) VALUE('KO734743', '销售作品', 10, 1);

INSERT INTO PublishedPapers (teacher_id, paper_id, ranking, contact) VALUE('43754', '741314854', 1, 1);
INSERT INTO PublishedPapers (teacher_id, paper_id, ranking, contact) VALUE('70435', '741314854', 2, 0);
INSERT INTO PublishedPapers (teacher_id, paper_id, ranking, contact) VALUE('70435', '718377720', 2, 0);
INSERT INTO PublishedPapers (teacher_id, paper_id, ranking, contact) VALUE('42316', '759471248', 3, 1);

INSERT INTO ParticipatedProjects (teacher_id, project_id, ranking, fund) VALUE('43754', 'K5708826', 1, 62.17);
INSERT INTO ParticipatedProjects (teacher_id, project_id, ranking, fund) VALUE('70435', 'U3560432', 2, 34.27);
INSERT INTO ParticipatedProjects (teacher_id, project_id, ranking, fund) VALUE('42316', 'B4939840', 2, 60);
INSERT INTO ParticipatedProjects (teacher_id, project_id, ranking, fund) VALUE('42316', 'Y0792391', 3, 60.71);

INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('43754', 'KY963508', 2016, 1, 60);
INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('70435', 'KY963508', 2017, 1, 60);
INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('70435', 'NV078063', 2016, 1, 10);
INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('42316', 'NV078063', 2016, 1, 10);
INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('42316', 'JD640016', 2016, 1, 20);
INSERT INTO SelectedCourses (teacher_id, course_id, year, semester, credit) VALUE('42316', 'KO734743', 2018, 2, 10);
