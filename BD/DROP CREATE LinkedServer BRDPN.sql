USE [master]
GO

/****** Object:  LinkedServer [BDRPN]    Script Date: 15/12/2020 21:35:00 ******/
EXEC master.dbo.sp_dropserver @server=N'BDRPN', @droplogins='droplogins'
GO

/****** Object:  LinkedServer [BDRPN]    Script Date: 15/12/2020 21:35:00 ******/
EXEC master.dbo.sp_addlinkedserver @server = N'BDRPN', @srvproduct=N'SQL Server'
 /* For security reasons the linked server remote logins password is changed with ######## */
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname=N'BDRPN',@useself=N'False',@locallogin=N'sa',@rmtuser=N'sa',@rmtpassword=N'AQsw142536'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'collation compatible', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'data access', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'dist', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'pub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'rpc', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'rpc out', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'sub', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'connect timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'collation name', @optvalue=null
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'lazy schema validation', @optvalue=N'false'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'query timeout', @optvalue=N'0'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'use remote collation', @optvalue=N'true'
GO

EXEC master.dbo.sp_serveroption @server=N'BDRPN', @optname=N'remote proc transaction promotion', @optvalue=N'true'
GO


