package webperf.sonar.test;

import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import webperf.tools.JsVersionConf;
import webperf.tools.JsVersionList;
import webperf.tools.XmlMarshaller;
import junit.framework.TestCase;

public class MarshallTest extends TestCase {

	public void testGetWebRoot() {
 		JsVersionList jslist;
		try {
			jslist = JsVersionList.readJsVersionList();
			/*jslist.setWebRoot("ss");
			 
			ArrayList<JsVersionConf> list = jslist.getJsVresions();
			JsVersionConf f=new JsVersionConf();
			f.setJsName("jsName");
			f.setMd5("dd");
			list.add(f);
			*/
			String newContent=XmlMarshaller.marshal(jslist);
			System.err.println(newContent);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
