package webperf.sonar.test;

 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

 

 


import webperf.Run.ExcludeViolationList;
import webperf.Run.ExcludeViolation;
import webperf.tools.XmlMarshaller;

public class ExcludeViolationListTest {

	 
	public void testReadExcludeExption() {
		ExcludeViolation e=new ExcludeViolation();
		e.setNodeText("nodeText");
		e.setRule("rule");
		e.setSource("souirce");
		e.setStartLine("10");
		e.setSubRule("sub");
		
		ExcludeViolationList l=new ExcludeViolationList();
		ArrayList<ExcludeViolation> violations=new ArrayList<ExcludeViolation> ();
		violations.add(e);
		ExcludeViolation f=new ExcludeViolation();
		f.setNodeText("nodeText");
		f.setRule("rule");
		f.setSource("souirce");
		f.setStartLine("10");
		f.setSubRule("sub");
		violations.add(f);
		l.setViolations(violations);
		try {
			String s=XmlMarshaller.marshal(l);
			System.out.println(s);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	 
	public void testRead() {
	 try {
		ExcludeViolationList list = ExcludeViolationList.readExcludeViolations();
		try {
			String s=XmlMarshaller.marshal(list);
			System.out.println(s);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
