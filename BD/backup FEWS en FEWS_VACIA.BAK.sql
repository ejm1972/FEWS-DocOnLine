BACKUP DATABASE [fews] TO  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA_3.1.0.BAK' 
WITH NOFORMAT, NOINIT,  NAME = N'fews-Completa Base de datos Copia de seguridad', SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO

BACKUP LOG [fews] TO  DISK = N'D:\FacturaElectronicaAfip\Java\DocOnLine\BD\FEWS_VACIA_3.1.0.LOG.BAK' 
WITH NOFORMAT, NOINIT,  NAME = N'fews-Completa Base de datos Copia de seguridad', SKIP, NOREWIND, NOUNLOAD,  STATS = 10
GO