package org.sonar.plugins.web.checks;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

 

/**
 * XML MARSHALLπ§≥ß¿‡
 * @author xiehq
 *
 */
@XmlRegistry
public class ObjectFactory {

	
	public ObjectFactory() {
	}

	

	private final static QName _WebViolation_QNAME = new QName("", "WebViolation");

	public WebViolation createWebViolation() {
		return new WebViolation();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "WebViolation")
	public JAXBElement<WebViolation> createWebViolation(WebViolation value) {
		// return new JAXBElement<Item>(_Item_QNAME, Item.class, null, value);
		return new JAXBElement<WebViolation>(_WebViolation_QNAME,
				WebViolation.class, null, value);
	}
	
	 
	
}
