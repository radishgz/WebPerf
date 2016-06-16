package webperf.Run;

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

	

	private final static QName _ExcludeViolation_QNAME = new QName("", "ExcludeViolation");

	public ExcludeViolation createExcludeViolation() {
		return new ExcludeViolation();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "ExcludeViolation")
	public JAXBElement<ExcludeViolation> createExcludeViolation(ExcludeViolation value) {
		// return new JAXBElement<Item>(_Item_QNAME, Item.class, null, value);
		return new JAXBElement<ExcludeViolation>(_ExcludeViolation_QNAME,
				ExcludeViolation.class, null, value);
	}
	
	
	private final static QName _ExcludeViolationList_QNAME = new QName("", "ExcludeViolationList");

	public ExcludeViolationList createExcludeViolationList() {
		return new ExcludeViolationList();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "ExcludeViolationList")
	public JAXBElement<ExcludeViolationList> createExcludeViolationList(ExcludeViolationList value) {
		// return new JAXBElement<Item>(_Item_QNAME, Item.class, null, value);
		return new JAXBElement<ExcludeViolationList>(_ExcludeViolationList_QNAME,
				ExcludeViolationList.class, null, value);
	}
	
	
}
