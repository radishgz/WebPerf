package org.sonar.plugins.web.checks;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang.StringEscapeUtils;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;
import org.sonar.plugins.web.node.Node;

import webperf.tools.HtmlTool;

/**
 * @author xie a violation for webperf check result;
 */
@XmlAccessorType(XmlAccessType.NONE)
@SuppressWarnings("deprecation")
public class WebViolation extends Violation {

	private Node node;
	private String subRuleId;
	@XmlElement(name="DIRNAME")
	String dirName;

	public String getSubRuleId() {
		return subRuleId;
	}

	public WebViolation() {
		super(new Rule());
	}

	public void setSubRuleId(String subRuleId) {
		this.subRuleId = subRuleId;
	}

	public WebViolation(Rule rule) {
		super(rule);
	}

	public WebViolation(Rule rule, Resource resource) {
		super(rule, resource);
	}

	public WebViolation(Rule rule, Resource resource, Node node2) {
		super(rule, resource);
		node = node2;
	}

	public WebViolation(Rule rule, Resource resource, Node node2,
			String subRuleId2) {
		super(rule, resource);
		node = node2;
		subRuleId = subRuleId2;
	}

	public static WebViolation WebCreate(Rule rule, Resource resource,
			Node node, String subRule) {
		WebViolation vio = new WebViolation(rule, resource, node, subRule);
		return vio;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	private final String xmlFormat = " <VIOLATION>\r\n"
			+ "<SOURCE>#SOURCE</SOURCE>\r\n" + "\t<RULE>#RULE_NAME</RULE>\r\n"
			+ "\t<SUBRULE>#SUB_RULE_NAME</SUBRULE>\r\n"
			+ "\t<START_LINE>#START_LINE</START_LINE>\r\n"
			+ "\t<NODE_TEXT>#NODE_TEXT</NODE_TEXT>\r\n"
			+ "\t<MESSAGE>#MESSAGE</MESSAGE>\r\n" + "</VIOLATION>\r\n";

	public String toXML() {
		String xml = xmlFormat.replace("#SOURCE", StringEscapeUtils
				.escapeXml(this.getResource().getPath()));
		xml = xml.replace("#RULE_NAME", this.getRule().getKey());
		xml = xml.replace("#SUB_RULE_NAME", subRuleId);
		xml = xml.replace("#START_LINE", String.valueOf(this.getLineId()));
		if (this.getNode() != null)
			xml = xml.replace("#NODE_TEXT", StringEscapeUtils.escapeXml(this
					.getNode().getCode()));
		else
			xml = xml.replace("#NODE_TEXT", "");

		xml = xml.replace("#MESSAGE", StringEscapeUtils.escapeXml(this
				.getMessage()));
		return xml;

	}

	public String getDirName() {
		return this.getResource().getParent().getPath();
		//return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

}
