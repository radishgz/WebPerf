package webperf.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 提供一些html/STR处理函数。
 * 
 * @author xiehq
 * 
 */
public class HtmlTool {

	/**
	 * 将STR中HTML特殊字符进行转码，如空格变为&nbsp;
	 * 
	 * @param s
	 *            要转换的字符串
	 * @return String 转换后的字符串
	 */
	public static final String escapeHTML(String s) {

		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&quot;");
				break;
			case 'à':
				sb.append("&agrave;");
				break;
			case 'é':
				sb.append("&eacute;");
				break;
			case 'è':
				sb.append("&egrave;");
				break;
			case 'ê':
				sb.append("&ecirc;");
				break;
			case 'ù':
				sb.append("&ugrave;");
				break;
			case 'ü':
				sb.append("&uuml;");
				break;
			case ' ':
				sb.append("&nbsp;");
				break;

			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 将STR中转码的内容换成HTML，如&nbsp变为空格;
	 * 
	 * @param s
	 *            要转换的字符串
	 * @return String 转换后的字符串
	 * @see escapeHTML
	 */
	public static final String restoreHTML(String s) {

		String temp = strRep(s, "&lt;", "<");
		temp = strRep(temp, "&gt;", ">");
		temp = strRep(temp, "&amp;", "&");
		temp = strRep(temp, "&quot;", "\"");
		temp = strRep(temp, "&agrave;", "à");
		temp = strRep(temp, "&egrave;", "è");
		temp = strRep(temp, "&ecirc;", "ê");
		temp = strRep(temp, "&eacute;", "é");
		temp = strRep(temp, "&ugrave;", "ù");
		temp = strRep(temp, "&uuml;", "ü");
		temp = strRep(temp, "&nbsp;", " ");
		return temp;
	}

	/**
	 * 对指定的字符串进行替换.
	 * 
	 * @param str
	 *            进行替换操作的字符串
	 * @param source
	 *            要被替换的子字符串
	 * @param target
	 *            替换source的字符串
	 * @return String 用target替换了所有source的str
	 */
	// *********************************************************************
	static public String strRep(String str, String source, String target) {

		int sourceLength;
		int fromIndex = 0;
		int findPos = 0;
		if (target == null)
			target = " ";
		sourceLength = source.length();
		findPos = str.indexOf(source, fromIndex);
		while (findPos >= 0) {
			str = str.substring(0, findPos) + target
					+ str.substring(findPos + sourceLength);

			fromIndex = findPos + target.length();
			findPos = str.indexOf(source, fromIndex);
		}
		// System.out.println(findPos); }
		return str;

	}

	/**
	 * 返回root目录对应的filename的相对路径，即去掉filename以root开头的部门
	 * 
	 * @param root
	 * @param filename
	 * @return
	 */
	static public String getAbsPath(String root, String filename) {
		if (filename.startsWith(root)) {
			return filename.substring(root.length());
		}
		return filename;

	}

	/**
	 * 返回root目录对应的file的相对路径，即去掉file以root开头的部门
	 * 如果获取ROOT或file的getCanonicalPath失败，返回null
	 * 
	 * @param root
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	static public String getAbsPath(File root, File file) throws IOException {
		String rootname;
		String filename;

		rootname = root.getCanonicalPath();
		filename = file.getCanonicalPath();

		if (filename.startsWith(rootname)) {
			return filename.substring(rootname.length());
		}
		return filename;

	}

	final static String contextpath = "<%=request.getcontextpath()%>";

	public static String SpecialfileName(String name) {
		return removeJspTag(name);

	}

	final static String jspStartTag = "<%";
	final static String jspEndTag = "%>";

	public static String removeJspTag(String text) {
		return removeContent(text, jspStartTag, jspEndTag);
	}

	public static String removeContent(String text, String startTag,
			String endTag) {
		int startPos = text.indexOf(startTag);
		int endPos = 0 - endTag.length();
		StringBuffer ret = new StringBuffer("");
		while (startPos >= 0) {
			ret.append(text.substring(endPos + endTag.length(), startPos));
			endPos = text.indexOf(endTag, startPos);
			startPos = text.indexOf(startTag, endPos + endTag.length());

		}
		if (endPos < 0)
			return text;
		ret.append(text.substring(endPos + endTag.length()));
		return ret.toString();
	}

	public static StringBuffer readFileAndRemoveEmptyLine(File file)
			throws IOException {
		FileReader input = new FileReader(file);
		BufferedReader in = new BufferedReader((input));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				if (line.trim().length() > 0)
					buffer.append(line + "\n");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				in.close();
			} catch (Exception e) {

			}
			try {
				input.close();
			} catch (Exception e) {

			}

		}
		return buffer;
	}
}
