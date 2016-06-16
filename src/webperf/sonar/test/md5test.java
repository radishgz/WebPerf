package webperf.sonar.test;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.digest.Md5Crypt;

import junit.framework.TestCase;

public class md5test extends TestCase {

	public void testXz() {
		String s = "werwerweuiqwueroiqueriquweriuqwoieruqweruqoieruqieurqewrerq";
		byte[] keyBytes = s.getBytes();
		String salt = "$1$qmgcr1zB";
		// new
		// StringBuilder().append("$1$").append(getRandomSalt(8)).toString();
		System.out.println(salt);

		for (int i = 0; i < 10; i++)
			System.out.println("i="+i+"--"+MD5(					s));

	}

	static String getRandomSalt(int num) {
		StringBuilder saltString = new StringBuilder();
		for (int i = 1; i <= num; i++) {
			saltString
					.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
							.charAt(new Random()
									.nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
											.length())));
		}
		return saltString.toString();
	}
	
	
	 public   static String MD5(String s) {
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
	        try {
	            byte[] btInput = s.getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	 }
}
