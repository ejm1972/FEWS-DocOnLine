package ar.com.boldt.monedero.shared.service;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import ar.com.coninf.doconline.rest.model.request.RequestMail;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

public abstract class TicketApuesta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected PdfWriter docWriter;
	protected Document doc;
	
	public TicketApuesta() throws FileNotFoundException, DocumentException {
		doc = new Document();
    	docWriter = PdfWriter.getInstance(doc, new FileOutputStream("/soft/cupon.pdf"));
	}
	
	public TicketApuesta(Document document, PdfWriter documentWriter) {
		docWriter = documentWriter;
		doc = document;
	}

	public abstract void generoPDF(RequestMail req, String logosJuegos);
	
	public PdfWriter getDocWriter() {
		return docWriter;
	}
	public void setDocWriter(PdfWriter docWriter) {
		this.docWriter = docWriter;
	}

	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
}