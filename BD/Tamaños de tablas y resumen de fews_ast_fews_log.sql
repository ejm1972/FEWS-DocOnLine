SET NOCOUNT ON 
DBCC UPDATEUSAGE(0) 

-- Table row counts and sizes.

DECLARE @sizes TABLE
(
    [name] NVARCHAR(128),
    [rows] CHAR(11),
    reserved VARCHAR(18),
    data VARCHAR(18),
    index_size VARCHAR(18),
    unused VARCHAR(18)
)

INSERT @sizes EXEC sp_msForEachTable 'EXEC sp_spaceused ''?''' 

SELECT *
FROM   @sizes
ORDER BY convert(int, substring(data, 1, len(data)-3)) desc


select year(creado), MONTH(creado), day(creado), COUNT(*)
from FEWS_AST_FEWS_LOG
group by year(creado), MONTH(creado), day(creado)
order by year(creado), MONTH(creado), day(creado)


