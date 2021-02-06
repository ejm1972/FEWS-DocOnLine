package ar.com.coninf.doconline.business.facade;

import java.io.IOException;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.zxing.WriterException;

import ar.com.coninf.doconline.business.negocio.GenerarQrBusiness;
import ar.com.coninf.doconline.rest.model.request.RequestGenerarQr;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;

@Component("facade.generarQrFacade")
public class GenerarQrFacade {

	@Autowired
	@Qualifier("business.generarQrBusiness")
	private GenerarQrBusiness generarQrBusiness;
	
	public ResponseGenerarQr generarQr(RequestGenerarQr datos) throws IOException, WriterException, EncoderException {
		
		return generarQrBusiness.generarQr(datos);
	}

}
