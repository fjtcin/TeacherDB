DELIMITER //

CREATE TRIGGER BeforeInsertPublishedPaper
BEFORE INSERT ON PublishedPapers
FOR EACH ROW
BEGIN
    -- 检查论文是否已有通讯作者
    IF NEW.contact = 1 AND EXISTS (
        SELECT 1 FROM PublishedPapers
        WHERE paper_id = NEW.paper_id AND contact = 1
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '一篇论文只能有一位通讯作者';
    END IF;

    -- 检查论文作者排名是否重复
    IF EXISTS (
        SELECT 1 FROM PublishedPapers
        WHERE paper_id = NEW.paper_id AND ranking = NEW.ranking
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '论文的作者排名不能有重复';
    END IF;
END //

CREATE TRIGGER BeforeUpdatePublishedPaper
BEFORE UPDATE ON PublishedPapers
FOR EACH ROW
BEGIN
    -- 检查论文是否已有通讯作者
    IF NEW.contact = 1 AND EXISTS (
        SELECT 1 FROM PublishedPapers
        WHERE paper_id = NEW.paper_id AND contact = 1 AND teacher_id <> NEW.teacher_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '一篇论文只能有一位通讯作者';
    END IF;

    -- 检查论文作者排名是否重复
    IF EXISTS (
        SELECT 1 FROM PublishedPapers
        WHERE paper_id = NEW.paper_id AND ranking = NEW.ranking AND teacher_id <> NEW.teacher_id
    ) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = '论文的作者排名不能有重复';
    END IF;
END //

DELIMITER ;
