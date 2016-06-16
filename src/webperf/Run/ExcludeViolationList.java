package webperf.Run;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringEscapeUtils;
import org.sonar.plugins.web.checks.WebViolation;

import webperf.tools.Configure;
import webperf.tools.XmlMarshaller;

@XmlRootElement(name = "EXCELUDE_VIOLATIONS")
@XmlAccessorType(XmlAccessType.NONE)
public class ExcludeViolationList {

	@XmlElement(name = "VIOLATION")
	ArrayList<ExcludeViolation> violations = new ArrayList<ExcludeViolation>();

	public ArrayList<ExcludeViolation> getViolations() {
		return violations;
	}

	public void setViolations(ArrayList<ExcludeViolation> violations) {
		this.violations = violations;
	}

	public static String fileEncode = "GBK";

	/**
	 * 读取检查例外配置，并返回ExcludeException类
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ExcludeViolationList readExcludeViolations()
			throws FileNotFoundException, IOException {

		try {

			String filenameOnClasspath = Configure.getConfig()
					.getExcludeViolations();
			if (filenameOnClasspath == null
					|| (filenameOnClasspath.trim().length() == 0))
				filenameOnClasspath = "EmptyExcludeViolations.xml";
			InputStreamReader input = new InputStreamReader(
					ExcludeViolation.class.getClassLoader()
							.getResourceAsStream(filenameOnClasspath));
			BufferedReader in = new BufferedReader((input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			try {
				while ((line = in.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			ExcludeViolationList inst = (ExcludeViolationList) XmlMarshaller
					.unmarshal(ExcludeViolationList.class.getPackage()
							.getName(), buffer.toString());
			// debug
			if (in != null) {
				in.close();
			}
			return inst;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public ExcludeViolationList() {
		super();
	}

	public boolean isExcludeViolation(WebViolation vio) {
		// boolean ret=false;
		for (ExcludeViolation exclude : violations) {

			String nodeText = "";
			// StringEscapeUtils.escapeXml(
			if (vio.getNode() != null)
				nodeText = vio.getNode().getCode();

			boolean b1 = vio.getResource().getPath().matches(
					exclude.getSource());

			// System.err.println(exclude.getSource()+"--"+vio.getResource().getPath());

			boolean b2 = vio.getRule().getKey().matches(exclude.getRule());
			// System.err.println(exclude.getRule()+"--"+vio.getRule().getKey());

			boolean b3 = vio.getSubRuleId().matches(exclude.getSubRule());

			// System.err.println(exclude.getSubRule()+"--"+vio.getSubRuleId());

			boolean b4 = String.valueOf(vio.getLineId()).matches(
					exclude.getStartLine());

			// System.err.println(exclude.getStartLine()+"--"+vio.getLineId());

			// System.err.println(exclude.getNodeText()+"--"+nodeText);

			// if (nodeText.)
			boolean b5 = nodeText.matches(exclude.getNodeText());
			// System.err.println("ret"+b1+b2+b3+b4+b5);
			if (b1 && b2 && b3 && b4 && b5) {
				return true;
			}
		}
		return false;
	}

	public static ExcludeViolationList readExcludeViolations(
			InputStreamReader input) throws FileNotFoundException, Exception {

		BufferedReader in = new BufferedReader((input));
		StringBuffer buffer = new StringBuffer();
		String line = "";

		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}

		 
			ExcludeViolationList inst = (ExcludeViolationList) XmlMarshaller
					.unmarshal(ExcludeViolationList.class.getPackage().getName(),
							buffer.toString());
		 
		// debug
		if (in != null) {
			in.close();
		}
		return inst;

	}

}
