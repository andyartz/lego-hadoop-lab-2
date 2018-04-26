DROP TABLE IF EXISTS default.inventories;
CREATE EXTERNAL TABLE inventories (
    INVENTORY_ID INT,
    VERSION INT,
    SET_ID STRING
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
);

