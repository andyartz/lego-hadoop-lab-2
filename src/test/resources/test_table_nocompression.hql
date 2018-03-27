set mapreduce.output.fileoutputformat.compress=true;
set mapreduce.output.fileoutputformat.compress.type = BLOCK;
set hive.exec.compress.output = true;
set mapreduce.output.fileoutputformat.compress = true;
set hive.merge.mapredfiles = true;
set hive.exec.reducers.bytes.per.reducer=10000000000;
set hive.merge.smallfiles.avgsize = 512000000;

DROP TABLE IF EXISTS default.test_table;

CREATE TABLE default.test_table
(
    id INT COMMENT 'id comment',
    col1 STRING COMMENT 'col1 comment',
    col2 TIMESTAMP COMMENT 'col2 comment'
)
STORED AS ORC;

INSERT INTO default.test_table VALUES
    (0, "record 1", "2017-01-01 00:00:01"),
    (1, "record 2", "2017-01-02 00:00:02");