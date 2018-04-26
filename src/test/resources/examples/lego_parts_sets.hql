DROP TABLE IF EXISTS default.lego_parts_sets;
CREATE TABLE lego_parts_sets (
    PART_ID STRING,
    PART_NAME STRING,
    SET_ID STRING,
    YEAR INT,
    THEME_ID INT,
    NUM_PARTS INT,
    INVENTORY_ID INT,
    VERSION INT,
    QUANTITY INT,
    IS_SPARE STRING,
    COLOR_ID INT
)
STORED AS ORC;

INSERT INTO lego_parts_sets
select parts.PART_ID, parts.name PART_NAME, sets.SET_ID, sets.YEAR, sets.THEME_ID, sets.NUM_PARTS, inventories.INVENTORY_ID, inventories.VERSION, inventory_parts.QUANTITY, inventory_parts.IS_SPARE, inventory_parts.COLOR_ID
from sets
join inventories on sets.set_id = inventories.set_id
join inventory_parts on inventory_parts.inventory_id = inventories.inventory_id
join parts on parts.part_id = inventory_parts.part_id;
