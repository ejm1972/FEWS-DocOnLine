package ar.com.coninf.doconline.ws.ayudante;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.codec.EncoderException;

import com.google.zxing.WriterException;

import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.tx.ControlTransaccion;

public interface AyudanteWS<D> {
	public Response hacer(ControlTransaccion ctx, D datos) throws SQLException, IOException, WriterException, EncoderException;
	
}
