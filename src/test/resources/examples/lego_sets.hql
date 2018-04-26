DROP TABLE IF EXISTS default.sets;
CREATE EXTERNAL TABLE sets (
    set_id STRING,
    name STRING,
    year INT,
    theme_id INT,
    num_parts INT
)
COMMENT 'Contains lego sets data.'
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
)
;
