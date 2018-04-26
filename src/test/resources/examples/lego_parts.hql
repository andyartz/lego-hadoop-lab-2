DROP TABLE IF EXISTS default.parts;
CREATE TABLE parts (
    PART_ID STRING,
    NAME STRING,
    PART_CAT_ID INT
);
--ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
--WITH SERDEPROPERTIES
--(
--    "separatorChar" = ",",
--    "quoteChar"="\""
--);
