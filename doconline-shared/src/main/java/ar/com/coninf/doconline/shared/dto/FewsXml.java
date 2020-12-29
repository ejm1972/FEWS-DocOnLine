package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;
import ar.com.coninf.doconline.shared.util.StringXML2StringData;

@Entity
@Table(name = "dbo.fews_xml")
public class FewsXml implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;
	
	@InsertOptional
	@Column(name = "xml_request", columnDefinition = "text")
	private char[] xmlRequest;

	@InsertOptional
	@Column(name = "xml_response", columnDefinition = "text")
	private char[] xmlResponse;
    
	@InsertOptional
	@Column(name = "codigo", nullable = true)
	private String codigo;

	@InsertOptional
	@Column(name = "descripcion", nullable = true)
	private String descripcion;
	
	@InsertOptional
	@Column(name = "observacion", nullable = true)
	private String observacion;

	@InsertOptional
	@Column(name = "excepcion_wsaa", nullable = true)
	private String excepcionWsaa;

	@InsertOptional
	@Column(name = "excepcion_wsfev1", nullable = true)
	private String excepcionWsfev1;
	
	@Transient
	private char[] xmlRequestAfip;
	@Transient
	private char[] xmlResponseAfip;

	@Transient
	private String txtRequest;
	@Transient
	private String txtResponse;
	@Transient
	private String txtRequestAfip;
	@Transient
	private String txtResponseAfip;
	
	public Long getId() {
		return id;
	}
	public void setId(Long integer) {
		this.id = integer;
	}
	public void setId(Integer id) {
		this.id = id.longValue();
	}

	public char[] getXmlRequest(){
		return xmlRequest;
	}
	public void setXmlRequest(char[] xmlRequest){
		this.xmlRequest = xmlRequest;
		
		String xml = String.valueOf(xmlRequest);
		StringXML2StringData xml2Str = new StringXML2StringData();
		setTxtRequest(xml2Str.readXML3(xml));
	}
	
	public char[] getXmlResponse(){
		return xmlResponse;
	}
	public void setXmlResponse(char[] xmlResponse){
		this.xmlResponse = xmlResponse;
		
		String xml = String.valueOf(xmlResponse);
		StringXML2StringData xml2Str = new StringXML2StringData();
		setTxtResponse(xml2Str.readXML3(xml));
	}
	
	public char[] getXmlRequestAfip() {
		return xmlRequestAfip;
	}
	public void setXmlRequestAfip(char[] xmlRequestAfip) {
		this.xmlRequestAfip = xmlRequestAfip;

		String xml = String.valueOf(xmlRequestAfip);
		StringXML2StringData xml2Str = new StringXML2StringData();
		setTxtRequest(xml2Str.readXML3(xml));
	}
	
	public char[] getXmlResponseAfip() {
		return xmlResponseAfip;
	}
	public void setXmlResponseAfip(char[] xmlResponseAfip) {
		this.xmlResponseAfip = xmlResponseAfip;

		String xml = String.valueOf(xmlResponseAfip);
		StringXML2StringData xml2Str = new StringXML2StringData();
		setTxtResponse(xml2Str.readXML3(xml));
	}
	
	public String getTxtRequest() {
		return txtRequest;
	}
	public void setTxtRequest(String txtRequest) {
		this.txtRequest = txtRequest;
	}
	
	public String getTxtResponse() {
		return txtResponse;
	}
	public void setTxtResponse(String txtResponse) {
		this.txtResponse = txtResponse;
	}
	
	public String getTxtRequestAfip() {
		return txtRequestAfip;
	}
	public void setTxtRequestAfip(String txtRequestAfip) {
		this.txtRequestAfip = txtRequestAfip;
	}

	public String getTxtResponseAfip() {
		return txtResponseAfip;
	}
	public void setTxtResponseAfip(String txtResponseAfip) {
		this.txtResponseAfip = txtResponseAfip;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo.toString();
		
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getExcepcionWsaa() {
		return excepcionWsaa;
	}
	public void setExcepcionWsaa(String excepcion_wsaa) {
		this.excepcionWsaa = excepcion_wsaa;
	}
	
	public String getExcepcionWsfev1() {
		return excepcionWsfev1;
	}
	public void setExcepcionWsfev1(String excepcion_wsfev1) {
		this.excepcionWsfev1 = excepcion_wsfev1;
	}
	public void setXmlRequest(String str) {
		// TODO Auto-generated method stub
		
		this.xmlRequest = str.toCharArray();
		
	}
	public void setXmlResponse(String str) {
		// TODO Auto-generated method stub
		
		this.xmlResponse = str.toCharArray();
		
	}

}
