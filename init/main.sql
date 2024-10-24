DROP DATABASE IF EXISTS mydatabase;

CREATE DATABASE mydatabase;

use mydatabase;

CREATE TABLE Teachers (
    id CHAR(5) PRIMARY KEY CHECK (id REGEXP '^[0-9]{5}$'),
    name VARCHAR(256) NOT NULL,
    gender INT NOT NULL CHECK (gender = 1 OR gender = 2),
    title INT NOT NULL CHECK (title BETWEEN 1 AND 11)
);

CREATE TABLE Papers (
    id INT PRIMARY KEY CHECK (id >= 0),
    name VARCHAR(256) NOT NULL,
    source VARCHAR(256) NOT NULL,
    year YEAR NOT NULL,
    type INT NOT NULL CHECK (type BETWEEN 1 AND 4),
    level INT NOT NULL CHECK (level BETWEEN 1 AND 6)
);

CREATE TABLE Projects (
    id VARCHAR(256) PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    source VARCHAR(256) NOT NULL,
    type INT NOT NULL CHECK (type BETWEEN 1 AND 5),
    fund FLOAT NOT NULL CHECK (fund >= 0),
    start_year YEAR NOT NULL,
    end_year YEAR NOT NULL
);

CREATE TABLE Courses (
    id VARCHAR(256) PRIMARY KEY,
    name VARCHAR(256) NOT NULL,
    credit INT NOT NULL CHECK (credit >= 0),
    type INT NOT NULL CHECK (type = 1 OR type = 2)
);

CREATE TABLE PublishedPapers (
    teacher_id CHAR(5),
    paper_id INT,
    ranking INT NOT NULL CHECK (ranking >= 1),
    contact BOOL NOT NULL,
    CONSTRAINT PublishedPapers_PK PRIMARY KEY (teacher_id, paper_id),
    CONSTRAINT PublishedPapers_FK_teacher_id FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE,
    CONSTRAINT PublishedPapers_FK_paper_id FOREIGN KEY (paper_id) REFERENCES Papers(id) ON DELETE CASCADE
);

CREATE TABLE ParticipatedProjects (
    teacher_id CHAR(5),
    project_id VARCHAR(256),
    ranking INT NOT NULL CHECK (ranking >= 1),
    fund FLOAT NOT NULL CHECK (fund >= 0),
    CONSTRAINT ParticipatedProjects_PK PRIMARY KEY (teacher_id, project_id),
    CONSTRAINT ParticipatedProjects_FK_teacher_id FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE,
    CONSTRAINT ParticipatedProjects_FK_project_id FOREIGN KEY (project_id) REFERENCES Projects(id) ON DELETE CASCADE
);

CREATE TABLE SelectedCourses (
    teacher_id CHAR(5),
    course_id VARCHAR(256),
    year YEAR NOT NULL,
    semester INT NOT NULL CHECK (semester BETWEEN 1 AND 3),
    credit INT NOT NULL CHECK (credit >= 0),
    CONSTRAINT SelectedCourses_PK PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT SelectedCourses_FK_teacher_id FOREIGN KEY (teacher_id) REFERENCES Teachers(id) ON DELETE CASCADE,
    CONSTRAINT SelectedCourses_FK_course_id FOREIGN KEY (course_id) REFERENCES Courses(id) ON DELETE CASCADE
);
