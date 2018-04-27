DROP TABLE IF EXISTS default.inventories;
CREATE EXTERNAL TABLE inventories (
    inventory_id INT,
    version INT,
    set_id STRING
)
COMMENT 'Contains lego inventories data.'
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
);

