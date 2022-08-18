BACKUP DATABASE [fews] TO  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA.BAK' 
WITH NOFORMAT, NOINIT,  NAME = N'fews-Completa Base de datos Copia de seguridad', 
SKIP, 
NOREWIND, 
NOUNLOAD,  
STATS = 10
GO

BACKUP LOG [fews] TO  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA.LOG.BAK' 
WITH NOFORMAT, NOINIT,  NAME = N'fews-Completa Base de datos Copia de seguridad', SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO

USE [master]
RESTORE DATABASE [fews_vacia] FROM  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA_3.1.0.BAK' 
WITH  FILE = 1,  
MOVE N'fews' TO N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\fews_vacia.mdf',  
MOVE N'fews_log_1' TO N'c:\Program Files\Microsoft SQL Server\MSSQL10_50.MSSQLSERVER\MSSQL\DATA\fews_vacia_0.ldf',  
NOUNLOAD,  
STATS = 5
GO

BACKUP DATABASE [fews_vacia] TO  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA_3.1.0.BAK' 
WITH NOFORMAT, 
NOINIT,  
NAME = N'fews_vacia-Completa Base de datos Copia de seguridad', 
SKIP, 
NOREWIND, 
NOUNLOAD,  
STATS = 10
GO