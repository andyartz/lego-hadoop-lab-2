DROP TABLE IF EXISTS default.sets;
CREATE TABLE sets (
    SET_ID STRING,
    NAME STRING,
    YEAR INT,
    THEME_ID INT,
    NUM_PARTS INT
);
--ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
--WITH SERDEPROPERTIES
--(
--    "separatorChar" = ",",
--    "quoteChar"="\""
--);
