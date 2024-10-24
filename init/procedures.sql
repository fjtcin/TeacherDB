DELIMITER //

CREATE PROCEDURE CheckProjectFund(
    IN p_project_id VARCHAR(256),
    OUT total_fund FLOAT,
    OUT project_fund FLOAT
)
BEGIN
    SELECT COALESCE(SUM(fund), 0) INTO total_fund
    FROM ParticipatedProjects
    WHERE project_id = p_project_id;

    SELECT fund INTO project_fund
    FROM Projects
    WHERE id = p_project_id;

    IF project_fund IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '找不到经费信息';
    END IF;
END //

CREATE PROCEDURE CheckCourseCredit(
    IN p_course_id VARCHAR(256),
    IN p_year INT,  -- Must be INT type, not YEAR type
    IN p_semester INT,
    OUT total_credit INT,
    OUT course_credit INT
)
BEGIN
    SELECT COALESCE(SUM(credit), 0) INTO total_credit
    FROM SelectedCourses
    WHERE course_id = p_course_id
      AND year = p_year
      AND semester = p_semester;

    SELECT credit INTO course_credit
    FROM Courses
    WHERE id = p_course_id;

    IF course_credit IS NULL THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '找不到学时信息';
    END IF;
END //

CREATE PROCEDURE GetTeacherWorkloadSummary(
    IN p_teacher_id CHAR(5),
    IN p_start_year INT,
    IN p_end_year INT
)
BEGIN
    SELECT
        t.id AS teacher_id,
        p.id AS published_papers,
        pr.id AS participated_projects,
        c.id AS selected_courses
    FROM
        Teachers t
        LEFT JOIN PublishedPapers pp ON t.id = pp.teacher_id
        LEFT JOIN Papers p ON pp.paper_id = p.id AND p.year BETWEEN p_start_year AND p_end_year
        LEFT JOIN ParticipatedProjects ppr ON t.id = ppr.teacher_id
        LEFT JOIN Projects pr ON ppr.project_id = pr.id AND pr.start_year <= p_end_year AND pr.end_year >= p_start_year
        LEFT JOIN SelectedCourses sc ON t.id = sc.teacher_id AND sc.year BETWEEN p_start_year AND p_end_year
        LEFT JOIN Courses c ON sc.course_id = c.id
    WHERE
        t.id = p_teacher_id
    GROUP BY
        t.id, p.id, pr.id, c.id;
END //

DELIMITER ;
