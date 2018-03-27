DROP TABLE IF EXISTS ${db_name}.table_gt3;

CREATE TABLE ${db_name}.table_gt3
COMMENT 'A table with ids greater than 3.'
AS
SELECT * FROM ${db_name}.test_table WHERE id > 3;