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
	            // ���MD5ժҪ�㷨�� MessageDigest ����
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // ʹ��ָ�����ֽڸ���ժҪ
	            mdInst.update(btInput);
	            // �������
	            byte[] md = mdInst.digest();
	            // ������ת����ʮ�����Ƶ��ַ�����ʽ
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
