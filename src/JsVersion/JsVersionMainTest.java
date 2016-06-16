package JsVersion;

import java.util.Properties;

 import junit.framework.TestCase;

public class JsVersionMainTest extends TestCase {

	public void testMain() {
		Properties p = System.getProperties();
		String s[]=
			{ "D:\\crm_web\\bce\\demo","D:\\crm_web"};
		try{
			JavaVersionMain  img=new JavaVersionMain();
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
