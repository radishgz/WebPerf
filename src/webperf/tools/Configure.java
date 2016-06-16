package webperf.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 
/**
 * 配置文件对应CLASS
 * 
 * @author xiehq
 * 
 */
@XmlRootElement(name = "Configure")
@XmlAccessorType(XmlAccessType.NONE)
public class Configure {

	private static final Logger LOG = LoggerFactory.getLogger(Configure.class);

	/**
	 * 结果输出目录，主要必须以\(WINDOWS)或者/(UNIX)结束
	 */
	@XmlElement(name="OutPutDir")
	private String outputDir;
	
	/**
	 * 运行规则配置XML
	 */
	@XmlElement(name="CheckRule")
	private String CheckRule="CheckRule.xml";
	
	
	
	@XmlElement(name="ExcludeDir")
	private ArrayList<String>  ExcludeDir=new ArrayList<String>();
	
	
	@XmlElement(name="ExcludeFile")
	private ArrayList<String>  ExcludeFiles=new ArrayList<String>();
	
	
	  
	public ArrayList<String> getExcludeDir() {
		return ExcludeDir;
	}

	public void setExcludeDir(ArrayList<String> excludeDir) {
		ExcludeDir = excludeDir;
	}

	public ArrayList<String> getExcludeFiles() {
		return ExcludeFiles;
	}

	public void setExcludeFiles(ArrayList<String> excludeFiles) {
		ExcludeFiles = excludeFiles;
	}

	public String getCheckRule() {
		return CheckRule;
	}

	public void setCheckRule(String checkRule) {
		CheckRule = checkRule;
	}

	public String getExcludeViolations() {
		return ExcludeViolations;
	}

	public void setExcludeViolations(String excludeViolations) {
		ExcludeViolations = excludeViolations;
	}

	/**
	 * 允许内嵌的SCRIPT最行数
	 */
	@XmlElement(name="ExcludeViolations")
	private String ExcludeViolations="ExcludeViolations.xml";
	
	 
	public Configure() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	 	/**
	 * 读取配置文件 WebPerfConfigure.xml
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Configure readConfigure() throws FileNotFoundException,
			IOException {
		try {

			String filenameOnClasspath = "WebPerfConfigure.xml";
			InputStreamReader input = new InputStreamReader(Configure.class
					.getClassLoader().getResourceAsStream(filenameOnClasspath));
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
			Configure inst = (Configure) XmlMarshaller.elementUnmarshal(
					Configure.class.getPackage().getName(), buffer.toString());
			return inst;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	
	private static Configure configure;
	
	public static Configure getConfig(){
		if (configure==null)
			try {
				configure=Configure.readConfigure();
			} catch (FileNotFoundException e) {
				LOG.error(e.getLocalizedMessage(),e);
				System.exit(-1);
			} catch (IOException e) {
				LOG.error(e.getLocalizedMessage(),e);
				System.exit(-1);
			}
		return configure;
	}
	
}
