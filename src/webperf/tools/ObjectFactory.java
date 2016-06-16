package webperf.tools;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

 

/**
 * 
 * xml 序列/反序列化 Factory类，不直接使用
 * @author xiehq
 *
 */
@XmlRegistry
public class ObjectFactory {

	
	public ObjectFactory() {
	}

	private final static QName _Configure_QNAME = new QName("", "Configure");

	public Configure createConfigure() {
		return new Configure();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "Configure")
	public JAXBElement<Configure> createProjectTypeList(Configure value) {
		return new JAXBElement<Configure>(_Configure_QNAME,
				Configure.class, null, value);
	}
	
	
	private final static QName _JsVersionConf_QNAME = new QName("", "JsVersionConf");

	public JsVersionConf createJsVersionConf() {
		return new JsVersionConf();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "JsVersionConf")
	public JAXBElement<JsVersionConf> createJsVersionConf(JsVersionConf value) {
		return new JAXBElement<JsVersionConf>(_JsVersionConf_QNAME,
				JsVersionConf.class, null, value);
	}
	
	
	private final static QName _JsVersionList_QNAME = new QName("", "JsVersionList");

	public JsVersionList createJsVersionList() {
		return new JsVersionList();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "", name = "JsVersionList")
	public JAXBElement<JsVersionList> createJsVersionList(JsVersionList value) {
		return new JAXBElement<JsVersionList>(_JsVersionList_QNAME,
				JsVersionList.class, null, value);
	}
	
}
