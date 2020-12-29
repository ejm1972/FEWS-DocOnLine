package ar.com.coninf.doconline.shared.util;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Mario Cares
 */
public class StringXML2StringData {

	private Logger logger = Logger.getLogger(this.getClass());
	
	public ArrayList<String> parsearXML(String xml) {
		
		readXML2(xml);
		
		ArrayList<String> datos = new ArrayList<String>();
		Document doc;
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			
			datos.add("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) {
				printNote(doc.getChildNodes(), datos);
			}

		} catch (Exception e) {
			datos.add(e.toString()); //En caso de algun error
		}

		return datos;
	}

	private void printNote(NodeList nodeList, ArrayList<String> datos) {
		
		try {
			
			for (int count = 0; count < nodeList.getLength(); count++) {
				Node tempNode = nodeList.item(count);

				// make sure it's element node.
				if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

					// get node name and value
					datos.add("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
					datos.add("Node Value =" + tempNode.getTextContent());

					if (tempNode.hasAttributes()) {

						// get attributes names and values
						NamedNodeMap nodeMap = tempNode.getAttributes();

						for (int i = 0; i < nodeMap.getLength(); i++) {
							Node node = nodeMap.item(i);
							datos.add("attr name : " + node.getNodeName());
							datos.add("attr value : " + node.getNodeValue());
						}

					}

					if (tempNode.hasChildNodes()) {
						// loop again if has child nodes
						printNote(tempNode.getChildNodes(), datos);
					}

					datos.add("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
				}
			}
			
		} catch (Exception e) {
			datos.add(e.toString()); //En caso de algun error
		}

	}

	public String extraerTagXML(String xml, String tag) {
		
		String valorTag = "";
		Document doc;
		
		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			valorTag = doc.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
		} catch (Exception e) {
			valorTag = e.toString(); //En caso de algun error
		}

		return valorTag;
	}

	public void readXML2(String xml) {

		try {
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			logger.debug("Root element :" + doc.getDocumentElement().getNodeName());

			if (doc.hasChildNodes()) {

				printNote2(doc.getChildNodes());

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void printNote2(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				logger.debug("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				logger.debug("Node Value =" + tempNode.getTextContent());

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						Node node = nodeMap.item(i);
						logger.debug("attr name : " + node.getNodeName());
						logger.debug("attr value : " + node.getNodeValue());

					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					printNote2(tempNode.getChildNodes());

				}

				logger.debug("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}
		}
	}

	public String readXML3(String xml) {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			if (doc.hasChildNodes()) {
				return getStringFromDocument(doc);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	/**
	* Metodo de utilidad que transforma un Document en un String
	*
	* @param doc
	*            Documento XML
	* @return String con el XML
	*/
	public String getStringFromDocument(Document doc)
	{
//		try {
//			OutputFormat format = new OutputFormat(doc);
//			
//			format.setLineWidth(65);
//			format.setIndenting(true);
//			format.setIndent(5);
//
//			StringWriter writer = new StringWriter();
//			XMLSerializer serializer = new XMLSerializer(writer, format);
//			serializer.serialize(doc);
//		
//			return writer.toString();
//		
//		} catch (IOException e) {
			return null;
//		}
	}
	
}	

//            // respuesta1 = doc.getDocumentElement().getNodeName()+" - "; Con esto se obtiene la raiz primaria.
//            // en este caso, devuelve Info_Receta
//            //<Datos_Receta nombre="Torta Chocolate" tipo="1" tiempo="2:00" />
//            datos.add(doc.getElementsByTagName("Datos_Receta").item(0).getAttributes().getNamedItem("nombre").getNodeValue());
//            datos.add(doc.getElementsByTagName("Datos_Receta").item(0).getAttributes().getNamedItem("tipo").getNodeValue());
//            datos.add(doc.getElementsByTagName("Datos_Receta").item(0).getAttributes().getNamedItem("tiempo").getNodeValue());
//            
//            //<Preparacion_Receta>Se mezcla tal cosa, luego se junta con esta otra. Se hornea y listo</Preparacion_Receta>
//            datos.add(doc.getElementsByTagName("Preparacion_Receta").item(0).getChildNodes().item(0).getNodeValue());
//            
//            //<Ingredientes_Receta>
//            //  <Ingrediente cantidad="130" Umedida="Gramos" producto="Harina" /> y siguientes. Siguen la misma
//             // estructura, solo cambia el item(i) por el siguiente y asi tantas veces como ingredientes tenga.
//            for (int i = 0; i < doc.getElementsByTagName("Ingrediente").getLength(); i++) {
//                datos.add(doc.getElementsByTagName("Ingrediente").item(i).getAttributes().getNamedItem("cantidad").getNodeValue());
//                datos.add(doc.getElementsByTagName("Ingrediente").item(i).getAttributes().getNamedItem("Umedida").getNodeValue());
//                datos.add(doc.getElementsByTagName("Ingrediente").item(i).getAttributes().getNamedItem("producto").getNodeValue());
//            }

/*
Transformer transformer = TransformerFactory.newInstance().newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//initialize StreamResult with File object to save to file
StreamResult result = new StreamResult(new StringWriter());
DOMSource source = new DOMSource(doc);
transformer.transform(source, result);
String xmlString = result.getWriter().toString();
logger.debug(xmlString);
*/