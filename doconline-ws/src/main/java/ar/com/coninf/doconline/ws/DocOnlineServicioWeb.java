package ar.com.coninf.doconline.ws;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.codec.EncoderException;

import com.google.zxing.WriterException;

import ar.com.coninf.doconline.rest.model.request.RequestActualizarItemComprobante;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarPadronLocal;
import ar.com.coninf.doconline.rest.model.request.RequestConsultarPadronOnline;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseActualizarItemComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseAutenticacion;
import ar.com.coninf.doconline.rest.model.response.ResponseAutorizarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarPadronLocal;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarPadronOnline;
import ar.com.coninf.doconline.rest.model.response.ResponseConsultarUltimoComprobante;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;
import ar.com.coninf.doconline.rest.model.tx.ComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;
import ar.com.coninf.doconline.rest.model.tx.DatoOpcional;
import ar.com.coninf.doconline.rest.model.tx.DatoQr;
import ar.com.coninf.doconline.rest.model.tx.Iva;
import ar.com.coninf.doconline.rest.model.tx.PeriodoComprobanteAsociado;
import ar.com.coninf.doconline.rest.model.tx.Tributo;

public interface DocOnlineServicioWeb {
	
	public ResponseAutenticacion iniciarSesion(Integer interfaz, String clave);
	public Response cerrarSesion(Integer interfaz, String idSesion);

	public ResponseAutorizarComprobante autorizarComprobante(ControlTransaccion ctx, 
			Integer concepto, Integer tipoDoc, Long nroDoc, Integer tipoCbte, Integer ptoVta, Long nroCbte, 
			BigDecimal impTotal, BigDecimal impTotConcNoGrav, BigDecimal impNeto, BigDecimal impIva, BigDecimal impTrib, BigDecimal impOpEx, 
			String fechaCbte, String fechaVencPago, String fechaServDesde, String fechaServHasta,
			String monedaId, BigDecimal monedaCtz, 
			Tributo[] tributos, Iva[] ivas, ComprobanteAsociado[] comprobantesAsociados, 
			DatoOpcional[] datosOpcionales, PeriodoComprobanteAsociado[] periodosAsociados);
	
	public ResponseConsultarUltimoComprobante consultarUltimoComprobante(ControlTransaccion ctx, Integer tipoCbte, Integer ptoVta);
	public ResponseConsultarComprobante consultarComprobante(ControlTransaccion ctx, Integer tipoCbte, Integer ptoVta, Integer cbte);
	public ResponseActualizarItemComprobante actualizarItemComprobante(RequestActualizarItemComprobante request);
	public ResponseConsultarPadronLocal consultarPadronLocal(RequestConsultarPadronLocal request);
	public ResponseConsultarPadronOnline consultarPadronOnline(RequestConsultarPadronOnline request);
	
	public ResponseGenerarQr generarQr(ControlTransaccion ctx, DatoQr datoQr) throws IOException, WriterException, EncoderException;

}
