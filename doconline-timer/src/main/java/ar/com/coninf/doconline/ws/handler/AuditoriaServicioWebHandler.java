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

package ar.com.coninf.doconline.ws.handler;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.HandlerDescription;
import org.apache.axis2.engine.Handler;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AuditoriaServicioWebHandler extends AbstractHandler implements Handler {
	private static final Log log = LogFactory.getLog(AuditoriaServicioWebHandler.class);
    private String name;
	private final String nsURI = "http://impl.ws.doconline.coninf.com.ar";
    private final String nsURICtx = "http://tx.model.rest.doconline.coninf.com.ar/xsd";
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
	@Override
	public void init(HandlerDescription handlerdesc) {
		super.init(handlerdesc);
		ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[]{"ctx/application-context.xml"}, true);
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
        }
        
        return InvocationResponse.CONTINUE;        
    }

    public void revoke(MessageContext msgContext) {
        log.info(msgContext.getEnvelope().toString());
    }

}
