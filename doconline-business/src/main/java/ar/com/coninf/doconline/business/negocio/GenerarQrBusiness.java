package ar.com.coninf.doconline.business.negocio;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import ar.com.boldt.monedero.shared.constant.MonederoParametros;
import ar.com.coninf.doconline.model.dao.ParametroDao;
import ar.com.coninf.doconline.rest.model.enums.ErrorEnum;
import ar.com.coninf.doconline.rest.model.request.RequestGenerarQr;
import ar.com.coninf.doconline.rest.model.response.Response;
import ar.com.coninf.doconline.rest.model.response.ResponseGenerarQr;

@Component("business.generarQrBusiness")
public class GenerarQrBusiness extends AbstractBusiness {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	@Qualifier("parametroDao")
	private ParametroDao parametroDao;
	
	public ResponseGenerarQr generarQr(RequestGenerarQr req) throws IOException, WriterException {
		
		logger.debug("Ejecucion generarQr() para FE");
		
		// Image properties
		int qrImageWith = Integer.parseInt(parametroDao.getValorVigente(MonederoParametros.QR_IMAGE_WIDTH));
		int qrImageHeight = Integer.parseInt(parametroDao.getValorVigente(MonederoParametros.QR_IMAGE_HEIGHT));
		String imageFormat = parametroDao.getValorVigente(MonederoParametros.QR_IMAGE_FORMAT);
		String imagePathFile = paramUserDir+"qrcode.";
		
		BufferedImage image = crearQR(req.getTextoQr(), qrImageWith, qrImageHeight);
		
		// Write the image to a file
		FileOutputStream qrFile = null;
		qrFile = new FileOutputStream(imagePathFile.concat(imageFormat));
		ImageIO.write(image, imageFormat, qrFile);
		qrFile.close();
				
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, imageFormat, baos );
		baos.flush();
		byte[] imageAsBytes = baos.toByteArray();
		baos.close();
		
		ResponseGenerarQr resp = new ResponseGenerarQr();
		resp.setEsReintento(false);
		resp.cargarError(new Response(ErrorEnum.SIN_ERROR));
		
		resp.setImagenQr(imageAsBytes);
		
		return resp;
	}
	
	private BufferedImage crearQR(String datos, int ancho, int altura) throws WriterException {
        BitMatrix matrix;
        Writer escritor = new QRCodeWriter();
        matrix = escritor.encode(datos, BarcodeFormat.QR_CODE, ancho, altura);
        
        BufferedImage imagen = new BufferedImage(ancho, altura, BufferedImage.TYPE_INT_RGB);
        
        for(int y = 0; y < altura; y++) {
            for(int x = 0; x < ancho; x++) {
                int grayValue = (matrix.get(x, y) ? 0 : 1) & 0xff;
                imagen.setRGB(x, y, (grayValue == 0 ? 0 : 0xFFFFFF));
            }
        }
        
        return imagen;        
    }  

}


