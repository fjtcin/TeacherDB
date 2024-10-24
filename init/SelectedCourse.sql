DELIMITER //

CREATE TRIGGER BeforeInsertSelectedCourse
BEFORE INSERT ON SelectedCourses
FOR EACH ROW
BEGIN
    DECLARE total_credit INT;
    DECLARE course_credit INT;

    -- 检查学分总和是否小于等于课程总学分
    SELECT COALESCE(SUM(credit), 0) INTO total_credit
    FROM SelectedCourses
    WHERE course_id = NEW.course_id
      AND year = NEW.year
      AND semester = NEW.semester;

    SELECT credit INTO course_credit
    FROM Courses
    WHERE id = NEW.course_id;

    IF total_credit + NEW.credit > course_credit THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '所有教师的主讲学分总和不能超过课程总学分';
    END IF;
END //

CREATE TRIGGER BeforeUpdateSelectedCourse
BEFORE UPDATE ON SelectedCourses
FOR EACH ROW
BEGIN
    DECLARE total_credit INT;
    DECLARE course_credit INT;

    -- 检查学分总和是否小于等于课程总学分
    SELECT COALESCE(SUM(credit), 0) - OLD.credit INTO total_credit
    FROM SelectedCourses
    WHERE course_id = NEW.course_id
      AND year = NEW.year
      AND semester = NEW.semester;

    SELECT credit INTO course_credit
    FROM Courses
    WHERE id = NEW.course_id;

    IF total_credit + NEW.credit > course_credit THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '所有教师的主讲学分总和不能超过课程总学分';
    END IF;
END //

DELIMITER ;
