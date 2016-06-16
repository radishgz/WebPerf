package webperf.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
 * XML序列化及反序列化
 * @author xiehq
 *
 */
public class XmlMarshaller {

 	private static final Logger LOG = LoggerFactory.getLogger(XmlMarshaller.class);

	/*
	 * static final JAXBContext context = initContext();
	 * 
	 * private static JAXBContext initContext() { return
	 * JAXBContext.newInstance(MyServlet.class.getClassLoader()); }
	 */

	public static void marshal(Object object, PrintWriter print)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(object.getClass());

		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// m.setProperty(Marshaller., true);
		m.setProperty(Marshaller.JAXB_ENCODING, "GBK");
		// StreamSource s=new StreamSource();
		StringWriter w = new StringWriter();
		//	javax.xml.stream.XMLStreamWriter w=new javax.xml.stream.XMLStreamWrite();
		// m.marshal(object, print);
		//m.marshal(arg0, arg1)
		m.marshal(object, w);
		m.marshal(object, w);

		String temp = w.getBuffer().toString();
		// temp = temp.replace("encoding=\"GBK\"", "encoding=\"utf-8\"");
		temp = temp.replace("standalone=\"yes\"", "");
		LOG.debug("marshal result:" + temp);
		print.print(temp);
	}

	public static String marshal(Object object) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(object.getClass());

		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// m.setProperty(Marshaller., true);
		m.setProperty(Marshaller.JAXB_ENCODING, "GBK");
		// StreamSource s=new StreamSource();
		StringWriter w = new StringWriter();
		// m.marshal(object, print);
		m.marshal(object, w);

		String temp = w.getBuffer().toString();
		// temp = temp.replace("encoding=\"GBK\"", "encoding=\"utf-8\"");
		temp = temp.replace("standalone=\"yes\"", "");
		//LOG.debug("marshal result:" + temp);
		return temp;
	}

	/*
	 * public static void marshalTypeList(ProjectTypeList object,PrintWriter
	 * print) throws JAXBException{ JAXBContext context =
	 * JAXBContext.newInstance(ProjectTypeList.class); Marshaller m =
	 * context.createMarshaller();
	 * m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	 * m.marshal(object,print); }
	 */
	/*
	 * public static Document DOM(String xml) { DocumentBuilderFactory f =
	 * DocumentBuilderFactory.newInstance(); InputStream input; try {
	 * DocumentBuilder p = f.newDocumentBuilder(); input = new
	 * ByteArrayInputStream(xml.getBytes()); // LOG.debug("XML:"+xml); Document
	 * t = p.parse(input); LOG.debug("doc:" + t.toString()); return
	 * p.parse(input); } catch (Exception e) { e.printStackTrace();
	 * LOG.error(e.getLocalizedMessage()); } return null; }
	 * 
	 * public static void SAX( String xml) { SAXParserFactory f =
	 * SAXParserFactory.newInstance(); InputStream input; try { SAXParser p =
	 * f.newSAXParser(); input = new ByteArrayInputStream(xml.getBytes());
	 * p.parse(input, new DefaultHandler()); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

	public static Object unmarshal(String packageName, String xml)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(packageName);

		Unmarshaller u = jc.createUnmarshaller();
		// u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// m.setProperty(Marshaller., true);
		//u.setProperty(Marshaller.JAXB_ENCODING,Marshaller.JAXB_ENCODING.);

		return u.unmarshal(new StreamSource(new StringReader(xml)));

		// (new StreamSourceDOM(xml).getFirstChild());// , Employee.class);
	}

	public static Object elementUnmarshal(String packageName, String xml)
			throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(packageName);

		Unmarshaller u = jc.createUnmarshaller();
		// u.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		// m.setProperty(Marshaller., true);
		// u.setProperty(Unmarshaller., "GBK");
		return ((JAXBElement) u.unmarshal(new StringReader(xml))).getValue();
		// return u.unmarshal(new StreamSource(new StringReader(xml)));

		// (new StreamSourceDOM(xml).getFirstChild());// , Employee.class);
	}

	
}
