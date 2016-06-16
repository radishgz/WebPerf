package webperf.sonar.test;

import java.util.ArrayList;
import java.util.Properties;

import junit.framework.TestCase;
import webperf.Run.webPerfMain;
 
public class SonarTest extends TestCase {
	
	 
	public void testXz(){
		
		Properties p = System.getProperties();
		String s[]=
			{"C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER\\WebContent",
				"C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER"};
		webPerfMain  img=new webPerfMain();
		img.main(s);
		//img.check("C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER\\WebContent",
		//		"C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER\\WebContent");
			

	}
}
