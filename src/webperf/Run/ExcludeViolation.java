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

 

import webperf.tools.XmlMarshaller;

/**
 * 保存从ExcludeException.xml读取的不处理的例外信息。
 * 主要用于处理JSP及javascript动态有关CSS/IMG信息
 * @author xiehq
 *
 */
//@XmlRootElement(name="VIOLATION")
@XmlAccessorType(XmlAccessType.NONE)
public class ExcludeViolation {
	
	
  
	
	@XmlElement(name="SOURCE")
	private String source;
	
	
	@XmlElement(name="RULE")
	private String rule;
	
	@XmlElement(name="SUBRULE")
	private String subRule;
	
	@XmlElement(name="START_LINE")
	private String startLine;
	
	@XmlElement(name="NODE_TEXT")
	private String nodeText;
 	
	 
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getSubRule() {
		return subRule;
	}

	public void setSubRule(String subRule) {
		this.subRule = subRule;
	}

	public String getStartLine() {
		return startLine;
	}

	public void setStartLine(String startLine) {
		this.startLine = startLine;
	}

	public String getNodeText() {
		return nodeText;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	public ExcludeViolation() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
	 
	

	
	

	 
	
}
