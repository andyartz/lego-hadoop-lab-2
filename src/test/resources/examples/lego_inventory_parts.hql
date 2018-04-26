DROP TABLE IF EXISTS default.inventory_parts;
CREATE EXTERNAL TABLE inventory_parts (
    INVENTORY_ID INT,
    PART_ID STRING,
    COLOR_ID INT,
    QUANTITY INT,
    IS_SPARE STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
);

