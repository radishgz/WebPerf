package webperf.sonar.test;

public class javaMatchTest {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  String likeType = "23";
		  String pattern = "([\\w\\W]*)ml([\\w\\W]*)";
		  String sourceStr = "test.Œ“≥‘htmlww";
		     System.out.println(sourceStr.matches(pattern)); 
		     String s="LongJavaScriptCheck";
		     String t="LongJavaScriptCheck";
		     System.out.println(s.matches(t)); 
		     
		     /*
		     \\W*--test.html
		     LongJavaScriptCheck--LongJavaScriptCheck
		     \\W*--USE_STYLE
		     \\W*--147
		     \\W*--&lt;style&gt;
		     */
		     
	}
}
