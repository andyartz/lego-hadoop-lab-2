DROP TABLE IF EXISTS default.large_sales;
CREATE TABLE large_sales
COMMENT 'Contains only large online transactions over $100 total.'
AS
SELECT
  tran_id,
  CASE chan_c
    WHEN "S" THEN "Store"
    WHEN "O" THEN "Online"
  END sell_chan,
  total
FROM sales
WHERE total >= 100;
