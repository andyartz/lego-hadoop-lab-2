DROP TABLE IF EXISTS default.inventory_parts;
CREATE EXTERNAL TABLE inventory_parts (
    inventory_id INT,
    part_id STRING,
    color_id INT,
    quantity INT,
    is_spare STRING
)
COMMENT "Contains lego inventory to parts data."
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES
(
    "separatorChar" = ",",
    "quoteChar"="\""
);

