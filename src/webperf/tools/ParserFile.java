package webperf.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
 
import java.util.HashMap;
 
 


/**
 * ��HTML/JSP�ļ� תΪJSOUP DOCUMENT
 * @author xiehq
 *
 */
public class ParserFile {
	public static HashMap<String, String> fileExtension;

	public static String fileEncode="GBK";
	 
/*
	public static Document parser(String filename) throws FileNotFoundException, IOException {
 
			if (!AllowExtension(filename)){
				return null;
				}
			if (getExtension(filename).equals("jsp")){
				String conext=readFile(filename);
				if (conext!=null)
					return Jsoup.parse(excludeJspNode(filename,conext));
				
			}
				
			else{
				File input = new File(filename);
 
				return Jsoup.parse(input, fileEncode, "http://example.com/");
			}
			return null;
			 
		 
	}
	*/

	/**
	 * ��ȡ�ļ���׺
	 * 
	 * @param filename
	 *            �ļ���
	 * @return ȫСд���ļ���׺��û�з���""
	 */
	public static String getExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return "";
	}

	/**
	 * ����ļ���׺�Ƿ�ΪJSP/HTML,�Ƿ���true
	 * @param filename
	 * @return
	 */
	public static boolean AllowExtension(String filename) {
		if (fileExtension == null) {
			fileExtension = new HashMap<String, String>();
			fileExtension.put("jsp", "jsp file");
			fileExtension.put("html", "html file");
			fileExtension.put("htm", "htm file");
		}
		return fileExtension.containsKey(getExtension(filename));
	}

	
	/**
	 * ��ȡ�ļ����ݣ�����STRING
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile(String filename) throws FileNotFoundException,
			IOException {
		FileInputStream Html;
		byte Content[];
		Html = new FileInputStream(filename);
		Content = new byte[Html.available()];
		Html.read(Content, 0, Html.available());
		Html.close();
		return new String(Content, fileEncode);
	}
	
	/**
	 * ���ַ�����jsp���벿�� <% �� %>����ȥ��
	 * @param context Ҫ�ᳫ���ַ���
	 * @return
	 * @throws Exception ���<% %>��ƥ�䣬�׳��쳣
	 */
	public static  String excludeJspNode(String filename,String context) {
		int beginPos=context.indexOf("<%");
		//System.out.println(context);
		int endPos;
		StringBuffer buffer=new StringBuffer();
		int readBegin=0;
		//System.out.println(readBegin+","+ beginPos+",");
		while(beginPos>=0){
			
			endPos=context.indexOf("%>", beginPos);
			if (endPos <0) 
				{
					//imgErrorList.add(new ParserException(filename,"û���ҵ���Ӧ��jsp �������� %> :"+beginPos));
					return null;
					}
				 
			
			//System.out.println(readBegin+","+ beginPos+","+endPos);
			if (beginPos>0)
				buffer.append((context.substring(readBegin, beginPos)));
			readBegin=endPos+2;
			
			beginPos=context.indexOf("<%",readBegin);
		}
		buffer.append(context.substring(readBegin));
		//System.out.println("buffer____:"+buffer.toString()+"buffer____");
		return buffer.toString();
	}

}
