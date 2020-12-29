CREATE TABLE encabezado (
    id INTEGER  PRIMARY KEY,
    tipo_reg INTEGER ,
    webservice VARCHAR (5),
    fecha_cbte VARCHAR (8),
    tipo_cbte INTEGER ,
    punto_vta INTEGER ,
    cbte_nro INTEGER ,
    tipo_expo INTEGER ,
    permiso_existente VARCHAR (1),
    dst_cmp INTEGER ,
    nombre_cliente VARCHAR (200),
    tipo_doc INTEGER ,
    nro_doc INTEGER ,
    domicilio_cliente VARCHAR (300),
    id_impositivo VARCHAR (50),
    imp_total NUMERIC (15, 3),
    imp_tot_conc NUMERIC (15, 3),
    imp_neto NUMERIC (15, 3),
    impto_liq NUMERIC (15, 3),
    impto_liq_rni NUMERIC (15, 3),
    imp_op_ex NUMERIC (15, 3),
    impto_perc NUMERIC (15, 2),
    imp_iibb NUMERIC (15, 3),
    impto_perc_mun NUMERIC (15, 3),
    imp_internos NUMERIC (15, 3),
    imp_trib NUMERIC (15, 3),
    moneda_id VARCHAR (3),
    moneda_ctz NUMERIC (10, 6),
    obs_comerciales VARCHAR (1000),
    obs VARCHAR (1000),
    forma_pago VARCHAR (50),
    incoterms VARCHAR (3),
    incoterms_ds VARCHAR (20),
    idioma_cbte VARCHAR (1),
    zona VARCHAR (5),
    fecha_venc_pago VARCHAR (8),
    presta_serv INTEGER ,
    fecha_serv_desde VARCHAR (8),
    fecha_serv_hasta VARCHAR (8),
    cae INTEGER ,
    fecha_vto VARCHAR (8),
    resultado VARCHAR (1),
    reproceso VARCHAR (1),
    motivo VARCHAR (40),
    telefono_cliente VARCHAR (50),
    localidad_cliente VARCHAR (50),
    provincia_cliente VARCHAR (50),
    formato_id INTEGER ,
    email VARCHAR (100),
    pdf VARCHAR (100),
    err_code VARCHAR (6),
    err_msg VARCHAR (1000)
);
CREATE TABLE detalle (
    id INTEGER  FOREING KEY encabezado,
    tipo_reg INTEGER ,
    codigo VARCHAR (30),
    qty NUMERIC (12, 2),
    umed INTEGER ,
    precio NUMERIC (12, 3),
    imp_total NUMERIC (14, 3),
    iva_id INTEGER ,
    ds VARCHAR (4000),
    ncm VARCHAR (15),
    sec VARCHAR (15),
    bonif NUMERIC (15, 2)
);
CREATE TABLE permiso (
    id INTEGER  FOREING KEY encabezado,
    tipo_reg INTEGER ,
    id_permiso VARCHAR (16),
    dst_merc INTEGER 
);
CREATE TABLE cmp_asoc (
    id INTEGER  FOREING KEY encabezado,
    tipo_reg INTEGER ,
    cbte_tipo INTEGER ,
    cbte_punto_vta INTEGER ,
    cbte_nro INTEGER 
);
CREATE TABLE iva (
    id INTEGER  FOREING KEY encabezado,
    tipo_reg INTEGER ,
    iva_id INTEGER ,
    base_imp NUMERIC (15, 3),
    importe NUMERIC (15, 3)
);
CREATE TABLE tributo (
    id INTEGER  FOREING KEY encabezado,
    tipo_reg INTEGER ,
    tributo_id INTEGER ,
    desc VARCHAR (100),
    base_imp NUMERIC (15, 3),
    alic NUMERIC (15, 2),
    importe NUMERIC (15, 3)
);
CREATE TABLE xmls (
    id INTEGER  FOREING KEY encabezado,
    xml_response TEXT,
    xml_request TEXT,
    ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE formatos_pdf (
    pk INTEGER IDENTITY PRIMARY KEY, 
    formato_id INTEGER,
    name VARCHAR (50),
    type VARCHAR (2),
    x1 FLOAT,
    y1 FLOAT,
    x2 FLOAT,
    y2 FLOAT,
    font VARCHAR (50),
    size FLOAT,
    bold BIT,
    italic BIT,
    underline BIT,
    foreground INTEGER,
    backgroud INTEGER,
    align VARCHAR(1),
    text VARCHAR(250),
    priority INTEGER
);