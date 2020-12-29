use Sacca
go

/*
exec INS_USUARIO
					@USUARIO,
					@PASSWORD,
					@ROL,
					@APELLIDO (opcional),
					@NOMBRE (opcional),
					@EMAIL (opcional),
					@DESCRIPCION_ROL (opcional)
*/

-- La clave de este usuario es boldt 
exec INS_USUARIO 'ADMIN', '8201ca06098dacd45b157ed4a9889c22f2f39ba6','ROLE_ADMIN','Admnistrador', 'Señor', 'admin@admin.com', 'Administrador'


/*
exec INS_SECCION
	@SECCION,
	@ORDEN_SECCION,
	@CODIGO_SECCION,
	@URL_SECCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra esa sección
*/

exec INS_SECCION 'Administración', 1, 'SEC_ADM', ''
exec INS_SECCION 'Clientes', 2, 'SEC_CLI', ''
exec INS_SECCION 'Movimientos', 3, 'SEC_MOV', ''
exec INS_SECCION 'Ayuda', 4, 'SEC_HLP', ''


/*
exec INS_ITEM
	@ITEM,
	@CODIGO,
	@ORDEN,
	@URL,
	@ICON,
	@TARGET,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese item
*/
exec INS_ITEM 'Usuarios', 'SI_ADM_USR', 1, '/secure/usuarios/administracion.xhtml', 'admin-icon-blue', Null, 'Administración'
exec INS_ITEM 'Configuración', 'SI_CFG_MOD', 2, '/secure/configuracion/configuracion.xhtml', 'ui-icon-contact', Null, 'Administración'
exec INS_ITEM 'Log de Operaciones', 'SI_ADM_LOG', 3, '/secure/administracion/log.xhtml', 'ui-icon-contact', Null, 'Administración'
exec INS_ITEM 'Gestión clientes', 'SI_CLI_ABM', 1, '/secure/clientes/clientes.xhtml', 'ui-icon-person', Null, 'Clientes'
exec INS_ITEM 'Reporte', 'SI_CLI_TOT', 2, '/secure/clientes/clienteReporteTotal.xhtml', 'ui-icon-person', Null, 'Clientes'
exec INS_ITEM 'Reporte Movimientos', 'SI_CUE_MOV', 1, '/secure/cuentas/movimientos.xhtml', 'ui-icon-contact', Null, 'Movimientos'
exec INS_ITEM 'Ajuste Manual', 'SI_CUE_AJU', 2, '/secure/cuentas/ajusteManual.xhtml', 'ui-icon-wrench', Null, 'Movimientos'
exec INS_ITEM 'Exportación', 'SI_CUE_EXP', 3, '/secure/cuentas/exportaMovimientos.xhtml', 'ui-icon-disk', Null, 'Movimientos'
exec INS_ITEM 'Manual', 'SI_CUE_HLP', 1, '/secure/manual/index.html', 'ui-icon-help', '_blank', 'Ayuda'


/*
exec INS_ITEM_ROL				
	@ITEM,
	@ROL,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese permiso
*/

exec INS_ITEM_ROL 'SI_ADM_USR', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CUE_AJU', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CLI_ABM', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CFG_MOD', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CUE_MOV', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CUE_HLP', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CUE_EXP', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_ADM_LOG', 'ROLE_ADMIN'
exec INS_ITEM_ROL 'SI_CLI_TOT', 'ROLE_ADMIN'

/*
exe INS_PAIS @PAIS , @BORRAR(opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese país
*/
exec INS_PAIS 'Argentina'


/*
exe INS_PROVINCIA	@ID, @PROVINCIA, @COD_PROVINCIA, @PAIS, @BORRAR(opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra esa provincia
*/
exec INS_PROVINCIA 8, 'ENTRE RIOS', 'ER', 'Argentina'


/*
exe INS_LOCALIDAD	@LOCALIDAD, @PROVINCIA, @BORRAR(opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra esa localidad
*/

exec INS_LOCALIDAD 'Paraná', 'ENTRE RIOS'
exec INS_LOCALIDAD 'Gualeguaychú', 'ENTRE RIOS'


/*
exe INS_PARAMETRO 	'@PARAMETRO', 
					'@DESCRIPCION_CORTA', 
					'@DESCRIPCION_LARGA', 
					'@VALOR', 
					'@PROVINCIA'
*/

exec INS_PARAMETRO 	'DIAS_VEN_RES', 
					'Tiempo para poder retirar el dinero de una reserva', 
					'(en días) Es la validez en días de la reserva de dinero que los clientes hagan. No debe superar los tiempos de depuración (ej. 3).', 
					'15', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'DIAS_BAJA_CLIENTE', 
					'Tiempo para que un cliente realice su primer carga de crédito', 
					'(en días) Cuando un cliente se da de alta en el sistema, tiene un tiempo en el cual deberá realizar una primera carga de dinero para que su cuenta sea válida. Si el nuevo cliente no realizó una carga de dinero en ese tiempo, el sistema dará de baja al cliente. (ej. 1)', 
					'1', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'DEP_MOVIMIENTOS_CTACTE', 
					'Tiempo depuración movimientos de Cuenta Corriente.', 
					'(en días) Pasado este tiempo se depura en la base de datos los Movimientos de Cuenta Corriente. Debe ser mayor a Tiempo para poder retirar el dinero de una reserva (ej.7).', 
					'30', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'DEP_RESERVAS', 
					'Tiempo depuración movimientos de Reserva.', 
					'(en días) Pasado este tiempo se depura en la base de datos las Reservas. Debe ser mayor a Tiempo para poder retirar el dinero de una reserva (ej.7).', 
					'30', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'DEP_REG_TRANSAC', 
					'Tiempo de depuración Registro de Transacciones.', 
					'(en días) Pasado este tiempo se depura en la base de datos los Registros de Transacciones. Debe ser mayor a Tiempo para poder retirar el dinero de una reserva (ej.7).', 
					'7', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'MIN_VIG_SESION', 
					'Vigencia de sesión', 
					'(en minutos) Es el tiempo que tiene para operar un sistema externo contra Sacca (por ej. el BGS Central). Pasado ese tiempo, el sistema externo deberá iniciar sesión nuevamente. (ej. 30)', 
					'30', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'DEP_REG_LOG_TRANS', 
					'Tiempo de depuración Registro de Log de Transacciones.', 
					'(en días) Pasado este tiempo se depura en la base de datos los Registros de Log de Transacciones. Debe ser mayor a Tiempo para poder retirar el dinero de una reserva (ej.7).', 
					'7', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'COD_PROV_EXP', 
					'Código de provincia para la exportación a Libra', 
					'Número identificador de provincia de Libra (8=ER, 30=CB, 5=FM, 22=SF y 20=MS). Se usa en la exportación de movimientos hacia Libra.', 
					'8', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'LETRA_PROV_EXP', 
					'Letra de provincia para la exportación a Libra', 
					'(una letra) Identificador de provincia de Libra (N=ER, K=CB, O=FM, C=SF y M=MS). Se usa en la exportación de movimientos hacia Libra.', 
					'N', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'PREF_JUEGO_EXP', 
					'Prefijo de juego para exportación a Libra', 
					'(dos letras) Identificador de juego de Libra (ej. MO). Se usa en la exportación de movimientos hacia Libra.', 
					'MO', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'COD_JUEGO_EXP', 
					'Código de juego para exportación a Libra', 
					'(dos números) Identificador de juego de Libra (ej. 40). Se usa en la exportación de movimientos hacia Libra.', 
					'40', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'CANT_ULT_MOVS', 
					'Cantidad de movimientos a mostrar al cliente', 
					'(en números) Indica cuántos movimientos mostrar al cliente cuando este requiere sus últimos movimientos (ej. 10).', 
					'4', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'VENC_PIN_GENERADO', 
					'Tiempo de validez de la clave por primera vez o reseteada', 
					'(en horas) Indica de cuántas horas dispone el cliente para registrar su clave definitiva luego de registrarse en el sistema o de solicitar un blanqueo de clave (ej. 24).', 
					'10', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'DIAS_VENC_PIN_MODIFICADO', 
					'Tiempo de validez de la clave modificada por el usuario', 
					'(en días) Indica cuánto tiempo tiene validez la clave elegida por el usuario como PIN luego de modificarla (ej. 180). Luego deberá cambiarla.', 
					'10', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'NUM_INTENTOS_BLOQUEO', 
					'Cantidad de login fallidos antes de que el cliente sea bloqueado', 
					'(en números) Si el cliente pone su pin en modo errado por más de la cantidad de veces indicada, el sistema lo bloqueará y no podrá operar hasta que no sea desbloqueado por consola o blanquee su clave. (ej. 3)', 
					'3', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'NUM_MAX_RESERVA_DIARIA', 
					'Tope máximo Cantidad diaria de Reservas', 
					'(número) El cliente puede realizar Reservar de una cuenta hasta la cantidad seteada en un DÍA (ej. 1)', 
					'3', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MONTO_MAX_RESERVA_DIARIA', 
					'Tope máximo Monto diario de Reservas', 
					'(número) Las Reservas de una cuenta que haga el cliente pueden alcanzar hasta este máximo en un DÍA (ej. 10000)', 
					'100', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MUM_MAX_RESERVA_MENSUAL', 
					'Tope máximo Cantidad mensual de Reservas', 
					'(número) El cliente puede realizar Reservar de una cuenta hasta la cantidad seteada por MES (ej. 45)', 
					'3', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MONTO_MAX_RESERVA_MENSUAL', 
					'Tope máximo Monto mensual de Reservas', 
					'(número) Las Reservas de una cuenta que haga el cliente pueden alcanzar hasta este máximo por MES (ej. 200000)', 
					'1000', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'NUM_MAX_EXTRACCION_DIARIA', 
					'Tope máximo Cantidad diaria de Retiros de dinero', 
					'(número) El cliente puede realizar Retiros de dinero de una cuenta hasta la cantidad seteada en un DÍA (ej. 2)', 
					'2', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MONTO_MAX_EXTRACCION_DIARIA', 
					'Tope máximo Monto diario de Retiros de dinero', 
					'(número) Los Retiros de dinero de una cuenta que haga el cliente pueden alcanzar hasta este máximo en un DÍA (ej. 10000)', 
					'100', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MUM_MAX_EXTRACCION_MENSUAL', 
					'Tope máximo Cantidad mensual de Retiros de dinero', 
					'(número) El cliente puede realizar Retiros de dinero de una cuenta hasta la cantidad seteada por MES (ej. 60)', 
					'10', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MONTO_MAX_EXTRACCION_MENSUAL', 
					'Tope máximo Monto mensual de Retiros de dinero', 
					'(número) Los Retiros de dinero de una cuenta que haga el cliente pueden alcanzar hasta este máximo por MES (ej. 200000)', 
					'1000', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'CANTIDAD_CLAVES_REPETIDAS', 
					'Cantidad de claves repetidas', 
					'(número) Para controlar que la nueva clave del cliente no se repita con las claves ingresadas previamente (ej. 5). Si se coloca 0 el cliente puede repetir su clave.', 
					'0', 
					'ENTRE RIOS'	
exec INS_PARAMETRO 	'MONTO_MAXIMO_CARGA_DIARIA', 
					'Tope de carga diario', 
					'(número) Las Cargas de Crédito que haga el cliente pueden alcanzar hasta este máximo diario. Colocando 0 se desactiva. (ej. 200000)', 
					'0', 
					'ENTRE RIOS'
exec INS_PARAMETRO 	'MONTO_MAXIMO_CARGA_POR_TRANSACCION', 
					'Tope de carga en transacción', 
					'(número) Cada Carga de Crédito pueden alcanzar hasta este máximo. Colocando 0 se desactiva. (ej. 2500)', 
					'0', 
					'ENTRE RIOS'

/*
exec INS_ESTADOS_CIVILES
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese estado civil
*/

exec INS_ESTADOS_CIVILES 1, 'Soltero'
exec INS_ESTADOS_CIVILES 2, 'Casado'
exec INS_ESTADOS_CIVILES 3, 'Viudo/a'

/*
exec INS_ESTADOS_RESERVAS
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese estado reserva
*/

exec INS_ESTADOS_RESERVAS 1, 'Reservada'
exec INS_ESTADOS_RESERVAS 2, 'Retirada'
exec INS_ESTADOS_RESERVAS 3, 'Vencida'

/*
exec INS_TIPO_DOCUMENTO
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese tipo de documento
*/

exec INS_TIPO_DOCUMENTO 1, 'DNI'
exec INS_TIPO_DOCUMENTO 2, 'LE', 1
exec INS_TIPO_DOCUMENTO 3, 'LC', 1
exec INS_TIPO_DOCUMENTO 4, 'CI', 1
exec INS_TIPO_DOCUMENTO 5, 'PSP', 1
exec INS_TIPO_DOCUMENTO 6, 'cuit/cuil', 1
exec INS_TIPO_DOCUMENTO 7, 'DSC', 1

/*
exec INS_SEXO
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_SEXO 1, 'Masculino'
exec INS_SEXO 2, 'Femenino'

/*
exec INS_CANAL_VENTA
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_CANAL_VENTA 1, 'Red Propia'
exec INS_CANAL_VENTA 2, 'Canal Web'
exec INS_CANAL_VENTA 3, 'Canal Móvil'
exec INS_CANAL_VENTA 4, 'Telinfor'

/*
exec INS_TIPO_CTA
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_TIPO_CTA 1, 'Pesos Argentinos'
--exec INS_TIPO_CTA 2, 'Bonos' ,1

/*
exec INS_CANAL_ACCESO
	@ID,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_CANAL_ACCESO 1, 'PUBLICO'
exec INS_CANAL_ACCESO 2, 'PRIVADO'
exec INS_CANAL_ACCESO 3, 'PUBLICO / PRIVADO'

/*
exec INS_ESTADO_CLIENTE
	@ID,
	@NOMBRE,
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_ESTADO_CLIENTE 1, 'ACTIVO', 'Usuario activo'
exec INS_ESTADO_CLIENTE 2, 'BLOQUEADO', 'Usuario bloqueado'

/*
exec INS_INTERFAZ
	@ID,
	@NOMBRE,
	@CLAVE,
	@CANAL_ACCESO
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_INTERFAZ 1, 'MONEDERO', 'D404559F602EAB6FD602AC7680DACBFAADD13630335E951F097AF3900E9DE176B6DB28512F2E000B9D04FBA5133E8B1C6E8DF59DB3A8AB9D60BE4B97CC9E81DB', 'PUBLICO / PRIVADO'
exec INS_INTERFAZ 4, 'Consola Monedero', '1234', 'PUBLICO / PRIVADO'

/*
exec INS_TIPO_MOVIMIENTO
	@ID,
	@NOMBRE,
	@FACTOR
	@DESCRIPCION,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_TIPO_MOVIMIENTO 1, 'CREDITO_CARGA', '1', 'Crédito por carga de saldo', 1, 'Cred x carga'
exec INS_TIPO_MOVIMIENTO 2, 'CREDITO_PREMIO', '2', 'Crédito por apuesta ganada', 1, 'Cred x apuesta'
exec INS_TIPO_MOVIMIENTO 3, 'DEBITO_RESERVA', '3', 'Débito por reserva', -1, 'Deb x reserva'
exec INS_TIPO_MOVIMIENTO 4, 'DEBITO_APUESTA', '4', 'Débito por apuesta', -1, 'Deb x apuesta'
exec INS_TIPO_MOVIMIENTO 5, 'CREDITO_RESERVA_CAIDA', '5', 'Crédito por reserva caida', 1, 'Cred res.venc'
exec INS_TIPO_MOVIMIENTO 6, 'CREDITO_DEVOLUCION_APUESTA', '6', 'Crédito por Devolución Apuesta', 1, 'Cred x apuesta'
exec INS_TIPO_MOVIMIENTO 7, 'RETIRO_DINERO', '7', 'Retiro de dinero (sin movimiento)', 1, 'Retiro dinero'

/*
exec INS_TIPO_TRANSACCION 
	@ID,
	@NOMBRE,
	@TRANSACCION
	@TRANSACCION_REST,
	@URL_REST,
	@BORRAR (opcional)				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_TIPO_TRANSACCION 1, 'Registrar Cliente', 'registrarCliente','registrarCliente', '/cliente/registrar'
exec INS_TIPO_TRANSACCION 2, 'Credito Por Carga Terminal', 'creditoPorCargaTerminal','cargaPorTerminal', '/credito/cargaPorTerminal'
exec INS_TIPO_TRANSACCION 4, 'Consultar Saldo', 'consultarSaldo','1', '1'
exec INS_TIPO_TRANSACCION 5, 'Consultar Cliente', 'consultarCliente','consultarCliente', '/cliente/consultar'
exec INS_TIPO_TRANSACCION 6, 'Debito Por Apuesta Generada', 'debitoPorApuestaGenerada','apuesta', '/debito/apuesta'
exec INS_TIPO_TRANSACCION 7, 'Credito Por Apuesta Ganada', 'creditoPorApuestaGanada', 'creditoPorPremio', '/credito/creditoPorPremio'
exec INS_TIPO_TRANSACCION 8, 'Generar Reserva', 'generarReserva','generarReserva', '/reserva/generar'
exec INS_TIPO_TRANSACCION 9, 'Retirar Dinero Reserva', 'retirarDineroReserva','retirarReserva', '/reserva/retirar'
exec INS_TIPO_TRANSACCION 10, 'Consultar Reserva', 'consultarReserva','consultarReserva', '/reserva/consultar'
exec INS_TIPO_TRANSACCION 11, 'Cambio de clave de cliente', 'cambiarClave','cambiarClave', '/cliente/cambiarClave'
exec INS_TIPO_TRANSACCION 12, 'Consulta últimos movimientos', 'consultarMovimientos','consultarMovimientos', '/movimiento/consultar'
exec INS_TIPO_TRANSACCION 13, 'consulta Codigo Externo', 'consultarCodigoExterno','3', '3'
exec INS_TIPO_TRANSACCION 14, 'Modificar Cliente', 'modificarCliente','modificarCliente', '/cliente/modificar'
exec INS_TIPO_TRANSACCION 15, 'Retirar dinero sin reserva', 'retirarDineroSinReserva','4', '4'
exec INS_TIPO_TRANSACCION 16, 'Blanquear Clave', 'blanquearClave','blanquearClave', '/cliente/blanquearClave'
exec INS_TIPO_TRANSACCION 17, 'Pedido de Balnqueo de Clave', 'generarBlanqueoClave','generarBalnqueoClave', '/cliente/generarBlanqueoClave'
exec INS_TIPO_TRANSACCION 18, 'Crédito por Devolución de Apuesta', 'creditoPorDevolucionDeApuesta','cargaPorTerminal', '/credito/devolucionDeApuesta'
exec INS_TIPO_TRANSACCION 19, 'Actualizar Términos y Condiciones Legales', 'actualizarTerminoCondicion','actualizarTerminoCondicion', '/cliente/actualizarTermino'
exec INS_TIPO_TRANSACCION 20, 'Datos del Cliente Completos', 'datosDelClienteCompletos','datosDelClienteCompletos', '/cliente/datosDelClienteCompletos'
exec INS_TIPO_TRANSACCION 21, 'Actualizar e-Mail', 'actualizarEMail','actualizarEMail', '/cliente/actualizarEMail'
exec INS_TIPO_TRANSACCION 22, 'Actualizar Términos y Condiciones Legales y EMail', 'actualizarTerminoCondicionEMail','actualizarTerminoCondicionEMail', '/cliente/actualizarTerminoEMail'

/*
exec INS_PERMISO_TRANSACCION_CANAL_VENTA
	@CANAL_VENTA varchar(50),
	@TRANSACCION varchar(50),
	@BORRAR tinyint = null				si el parámetro @BORRAR existe (no importa el valor) entonces borra ese sexo
*/

exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Registrar Cliente'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Credito Por Carga Terminal'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Consultar Saldo'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Consultar Cliente'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Debito Por Apuesta Generada'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Credito Por Apuesta Ganada'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Generar Reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Retirar Dinero Reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Consultar Reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Cambio de clave de cliente'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Consulta últimos movimientos'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'consulta Codigo Externo'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Modificar Cliente'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Retirar dinero sin reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Blanquear Clave'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Pedido de Balnqueo de Clave'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Crédito por Devolución de Apuesta'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Actualizar Términos y Condiciones Legales'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Datos del Cliente Completos'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Actualizar e-Mail'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Red Propia', 'Actualizar Términos y Condiciones Legales y EMail'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Consultar Saldo'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Consultar Cliente'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Debito Por Apuesta Generada'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Generar Reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Consultar Reserva'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Consulta últimos movimientos'
exec INS_PERMISO_TRANSACCION_CANAL_VENTA 'Telinfor', 'Crédito por Devolución de Apuesta'
   
/***************************/
/*  Borrado Stored         */
/***************************/
GO
DROP PROCEDURE INS_USUARIO
GO
DROP PROCEDURE INS_ITEM
GO
DROP PROCEDURE INS_SECCION
GO
DROP PROCEDURE INS_ITEM_ROL
GO
DROP PROCEDURE INS_LOCALIDAD
GO
DROP PROCEDURE INS_PARAMETRO
GO
DROP PROCEDURE INS_ESTADOS_CIVILES
GO
DROP PROCEDURE INS_ESTADOS_RESERVAS
GO
DROP PROCEDURE INS_TIPO_DOCUMENTO
GO
DROP PROCEDURE INS_SEXO
GO
DROP PROCEDURE INS_CANAL_VENTA
GO
DROP PROCEDURE INS_TIPO_CTA
GO
DROP PROCEDURE INS_CANAL_ACCESO
GO
DROP PROCEDURE INS_ESTADO_CLIENTE
GO
DROP PROCEDURE INS_INTERFAZ
GO
DROP PROCEDURE INS_TIPO_MOVIMIENTO
GO
DROP PROCEDURE INS_TIPO_TRANSACCION
GO
DROP PROCEDURE INS_PERMISO_TRANSACCION_CANAL_VENTA
GO




