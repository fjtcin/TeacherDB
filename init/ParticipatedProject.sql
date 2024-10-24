DELIMITER //

CREATE TRIGGER BeforeInsertParticipatedProject
BEFORE INSERT ON ParticipatedProjects
FOR EACH ROW
BEGIN
    DECLARE total_fund FLOAT;
    DECLARE project_fund FLOAT;

    -- 检查排名是否重复
    IF EXISTS (
        SELECT 1 FROM ParticipatedProjects
        WHERE project_id = NEW.project_id AND ranking = NEW.ranking
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '项目排名不能有重复';
    END IF;

    -- 检查经费总和是否不超过项目总经费
    SELECT COALESCE(SUM(fund), 0) INTO total_fund
    FROM ParticipatedProjects
    WHERE project_id = NEW.project_id;

    SELECT fund INTO project_fund
    FROM Projects
    WHERE id = NEW.project_id;

    IF total_fund + NEW.fund > project_fund THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '所有教师的承担经费总和不能超过项目总经费';
    END IF;
END //

CREATE TRIGGER BeforeUpdateParticipatedProject
BEFORE UPDATE ON ParticipatedProjects
FOR EACH ROW
BEGIN
    DECLARE total_fund FLOAT;
    DECLARE project_fund FLOAT;

    -- 检查排名是否重复
    IF EXISTS (
        SELECT 1 FROM ParticipatedProjects
        WHERE project_id = NEW.project_id AND ranking = NEW.ranking AND teacher_id <> OLD.teacher_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '项目排名不能有重复';
    END IF;

    -- 检查经费总和是否不超过项目总经费
    SELECT COALESCE(SUM(fund), 0) - OLD.fund INTO total_fund
    FROM ParticipatedProjects
    WHERE project_id = NEW.project_id;

    SELECT fund INTO project_fund
    FROM Projects
    WHERE id = NEW.project_id;

    IF total_fund + NEW.fund > project_fund THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '所有教师的承担经费总和不能超过项目总经费';
    END IF;
END //

DELIMITER ;
