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
 
@Rule(key = "CssCheck", priority = Priority.CRITICAL)
public class CssCheck extends AbstractPageCheck {

	private static final Logger LOG = LoggerFactory.getLogger(CssCheck.class);
	TreeSet<String> usedCss = new TreeSet<String>();
	private String webRootDir;

	@Override
	public void directive(DirectiveNode node) {
		super.directive(node);
	}

	@RuleProperty(key = "AllowedCssFiles", defaultValue = "")
	public String AllowedCssFiles = "";

	private String[] AllowedCssFilesArray;

	@Override
	public void endDocument() {
		// clear css list
		usedCss.clear();
		super.endDocument();
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
	 
		AllowedCssFilesArray = StringUtils.split(AllowedCssFiles, ',');
			   
	}

	@Override
	public void startElement(TagNode node) {
		// check for style
		if (node.getLocalName() == null) {
			createViolation(node.getStartLinePosition(),
					"getLocalName is null ", node, "NULL");
			return;
		}
		if (node.getLocalName().equalsIgnoreCase("style")) {
			createViolation(node.getStartLinePosition(),
					"Don't use style in file", node, "USE_STYLE");
		}
		// check external link
		if (StringUtils.equalsIgnoreCase(node.getLocalName(), "link")) {
			String type = node.getAttribute("type");
			String rel = node.getAttribute("rel");
			if (rel == null) {
				createViolation(node.getStartLinePosition(),
						"link missing rel attr ", node, "MISS_REL");
				super.startElement(node);

				return;
			}
			if (type == null && rel.equalsIgnoreCase("stylesheet"))
				type = "text/css";

			if (!rel.equalsIgnoreCase("stylesheet")
					|| !type.equalsIgnoreCase("text/css")) {
				createViolation(node.getStartLinePosition(),
						"link type is not css ", node, "NOT_CSS");

			}
			// rule for standard css
			
			String cssname = node.getAttribute("href");
			if (cssname.startsWith("/")) {
				createViolation(
						node.getStartLinePosition(),
						"link a file using absolute path,please use relative path ",
						node, "ABSOLUTE_PATH");

			}
			File file = this.getWebSourceCode().getFile();
			File cssFile;
			cssname=HtmlTool.SpecialfileName(cssname);
			 if (cssname.startsWith("/"))
				cssFile = new File(webRootDir, cssname);
			else
				cssFile = new File(file.getParentFile(), cssname);

			String cssFilename;
			try {
				cssFilename = cssFile.getCanonicalPath();
				// 修改filename为相对WEBROOT的文件名
				cssFilename = HtmlTool.getAbsPath(webRootDir, cssFilename);
				boolean found=false;
				for (int i=0;i<AllowedCssFilesArray.length;i++) {					
					if (cssFilename.toLowerCase().startsWith(AllowedCssFilesArray[i].toLowerCase())){
						found=true;
						break;
					}
					

				}
				if (!found){
					createViolation(node.getStartLinePosition(),
							"link a not allowd external css file :"+cssFilename, node, "LINK_CSS");
				}
			} catch (IOException e1) {
				LOG.error(e1.getLocalizedMessage(), e1);

				createViolation(node.getStartLinePosition(),
						"link file  not exist", node, "FILE_NOT_EXIST");
				super.startElement(node);

				return;
			}

			if (!cssFile.exists()) {

				createViolation(node.getStartLinePosition(),
						"link file  not exist", node, "FILE_NOT_EXIST");

			}

			// 增加NG界面标准化 不允许调用外部CSS文件检查
			// TODO : 检查范围判断，包括版本+模块，完成后才取消
			// cssErrorList.add(new CssFileIncludeException(
			// filename, ele.outerHtml()));

			if (usedCss.contains(cssFilename))
				createViolation(node.getStartLinePosition(),
						"link a file once again", node, "ONCE_AGAIN");

			else
				usedCss.add(cssFilename);

		}
		super.startElement(node);
	}

	@Override
	public void setProject(Project project) {
		// TODO Auto-generated method stub
		webRootDir = project.getFileSystem().getBasedir().getAbsolutePath();
		super.setProject(project);
	}

}
