use DocOnline
go

insert into DBO.usuario(id_rol, usuario, [password], ultimo_acceso, activo, bloqueado, reintento, administrable, apellido, nombre, mail, cambia_clave, version_ui, visible, theme) values (1, 'ADMIN', '8201ca06098dacd45b157ed4a9889c22f2f39ba6', NULL, 1, 0, 0, 1,'Administrador2', 'Administrador', '', 0, 1, 1, null)
insert into DBO.usuario(id_rol, usuario, [password], ultimo_acceso, activo, bloqueado, reintento, administrable, apellido, nombre, mail, cambia_clave, version_ui, visible, theme) values (1, 'J', '5c2dd944dde9e08881bef0894fe7b22a5c9c4b06', NULL, 1, 0, 0, 1,'', '', '', 0, 1, 1, null)
insert into DBO.item ([id],[id_seccion],[descripcion],[orden],[url],[icon],[codigo]) values (1, 1, 'Administración',1,'/secure/usuarios/administracion.xhtml','admin-icon-blue','SI_ADM_USR')
insert into DBO.item ([id],[id_seccion],[descripcion],[orden],[url],[icon],[codigo]) values (2, 3, 'Ajuste Manual',1,'/secure/ajustes/ajusteManual.xhtml', 'ui-icon-wrench', 'SI_CUE_AJU')
insert into DBO.item ([id],[id_seccion],[descripcion],[orden],[url],[icon],[codigo]) values (3, 2, 'Clientes',1,'/secure/clientes/clientes.xhtml','ui-icon-person','SI_CLI_ABM')
insert into DBO.item ([id],[id_seccion],[descripcion],[orden],[url],[icon],[codigo]) values (4, 4, 'Configuración',1,'/secure/configuracion/configuracion.xhtml','ui-icon-contact','SI_CFG_MOD')
insert into DBO.rol (id, descripcion, codigo, visible) values (1,'Administrator', 'ROLE_ADMIN',1)
insert into DBO.item_rol (id, id_rol, id_item) values (1,1,1)
insert into DBO.item_rol (id, id_rol, id_item) values (2,1,2)
insert into DBO.item_rol (id, id_rol, id_item) values (3,1,3)
insert into DBO.item_rol (id, id_rol, id_item) values (4,1,4)
insert into DBO.seccion(id, descripcion, orden, codigo, url) values (1,'Usuarios',1, 'SEC_USR', '')
insert into DBO.seccion(id, descripcion, orden, codigo, url) values (2,'Clientes',4, 'SEC_CLI', '')
insert into DBO.seccion(id, descripcion, orden, codigo, url) values (3,'Cuentas',3, 'SEC_CUE', '')
insert into DBO.seccion(id, descripcion, orden, codigo, url) values (4,'Configuracion',2, 'SEC_CFG', '')

Insert into DBO.PARAMETROS 
   (ID_PARAMETRO, PARAMETRO, DESCRIPCION, TXT_AYUDA)
 Values
   ( 6, 'MIN_VIG_SESION', 'Minutos de vigencia de la sesión de autenticación.', 'Tiempo de vigencia de inicio de sesión.');

Insert into DBO.PARAMETROS_VALOR
   ( ID_PARAMETRO, VALOR, F_VIGENCIA_DESDE, F_VIGENCIA_HASTA, ID_PROVINCIA)
 Values
   (6, '30', cast('01/01/2013'as datetime), cast('09/09/9999'as datetime), 
    0);

/**********************************/

insert into dbo.CANALES_ACCESO (ID_CANAL_ACCESO, CANAL_ACCESO) values
(1, 'PUBLICO');
insert into dbo.CANALES_ACCESO (ID_CANAL_ACCESO, CANAL_ACCESO) values
(2, 'PRIVADO');
insert into dbo.CANALES_ACCESO (ID_CANAL_ACCESO,CANAL_ACCESO) values
(3, 'PUBLICO / PRIVADO');	

/************************************/

Insert into DBO.INTERFACES
   (ID_INTERFAZ, INTERFAZ, CLAVE, ACTIVADO, CANT_OPERACIONES, CANT_ACUM_OPERACIONES, ID_CANAL_ACCESO, FLG_CONTROL)
Values
   ( 1, 'DEMO', 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB', 'S', 0, 
    0, 3, 'N');
Insert into DBO.INTERFACES
   (ID_INTERFAZ, INTERFAZ, CLAVE, ACTIVADO, CANT_OPERACIONES, CANT_ACUM_OPERACIONES, ID_CANAL_ACCESO, FLG_CONTROL)
Values
   (4, 'Consola Web', 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB', 'S', 0, 
    0, 3, 'N');


/***********************************/

Insert into DBO.TIPOS_TRANSACCIONES (ID_TIPO_TRANSACCION, TRANSACCION)
Values	(1001, 'Autorizar Comprobante');

/****************************/
