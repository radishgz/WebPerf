package webperf.sonar.test;

import java.util.Properties;

import webperf.Run.webPerfMain;
import junit.framework.TestCase;

public class SonarMainTest extends TestCase {

	public void testMain() {
		Properties p = System.getProperties();
		//String s[]=
		//	{"D:\\crm_web",
		//		"D:\\crm_web"};
		
		String s[]={"/Users/xiehuiqiang/Downloads/baidu-mywork/SOUTH-MANAGER/WebContent",
		"/Users/xiehuiqiang/Downloads/baidu-mywork/SOUTH-MANAGER/"};
		
		try{
		webPerfMain  img=new webPerfMain();
		img.main(s);
		}catch(Exception e){
			e.printStackTrace();
		}catch (UnsupportedClassVersionError e2){
			System.err.println(e2.getLocalizedMessage());
			e2.printStackTrace();
		}
		//img.check("C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER\\WebContent",
		//		"C:\\mywork\\SOUTH-WORKSPACE\\SOUTH-MANAGER\\WebContent");
	}

}
