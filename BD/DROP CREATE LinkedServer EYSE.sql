USE [master]
GO

/****** Object:  LinkedServer [EYSELIFIN]    Script Date: 15/12/2020 21:35:00 ******/
EXEC master.dbo.sp_dropserver @server=N'EYSELIFIN', @droplogins='droplogins'
GO

/****** Object:  LinkedServer [EYSELIFIN]    Script Date: 15/12/2020 21:35:00 ******/
EXEC master.dbo.sp_addlinkedserver @server = N'EYSELIFIN', @srvproduct=N'SQL Server'
 /* For security reasons the linked server remote logins password is changed with ######## */
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'EYSELIFIN',@useself=N'False',@locallogin=N'sa',@rmtuser=N'sa',@rmtpassword=N'AQsw142536'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'collation compatible', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'data access', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'dist', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'pub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'rpc', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'rpc out', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'sub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'connect timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'collation name', @optvalue=null
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'lazy schema validation', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'query timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'use remote collation', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'EYSELIFIN', @optname=N'remote proc transaction promotion', @optvalue=N'true'
GO


