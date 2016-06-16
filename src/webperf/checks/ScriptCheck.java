package webperf.checks;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.resources.Project;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.node.DirectiveNode;
import org.sonar.plugins.web.node.ExpressionNode;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.node.TagNode;

import webperf.tools.HtmlTool;

@Rule(key = "ScriptCheck", priority = Priority.CRITICAL)
public class ScriptCheck extends AbstractPageCheck {

	private static final Logger LOG = LoggerFactory
			.getLogger(ScriptCheck.class);
	TreeSet<String> usedScript = new TreeSet<String>();
	private String webRootDir;
	

	@Override
	public void directive(DirectiveNode node) {
		super.directive(node);
	}

	@Override
	public void endDocument() {
		super.endDocument();
		usedScript.clear();

	}

	@Override
	public void endElement(TagNode node) {
		super.endElement(node);
	}

	@Override
	public void expression(ExpressionNode node) {
		super.expression(node);
	}

	@Override
	public void startDocument(List<Node> nodes) {
		super.startDocument(nodes);
		usedScript.clear();

	}

	@Override
	public void startElement(TagNode node) {

		//check by css 
		if (node.getLocalName()==null){
			return;
		}
		// check for style
		/*
		if (StringUtils.equalsIgnoreCase(node.getLocalName(), "Html")) {

			List<TagNode> childrens = node.getChildren();
			for (TagNode children : childrens) {
				if (StringUtils.equalsIgnoreCase(children.getLocalName(),
						"script")) {
					createViolation(node.getStartLinePosition(),
							"Don't write script in file,please link a external file",
							children,"INTERNAL_SCRIPT");

				}
			}
		}*/

		// check external link
		if (StringUtils.equalsIgnoreCase(node.getLocalName(), "script")) {

			String language = node.getAttribute("language");

			String type = node.getAttribute("type");

			if ((language != null && language.trim().length() == 0 && language
					.equalsIgnoreCase("javascript") == false)
					|| (type != null && type.trim().length() == 0 && type
							.equalsIgnoreCase("text/javascript") == false)) {
				createViolation(node.getStartLinePosition(),
						"script language is not javascript", node,"JAVASCRIPT_ONLY");

			}
			String srcname = node.getAttribute("src");
			if (srcname != null && srcname.trim().length() > 0) {
				// 去掉？后面的代码
				if (srcname.indexOf("?") > 0) {
					srcname = srcname.substring(0, srcname.indexOf("?"));
				}
				File scriptFile;
				File file = this.getWebSourceCode().getFile();

				if (srcname.startsWith("/")) {
					createViolation(
							node.getStartLinePosition(),
							"link a javascripte file using absolute path,please use relative path ",
							node,"ABSOLUTE_PATH");

				}
				srcname=HtmlTool.SpecialfileName(srcname);

				if (srcname.startsWith("/"))
					scriptFile = new File(webRootDir, srcname);
				else
					scriptFile = new File(file.getParentFile(), srcname);

				String scriptFilename;

				try {
					scriptFilename = scriptFile.getCanonicalPath();
					// 修改filename为相对WEBROOT的文件名
					scriptFilename = HtmlTool.getAbsPath(webRootDir,
							scriptFilename);
				} catch (IOException e1) {
					createViolation(node.getStartLinePosition(),
							"link javascirpte file  not exist", node,"FILE_NOT_EXIST");
					super.startElement(node);
					LOG.error(e1.getLocalizedMessage(), e1);
					return;
				}

				if (!scriptFile.exists()) {
					createViolation(node.getStartLinePosition(),
							"link javascirpte file  not exist", node,"FILE_NOT_EXIST");

				}
				if (usedScript.contains(scriptFilename))
					createViolation(node.getStartLinePosition(),
							"call a javascirpte file once again:"+scriptFilename, node,"ONCE_AGAIN");
				else
					usedScript.add(scriptFilename);
			}else{
				//internal Javascript
				createViolation(node.getStartLinePosition(),
						"Don't write script in file,please link a external file",
						node,"INTERNAL_SCRIPT");

			}

		}
		super.startElement(node);
	}

	@Override
	public void setProject(Project project) {
		webRootDir = project.getFileSystem().getBasedir().getAbsolutePath();	

		super.setProject(project);
	}

}
