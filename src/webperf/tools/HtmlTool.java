package webperf.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * �ṩһЩhtml/STR��������
 * 
 * @author xiehq
 * 
 */
public class HtmlTool {

	/**
	 * ��STR��HTML�����ַ�����ת�룬��ո��Ϊ&nbsp;
	 * 
	 * @param s
	 *            Ҫת�����ַ���
	 * @return String ת������ַ���
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
			case '��':
				sb.append("&agrave;");
				break;
			case '��':
				sb.append("&eacute;");
				break;
			case '��':
				sb.append("&egrave;");
				break;
			case '��':
				sb.append("&ecirc;");
				break;
			case '��':
				sb.append("&ugrave;");
				break;
			case '��':
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
	 * ��STR��ת������ݻ���HTML����&nbsp��Ϊ�ո�;
	 * 
	 * @param s
	 *            Ҫת�����ַ���
	 * @return String ת������ַ���
	 * @see escapeHTML
	 */
	public static final String restoreHTML(String s) {

		String temp = strRep(s, "&lt;", "<");
		temp = strRep(temp, "&gt;", ">");
		temp = strRep(temp, "&amp;", "&");
		temp = strRep(temp, "&quot;", "\"");
		temp = strRep(temp, "&agrave;", "��");
		temp = strRep(temp, "&egrave;", "��");
		temp = strRep(temp, "&ecirc;", "��");
		temp = strRep(temp, "&eacute;", "��");
		temp = strRep(temp, "&ugrave;", "��");
		temp = strRep(temp, "&uuml;", "��");
		temp = strRep(temp, "&nbsp;", " ");
		return temp;
	}

	/**
	 * ��ָ�����ַ��������滻.
	 * 
	 * @param str
	 *            �����滻�������ַ���
	 * @param source
	 *            Ҫ���滻�����ַ���
	 * @param target
	 *            �滻source���ַ���
	 * @return String ��target�滻������source��str
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
	 * ����rootĿ¼��Ӧ��filename�����·������ȥ��filename��root��ͷ�Ĳ���
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
	 * ����rootĿ¼��Ӧ��file�����·������ȥ��file��root��ͷ�Ĳ���
	 * �����ȡROOT��file��getCanonicalPathʧ�ܣ�����null
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
