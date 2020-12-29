/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package ar.com.boldt.monedero.ws.handler;

import java.util.Date;
import java.util.Hashtable;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.llom.OMElementImpl;
import org.apache.axiom.soap.impl.llom.SOAPBodyImpl;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ar.com.coninf.doconline.business.enums.TipoComprobanteAFIP;
import ar.com.coninf.doconline.business.facade.LogTransaccionFacade;
import ar.com.coninf.doconline.business.log.LogTransaccionContenido;
import ar.com.coninf.doconline.shared.util.MonederoUtiles;

public class AuditoriaServicioWebHandler extends AbstractHandler implements Handler {
	private static final Log log = LogFactory.getLog(AuditoriaServicioWebHandler.class);
	private static Hashtable<String, LogTransaccionContenido> info = new Hashtable<String, LogTransaccionContenido>();
    private String name;
    private final String nsURI = "http://impl.ws.doconline.coninf.com.ar";
    private final String nsURICtx = "http://tx.model.rest.doconline.coninf.com.ar/xsd";
    
    private LogTransaccionFacade logTransaccionFacade;

    public String getName() {
        return name;
    }
    
	@Override
	public void init(HandlerDescription handlerdesc) {
		super.init(handlerdesc);
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[]{"ctx/application-context.xml"}, true);
		logTransaccionFacade = (LogTransaccionFacade)appCtx.getBean("facade.logTransaccionFacade");
		appCtx.close();
	}

	public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {
    	
    	MessageContext messFault = msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_FAULT_VALUE);
    	if(messFault != null){
    		return InvocationResponse.CONTINUE;
    	}
    	
        MessageContext messOUT = msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE);
        if(messOUT != null) {
        	MessageContext messIN = msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        	LogTransaccionContenido log = info.get(messIN.getLogCorrelationID());
        	if(log != null){
        		log.setXmlSalida(messOUT.getEnvelope().toString());
        		
        		SOAPBodyImpl body = (SOAPBodyImpl) messOUT.getEnvelope().getBody().getDescendants(Boolean.TRUE).next();
                OMElementImpl bodyEl = (OMElementImpl) body.getDescendants(Boolean.TRUE).next();
                
                String responseName = msgContext.getOperationContext().getOperationName().concat("Response");
        		QName qName = new javax.xml.namespace.QName(nsURI, responseName);
        		OMElementImpl response = (OMElementImpl) bodyEl.getChildrenWithName(qName).next();
        		qName = new javax.xml.namespace.QName(nsURI, "return");
        		OMElementImpl retorno = (OMElementImpl) response.getChildrenWithName(qName).next();
        		
        		OMElement firstElement = retorno.getFirstElement();
        		
        		String codigo = null;
        		String descripcion = null;
        		String observacion = null;

        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "codigo");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	codigo = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "descripcion");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	descripcion = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "observacion");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	observacion = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}        		
        		
        		String excepcionWsaa = null;
        		String excepcionWsfev1 = null;
        		String resultado = null;
        		String errMsg = null;
        		String obs = null;

        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "excepcionWsaa");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	excepcionWsaa = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "excepcionWsfev1");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	excepcionWsfev1 = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "resultado");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	resultado = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "errMsg");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	errMsg = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "obs");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	obs = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		
        		String xmlRequest = null;
        		String xmlResponse = null;
        		
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "xmlRequest");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	xmlRequest = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "xmlResponse");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	xmlResponse = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		
        		String cae = null;
        		String fechaVencimiento = null;
        		
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "cae");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	cae = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "fechaVencimiento");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	fechaVencimiento = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		
        		String ultimoComprobante = null;
        		String fechaCbte = null;
        		String impTotal = null;
        	
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "fechaCbte");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	fechaCbte = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "impTotal");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	impTotal = el != null ? el.getText() : null;
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		qName = new javax.xml.namespace.QName(firstElement.getQName().getNamespaceURI(), "ultimoComprobante");
        		try {
                	OMElementImpl el = (OMElementImpl) retorno.getChildrenWithName(qName).next();
                	ultimoComprobante = el != null ? el.getText() : null;
                	if (ultimoComprobante!=null) {
                		ultimoComprobante = String.valueOf("00000000").concat(ultimoComprobante);
                		ultimoComprobante = ultimoComprobante.substring(ultimoComprobante.length()-8);
                	}
            	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        		
        		/*-------------------------------------------------------*/
        		log.setCodigo(codigo);
        		log.setDescripcion(descripcion);
        		log.setObservacion(observacion);
        		
        		log.setExcepcionWsaa(excepcionWsaa);
        		log.setExcepcionWsfev1(excepcionWsfev1);
        		log.setResultado(resultado);
        		log.setErrMsg(errMsg);
        		log.setObs(obs);
        		
        		log.setXmlRequest(xmlRequest);
        		log.setXmlResponse(xmlResponse);
        		
        		log.setCae(cae);
        		log.setFechaVencimiento(fechaVencimiento);
        		/*-------------------------------------------------------*/
        		if (log.getFechaCbte()==null) {
        			log.setFechaCbte(fechaCbte);	
        		}
        		if (log.getImpTotal()==null) {
        			log.setImpTotal(impTotal);
        		}
        		if (log.getNroCbte()==null) {
        			log.setNroCbte(ultimoComprobante);	
        		}
            	/*---------------------*/
        		if (log.getCbte()==null) {
	       			String cbte = null;
	       			if (log.getTipoCbte()!=null)
	       				cbte = TipoComprobanteAFIP.findPref4Cod(log.getTipoCbte());
	       			if (log.getPtoVtaCbte()!=null)
	       				cbte += "-"+log.getPtoVtaCbte();
	       			if (log.getNroCbte()!=null)
	       				cbte += "-"+log.getNroCbte();
	       			log.setCbte(cbte);	
        		}
        		/*-------------------------------------------------------*/
        		
        		log.setFechaFinOp(new Date());
	        	
        		//Registro auditoria
	        	logTransaccionFacade.registrarAuditoria(log);
        	}
        	info.remove(messIN.getLogCorrelationID());
        } else {
        	MessageContext messIN = msgContext.getOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
        	SOAPBodyImpl body = (SOAPBodyImpl) messIN.getEnvelope().getBody().getDescendants(Boolean.TRUE).next();
        	OMElementImpl bodyEl = (OMElementImpl) body.getDescendants(Boolean.TRUE).next();
        	
        	String operationName = msgContext.getOperationContext().getOperationName();
			QName qName = new javax.xml.namespace.QName(nsURI, operationName);
        	OMElementImpl servicio = (OMElementImpl) bodyEl.getChildrenWithName(qName).next();
        	
        	String tipoCbte = null;
        	String ptoVtaCbte = null;
        	String nroCbte = null;
        	String cbte = null;
    		String fechaCbte = null;
    		String impTotal = null;
    		
    		qName = new javax.xml.namespace.QName(nsURI, "tipoCbte");
        	try {
            	OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
        		tipoCbte = el != null ? el.getText() : null;
        	} catch (NoSuchElementException e) {
        		//No tiene asociado
        	}
        	qName = new javax.xml.namespace.QName(nsURI, "ptoVta");
        	try {
        		OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
        		ptoVtaCbte = el != null ? el.getText() : null;
        		if (ptoVtaCbte != null) {
        			ptoVtaCbte = String.valueOf("00000").concat(ptoVtaCbte);
        			ptoVtaCbte = ptoVtaCbte.substring(ptoVtaCbte.length()-5);
        		}
        	} catch (NoSuchElementException e) {
        		//No tiene asociado
        	}
    		qName = new javax.xml.namespace.QName(nsURI, "nroCbte");
        	try {
        		OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
        		nroCbte = el != null ? el.getText() : null;
        		if (nroCbte != null) {
        			nroCbte = String.valueOf("00000000").concat(nroCbte);
        			nroCbte = nroCbte.substring(nroCbte.length()-8);
        		}
         	} catch (NoSuchElementException e) {
        		//No tiene asociado
        	}
        	if (nroCbte == null) {
        		qName = new javax.xml.namespace.QName(nsURI, "cbte");
            	try {
            		OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
            		nroCbte = el != null ? el.getText() : null;
            		if (nroCbte != null) {
            			nroCbte = String.valueOf("00000000").concat(nroCbte);
            			nroCbte = nroCbte.substring(nroCbte.length()-8);
            		}
             	} catch (NoSuchElementException e) {
            		//No tiene asociado
            	}
        	}
        	/*---------------------*/
   			String letra = TipoComprobanteAFIP.findPref4Cod(tipoCbte);
   			cbte = letra;
   			if (ptoVtaCbte!=null)
   				cbte += "-"+ptoVtaCbte;
   			if (nroCbte!=null)
   				cbte += "-"+nroCbte;
            /*--------------------*/
        	qName = new javax.xml.namespace.QName(nsURI, "fechaCbte");
        	try {
            	OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
            	fechaCbte = el != null ? el.getText() : null;
        	} catch (NoSuchElementException e) {
        		//No tiene asociado
        	}
        	qName = new javax.xml.namespace.QName(nsURI, "impTotal");
        	try {
            	OMElementImpl el = (OMElementImpl) servicio.getChildrenWithName(qName).next();
            	impTotal = el != null ? el.getText() : null;
        	} catch (NoSuchElementException e) {
        		//No tiene asociado
        	}

        	qName = new javax.xml.namespace.QName(nsURI, "ctx");
        	if(servicio.getChildrenWithName(qName).hasNext()){
        		OMElementImpl ctx = (OMElementImpl) servicio.getChildrenWithName(qName).next();
        		qName = new javax.xml.namespace.QName(nsURICtx, "nroTransaccion");
        		OMElementImpl nroTransaccion = (OMElementImpl) ctx.getChildrenWithName(qName).next();
        		qName = new javax.xml.namespace.QName(nsURICtx, "interfaz");
        		OMElementImpl interfaz = (OMElementImpl) ctx.getChildrenWithName(qName).next();
        		
        		LogTransaccionContenido log = new LogTransaccionContenido();

        		log.setCtxNroTransaccion(nroTransaccion.getText());
        		log.setCtxInterfaz(interfaz.getText());
        		String servicioName = MonederoUtiles.generarNombreServicio(msgContext.getAxisService().getParameterValue("ServiceClass").toString(), operationName);
        		log.setCtxServicio(servicioName);
        		log.setCtxFechaTransaccion(new Date());
        		log.setFechaInicioOp(new Date());
        		log.setOperacion(operationName);
        		log.setXmlEntrada(messIN.getEnvelope().toString());
        		
        		/*-------------------------------------------------------*/
        		log.setTipoCbte(tipoCbte);
        		log.setPtoVtaCbte(ptoVtaCbte);
        		log.setNroCbte(nroCbte);
        		log.setCbte(cbte);
        		log.setFechaCbte(fechaCbte);
        		log.setImpTotal(impTotal);
        		/*-------------------------------------------------------*/
        		
        		info.put(messIN.getLogCorrelationID(), log);
        	}
        }
        
        return InvocationResponse.CONTINUE;        
    }

    public void revoke(MessageContext msgContext) {
        log.info(msgContext.getEnvelope().toString());
    }

    public void setName(String name) {
        this.name = name;
    }

}
