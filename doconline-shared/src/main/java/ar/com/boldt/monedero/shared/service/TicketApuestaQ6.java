package ar.com.boldt.monedero.shared.service;


import java.awt.Color;
import java.io.FileNotFoundException;

import org.jbars.Barcode;
import org.jbars.Barcode93;

import ar.com.coninf.doconline.rest.model.request.RequestMail;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class TicketApuestaQ6 extends TicketApuesta {

	private static final long serialVersionUID = 1L;

	public TicketApuestaQ6() throws FileNotFoundException, DocumentException {
		super();
	}

	public TicketApuestaQ6(Document document, PdfWriter documentWriter) {
		super(document, documentWriter);
	}

	@Override
	public void generoPDF(RequestMail request, String logosJuegos) {

		RequestMail req = (RequestMail) request;
		
		try
        {

        	doc.open();

        	PdfPTable tableDoc= new PdfPTable(1);
        	PdfPCell cellDoc= new PdfPCell();
        	Image img;
        	PdfPCell cell;
        	PdfPTable table;
        	Paragraph p;
        	
        	tableDoc.setWidthPercentage((float) 60);
        	
        	table = new PdfPTable(2);
        	table.setWidths(new int[]{5,15});
        	table.setWidthPercentage((float) 90);
        	
        	img = Image.getInstance(String.format(logosJuegos+"%s.jpg", req.getText()));
            img.scaleToFit(100, 60);
            img.setAlignment(Image.LEFT);

            cell = new PdfPCell(img);
        	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        	cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        	cell.setColspan(1);
        	cell.disableBorderSide(PdfPCell.LEFT);
        	cell.disableBorderSide(PdfPCell.RIGHT);
        	cell.disableBorderSide(PdfPCell.TOP);
        	cell.disableBorderSide(PdfPCell.BOTTOM);
        	table.addCell(cell);

        	cell = new PdfPCell();

        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(req.getText(), new Font(Font.COURIER, 9, Font.BOLD)));
        	cell.addElement(p);
       		
        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(req.getText(), new Font(Font.COURIER, 14, Font.BOLD)));
        	cell.addElement(p);

        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);

        	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        	cell.setColspan(2);
        	cell.disableBorderSide(PdfPCell.LEFT);
        	cell.disableBorderSide(PdfPCell.RIGHT);
        	cell.disableBorderSide(PdfPCell.TOP);
        	cell.disableBorderSide(PdfPCell.BOTTOM);
        	table.addCell(cell);
        	
        	cellDoc.addElement(table);

        	p = new Paragraph(" ");
        	cellDoc.addElement(p);

        	String apuesta = ""; //req.getText()[0];
//        	for (int n=1; n<req.getText().length; n++) {
//        		apuesta += (" "+req.getText()[n]); 
//        	}
        	
        	table = new PdfPTable(1);
        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(apuesta, new Font(Font.COURIER, 14, Font.BOLD)));
        	cell = new PdfPCell();
        	cell.setPaddingBottom(10);
        	cell.addElement(p);
        	table.addCell(cell);
        	cellDoc.addElement(table);
        	
        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase("Total Apostado: "+req.getText()+" $"+req.getText(), new Font(Font.COURIER, 9)));
        	cellDoc.addElement(p);

        	p = new Paragraph(" ");
        	cellDoc.addElement(p);

        	table = new PdfPTable(1);
        	cell = new PdfPCell();
        	cell.disableBorderSide(PdfPCell.LEFT);
        	cell.disableBorderSide(PdfPCell.RIGHT);
        	cell.disableBorderSide(PdfPCell.TOP);
        	cell.disableBorderSide(PdfPCell.BOTTOM);

        	p = new Paragraph();
        	p.add(new Phrase("Sorteo: "+req.getText()+" Fecha: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
        	
        	p = new Paragraph();
        	p.add(new Phrase("Si gana, cobrese hasta el: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
         	
        	p = new Paragraph();
        	p.add(new Phrase("Numero Ticket: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
         	
        	p = new Paragraph();
        	p.add(new Phrase("Secuencia: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
        	
        	p = new Paragraph();
        	p.add(new Phrase("Integridad: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
        	
        	p = new Paragraph();
        	p.add(new Phrase("Fecha de realización: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);
        	
        	p = new Paragraph();
        	p.add(new Phrase("Hora de realización: "+req.getText(), new Font(Font.COURIER, 9)));
        	cell.addElement(p);

        	table.addCell(cell);
        	cellDoc.addElement(table);

        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(req.getText()+" "+req.getText(), new Font(Font.COURIER, 9)));
        	cellDoc.addElement(p);
        	
        	p = new Paragraph(" ");
        	cellDoc.addElement(p);

        	Barcode93 code93 = new Barcode93();
        	code93.setCodeType(Barcode.CODE93);
        	code93.setCode(req.getText());
        	code93.setFontSize(15);
        	byte[] img93byte = code93.createJPG(80, Color.BLACK, Color.BLACK, 8, (double) 0);
        	img = Image.getInstance(img93byte);
            img.scaleToFit(100, 50);
        	img.setAlignment(Element.ALIGN_CENTER);
        	cellDoc.addElement(img);
        	
        	if (req.getText()==null) {
        		req.setText("Jugar tiene que ser divertido. No lo convierta en adiccion.");
        	}
        	p = new Paragraph();
        	p.setAlignment(Paragraph.ALIGN_CENTER);
        	p.add(new Phrase(req.getText(), new Font(Font.COURIER, 9)));
        	cellDoc.addElement(p);

        	p = new Paragraph(" ");
        	cellDoc.addElement(p);

        	tableDoc.addCell(cellDoc);
        	doc.add(tableDoc);
        	
        	doc.close();
        	docWriter.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}

}