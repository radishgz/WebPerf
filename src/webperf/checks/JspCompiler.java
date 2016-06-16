package webperf.checks;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
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

@Rule(key = "JspCompiler", priority = Priority.CRITICAL)
public class JspCompiler extends AbstractPageCheck {

	private static final Logger LOG = LoggerFactory
			.getLogger(JspCompiler.class);
	private String webRootDir;
	private JspC jspc;

	@RuleProperty(key = "JspcOutputDir", defaultValue = "c:\temp")
	private String JspcOutputDir;

	public String getJspcOutputDir() {
		return JspcOutputDir;
	}

	public void setJspcOutputDir(String jspcOutputDir) {
		JspcOutputDir = jspcOutputDir;
	}

	@Override
	public void directive(DirectiveNode node) {
		// TODO Auto-generated method stub
		super.directive(node);
	}

	@Override
	public void endDocument() {

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
		String fileName;
		File file = getWebSourceCode().getFile();
		try {

			fileName = file.getCanonicalPath();
			LOG.info("jsp compile", file.getAbsoluteFile());

			if (!getExtension(fileName).equalsIgnoreCase("jsp")) {
				super.startDocument(nodes);
				return;
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			LOG.error("read file error", e1);
			super.startDocument(nodes);
			return;
		}
		try {
			// fileName=
			// 转化为相对文件名称
			fileName = HtmlTool.getAbsPath(webRootDir, fileName);

			// jspc.setJavaEncoding("GBK");
			// jspc.set
			//jspc.cleanJspFile();
			jspc.setJspFiles(file.getCanonicalPath());
			jspc.execute();
			LOG.info("jsp compile", file.getAbsoluteFile());

			// jspc.get
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JasperException e) {

			// JspcErrorList.add(new JspJasperException(fileName,
			// e.getMessage()));
			createViolation(1, "throw JasperException when compile jsp file",
					null,"COMPILE_FAIL");

			// e.printStackTrace();
		} catch (java.lang.NoClassDefFoundError e) {
			createViolation(1,
					"throw NoClassDefFoundError when compile jsp file", null,"CLASS_NOT_FOUND");

		} catch (Exception e) {
			createViolation(1, "throw unkown Exception when compile jsp file",null,"COMPILE_UNKOWN_EXCEPTION");
		} catch (Error e) {
			createViolation(1,
					"throw unkown java lang error when compile jsp file", null,"COMPILE_LANG_ERROR");
			LOG.error(e.getMessage() + ":" + fileName, e);

		}
		super.startDocument(nodes);
	}

	@Override
	public void startElement(TagNode node) {

		super.startElement(node);
	}

	@Override
	public void setProject(Project project) {
		// TODO Auto-generated method stub
		webRootDir = project.getFileSystem().getBasedir().getAbsolutePath();

		jspc = new JspC();

		String[] arg0 = { "-mapped" };
		try {
			jspc.setArgs(arg0);
		} catch (Exception e) {
			LOG.error("create jspc fial", e);
		}

		jspc.setUriroot(webRootDir);// web应用的root目录
		jspc.setOutputDir(JspcOutputDir);// .java文件和.class文件的输出目录
		jspc.setCompile(true);// 是否编译 false或不指定的话只生成.java文件
		// jspc.setArgs(args);
		jspc.setPackage("webPerf");
		super.setProject(project);
	}

	public static String getExtension(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return "";
	}
}
