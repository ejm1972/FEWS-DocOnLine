EXEC master.dbo.sp_dropserver @server=N'afgpm-finnegans', @droplogins='droplogins'
GO
EXEC master.dbo.sp_addlinkedserver @server = N'afgpm-finnegans',
    @provider = N'SQLNCLI',
    @srvproduct = '',
	@provstr = N'SERVER=[afgpm-finnegans];User ID=sa',
    @datasrc=N'tcp:localhost'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'afgpm-finnegans',
    @locallogin = NULL,
    @useself = N'False',
    @rmtuser = N'sa',
    @rmtpassword = N'Admin.1-'
GO

EXEC master.dbo.sp_dropserver @server=N'EYSELIFIN', @droplogins='droplogins'
GO
EXEC master.dbo.sp_addlinkedserver @server = N'EYSELIFIN',
    @provider = N'SQLNCLI',
    @srvproduct = '',
	@provstr = N'SERVER=EYSELIFIN;User ID=sa',
    @datasrc=N'tcp:localhost'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'EYSELIFIN',
    @locallogin = NULL,
    @useself = N'False',
    @rmtuser = N'sa',
    @rmtpassword = N'Admin.1-'
GO

EXEC master.dbo.sp_dropserver @server=N'CLPRI', @droplogins='droplogins'
GO
EXEC master.dbo.sp_addlinkedserver @server = N'CLPRI',
    @provider = N'SQLNCLI',
    @srvproduct = '',
	@provstr = N'SERVER=CLPRI;User ID=sa',
    @datasrc=N'tcp:localhost'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'CLPRI',
    @locallogin = NULL,
    @useself = N'False',
    @rmtuser = N'sa',
    @rmtpassword = N'Admin.1-'
GO

EXEC master.dbo.sp_dropserver @server=N'LQ5\LQ5', @droplogins='droplogins'
GO
EXEC master.dbo.sp_addlinkedserver @server = N'LQ5\LQ5',
    @provider = N'SQLNCLI',
    @srvproduct = '',
	@provstr = N'SERVER=LQ5\LQ5;User ID=sa',
    @datasrc=N'tcp:localhost'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'LQ5\LQ5',
    @locallogin = NULL,
    @useself = N'False',
    @rmtuser = N'sa',
    @rmtpassword = N'Admin.1-'
GO

EXEC master.dbo.sp_dropserver @server=N'DRGFINNEGANS', @droplogins='droplogins'
GO
EXEC master.dbo.sp_addlinkedserver @server = N'DRGFINNEGANS',
    @provider = N'SQLNCLI',
    @srvproduct = '',
	@provstr = N'SERVER=DRGFINNEGANS;User ID=sa',
    @datasrc=N'tcp:localhost'
EXEC master.dbo.sp_addlinkedsrvlogin @rmtsrvname = N'DRGFINNEGANS',
    @locallogin = NULL,
    @useself = N'False',
    @rmtuser = N'sa',
    @rmtpassword = N'Admin.1-'
GO
