package webperf.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "JsVersionList")
@XmlAccessorType(XmlAccessType.NONE)
public class JsVersionList {

	@XmlElement(name = "WebRoot")
	String WebRoot;

	@XmlElement(name = "JsVresions")
	static ArrayList<JsVersionConf> JsVresions=new ArrayList<JsVersionConf> ();

	static HashMap<String,JsVersionConf> VersionMap=new HashMap<String,JsVersionConf>();
	
	
	public JsVersionConf  getVersionConf(String fileName){
		if (VersionMap==null|| VersionMap.size()!=JsVresions.size()){
			VersionMap=new HashMap<String,JsVersionConf>();
			for (JsVersionConf ver:JsVresions){
				VersionMap.put(ver.getJsName(), ver);
			}
			
		}
		return VersionMap.get(fileName);
		
	}
	
	 
	
	public void ModifyJSFile(JsVersionConf ver){
		JsVersionConf oldver = getVersionConf(ver.getJsName());
		if (oldver!=null)
			JsVresions.remove(oldver);
		VersionMap.put(ver.getJsName(), ver);
		JsVresions.add(ver);
	}
	
	public String getWebRoot() {
		return WebRoot;
	}

	public void setWebRoot(String webRoot) {
		WebRoot = webRoot;
	}

	public ArrayList<JsVersionConf> getJsVresions() {
		return JsVresions;
	}

	public void setJsVresions(ArrayList<JsVersionConf> jsVresions) {
		JsVresions = jsVresions;
	}

	static String filenameOnClasspath = "../WebPerfJsVersionList.xml";

	public static JsVersionList jslist;
	public static JsVersionList readJsVersionList() throws JAXBException  {
			 if (jslist!=null)
				 	return jslist;
			String classdir = JsVersionList.class.getClass().getResource("/").getPath();
			FileReader input;
			try {
				input = new FileReader(classdir + filenameOnClasspath);
			} catch (FileNotFoundException e1) {
				return new JsVersionList();
			}
			//InputStreamReader input = new InputStreamReader(JsVersionList.class
			//		.getClassLoader().getResourceAsStream(filenameOnClasspath));
			
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
			jslist = (JsVersionList) XmlMarshaller
					.elementUnmarshal(JsVersionList.class.getPackage()
							.getName(), buffer.toString());
			if (jslist==null)
				jslist=new JsVersionList();
			return jslist;

		 
		

	}
	
	public static  void WriteResultFile(JsVersionList vList) throws IOException, JAXBException {
		// 
		String classdir = JsVersionList.class.getClass().getResource("/").getPath();
		File outFile;
		outFile = new File(classdir + filenameOnClasspath);
		String newContent=XmlMarshaller.marshal(vList );
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(outFile, false), "GBK");
		writer.write(newContent);
		writer.close();

	}

}
