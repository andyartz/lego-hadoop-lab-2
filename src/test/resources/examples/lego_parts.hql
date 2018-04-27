DROP TABLE IF EXISTS default.parts;
CREATE TABLE parts (
    part_id STRING,
    name STRING,
    part_cat_id INT
)
COMMENT "Contains lego parts data."
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
);
