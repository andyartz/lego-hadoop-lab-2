DROP TABLE IF EXISTS ${db_name}.test_table;

CREATE TABLE ${db_name}.test_table
(
    id INT COMMENT 'id comment',
    col1 STRING COMMENT 'col1 comment',
    col2 TIMESTAMP COMMENT 'col2 comment'
);