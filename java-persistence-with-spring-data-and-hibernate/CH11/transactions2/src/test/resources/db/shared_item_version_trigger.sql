-- Database-level versioning for a shared database (MySQL).
--
-- When a legacy application runs plain SQL such as:
--   UPDATE SHARED_ITEM SET name = 'x' WHERE id = 1
-- it does not increment the version column. Hibernate optimistic locking then
-- fails to detect that the row changed.
--
-- This trigger runs before every UPDATE. If the incoming row did not advance
-- version (typical of non-Hibernate clients), the database increments it.
-- Hibernate updates already set version = oldVersion + 1, so they are left alone.

-- Run DROP separately in Java/tests; mysql client users can uncomment the line below.
-- DROP TRIGGER IF EXISTS shared_item_version_trigger;

CREATE TRIGGER shared_item_version_trigger
BEFORE UPDATE ON SHARED_ITEM
FOR EACH ROW
BEGIN
    IF NEW.version <= OLD.version THEN
        SET NEW.version = OLD.version + 1;
    END IF;
END
