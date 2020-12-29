package ar.com.coninf.doconline.business.negocio;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.business.enums.TipoTransaccionEnum;
import ar.com.coninf.doconline.business.log.LogTransaccionContenido;
import ar.com.coninf.doconline.model.dao.LogTransaccionDao;
import ar.com.coninf.doconline.shared.dto.LogTransaccionDto;
import ar.com.coninf.doconline.shared.dto.RegistroTransaccionDto;
import ar.com.coninf.doconline.shared.excepcion.ApplicationException;

@Component("business.logTransaccionBusiness")
public class LogTransaccionBusiness {

	@Autowired
	@Qualifier("logTransaccionDao")
	private LogTransaccionDao logTransaccionDao;
	
	@Autowired
	@Qualifier("business.registroTransaccionBusiness")
	private RegistroTransaccionBusiness registroTransaccionBusiness;
	
	public void registrarAuditoria(LogTransaccionContenido log) {
		try {
			LogTransaccionDto logDto = new LogTransaccionDto();
			logDto.setFechaFinOp(new Timestamp(log.getFechaFinOp().getTime()));
			logDto.setFechaInicioOp(new Timestamp(log.getFechaInicioOp().getTime()));
			logDto.setTipoTransaccionId(TipoTransaccionEnum.valueOf(log.getOperacion()).getIdTransaccion());
			
			if(log.getCtxInterfaz() == null || log.getCtxNroTransaccion() == null){
				//No se puede registrar la auditoria ya que no tengo una transaccion
				return;
			}
			
			RegistroTransaccionDto regTx = new RegistroTransaccionDto();
			regTx.setInterfazId(Integer.parseInt(log.getCtxInterfaz()));
			regTx.setCodigoTransaccion(Long.parseLong(log.getCtxNroTransaccion()));
			regTx.setServicio(log.getCtxServicio());
			regTx.setFechaTransaccion(log.getCtxFechaTransaccion());
			regTx = registroTransaccionBusiness.getByKey(regTx);
			logDto.setRegistroTransaccionId(regTx.getId());
			
			char[] xmlChar = new char[log.getXmlEntrada().length()];
			log.getXmlEntrada().getChars(0, log.getXmlEntrada().length(), xmlChar, 0);
			logDto.setXmlEntrada(xmlChar);
			
			xmlChar = new char[log.getXmlSalida().length()];
			log.getXmlSalida().getChars(0, log.getXmlSalida().length(), xmlChar, 0);
			logDto.setXmlSalida(xmlChar);
			
			if (log.getXmlRequest()!=null) {
				xmlChar = new char[log.getXmlRequest().length()];
				log.getXmlRequest().getChars(0, log.getXmlRequest().length(), xmlChar, 0);
			} else {
				xmlChar = new char[0];
			}
			logDto.setXmlRequestAfip(xmlChar);			
			
			if (log.getXmlResponse()!=null) {
				xmlChar = new char[log.getXmlResponse().length()];
				log.getXmlResponse().getChars(0, log.getXmlResponse().length(), xmlChar, 0);
			} else {
				xmlChar = new char[0];
			}
			logDto.setXmlResponseAfip(xmlChar);
			
			logDto.setCodigo(log.getCodigo());
			logDto.setDescripcion(log.getDescripcion());
			logDto.setObservacion(log.getObservacion());
			logDto.setExcepcionWsaa(log.getExcepcionWsaa());
			logDto.setExcepcionWsfev1(log.getExcepcionWsfev1());
			logDto.setResultado(log.getResultado());
			logDto.setErrMsg(log.getErrMsg());
			logDto.setObs(log.getObs());
			logDto.setTipoComprobante(log.getTipoCbte());
			logDto.setPuntoVenta(log.getPtoVtaCbte());
			logDto.setNumeroComprobante(log.getNroCbte());
			logDto.setComprobante(log.getCbte());
			logDto.setFechaComprobante(log.getFechaCbte());
			logDto.setImporteTotal(log.getImpTotal());
			logDto.setCae(log.getCae());
			logDto.setFechaVencimiento(log.getFechaVencimiento());
			
			logTransaccionDao.insertarAuditoria(logDto);
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

}
