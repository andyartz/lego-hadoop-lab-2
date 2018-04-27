DROP TABLE IF EXISTS default.lego_parts_sets;
CREATE TABLE lego_parts_sets (
    part_id STRING,
    part_name STRING,
    set_id STRING,
    year INT,
    theme_id INT,
    num_parts INT,
    inventory_id INT,
    version INT,
    quantity INT,
    is_spare STRING,
    color_id INT
)
COMMENT 'Contains lego parts to sets data.'
STORED AS ORC;

INSERT INTO lego_parts_sets
select parts.part_id,
       parts.name part_name,
       sets.set_id,
       cast(sets.year as int),
       cast(sets.theme_id as int),
       cast(sets.num_parts as int),
       cast(inventories.inventory_id as int),
       cast(inventories.version as int),
       cast(inventory_parts.quantity as int),
       inventory_parts.is_spare,
       cast(inventory_parts.color_id as int)
from sets
join inventories on sets.set_id = inventories.set_id
join inventory_parts on inventory_parts.inventory_id = inventories.inventory_id
join parts on parts.part_id = inventory_parts.part_id;
