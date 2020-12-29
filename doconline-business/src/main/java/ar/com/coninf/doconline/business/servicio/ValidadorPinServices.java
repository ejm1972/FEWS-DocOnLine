package ar.com.coninf.doconline.business.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ar.com.coninf.doconline.model.dao.ParametroDao;

@Component("services.validadorPinServices")
public class ValidadorPinServices {

	@Autowired
	@Qualifier("parametroDao")
	private ParametroDao parametroDao;

//	public boolean validarPin(ClienteDto clienteOrg, ClienteDto clienteDst) {
//
//		try {
//			
//			//valido el pin...
//			String pinOrg = EncriptacionUtiles.byteArrayToHexString(EncriptacionUtiles.computeHash(clienteOrg.getPin()));
//			String pinDst = clienteDst.getPin();
//			
//			if (!pinOrg.equals(pinDst)) {
//				registroIntento(clienteDst);
//				return false;
//			}
//			else {
//				blanqueoIntentos(clienteDst);
//				return true;
//			}
//
//		} catch (ApplicationException e){
//			throw e;
//		} catch (Exception e) {
//			throw new ApplicationException(e);
//		}
//	}
//
//	public Response validarPin(ClienteDto cliING) {
//
//		ClienteDto cliCLI;
//		Response respuesta =  null;
//		
//		try {
//			
//			cliCLI = clienteDao.getById(cliING.getId());
//			
//			//valido el pin...
//			String pinING = EncriptacionUtiles.byteArrayToHexString(EncriptacionUtiles.computeHash(cliING.getPin()));
//			String pinCLI = cliCLI.getPin();
//			
//			if (!pinING.equals(pinCLI)) {
//				registroIntento(cliCLI);
//				respuesta = new Response(ErrorEnum.ERROR_PIN_INVALIDO, "error.ws-pin_invalido");
//				return respuesta;
//			}
//			else {
//				blanqueoIntentos(cliCLI);
//				return null;
//			}
//
//		} catch (ApplicationException e){
//			throw e;
//		} catch (Exception e) {
//			throw new ApplicationException(e);
//		}
//	}
//
//	public void registroIntento(ClienteDto cliente) {
//
//		try {
//			
//			int numMaxIntentos = Integer.parseInt(parametroDao.getValorVigente(MonederoParametros.NUM_INTENTOS_BLOQUEO));
//			int intentosFallidos = cliente.getAccesosFallidos();
//			intentosFallidos++;
//				
//			if (numMaxIntentos>intentosFallidos) {
//				cliente.setAccesosFallidos(intentosFallidos);
//			}
//			else {
//				if (numMaxIntentos==intentosFallidos) {
//					cliente.setAccesosFallidos(intentosFallidos);
//				}
//				cliente.setFlgHabilitado(TipoEstadoClienteEnum.BLOQUEADO.getCodigo());
//			}
//			clienteDao.update(cliente);
//		
//		} catch (ApplicationException e){
//			throw e;
//		} catch (Exception e) {
//			throw new ApplicationException(e);
//		}
//	}
//	
//	public void blanqueoIntentos(ClienteDto cliente) {
//
//		try {
//	
//			cliente.setAccesosFallidos(0);
//			clienteDao.update(cliente);
//			
//		} catch (ApplicationException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new ApplicationException(e);
//		}
//	}
}

