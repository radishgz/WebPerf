package JsVersion;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.checks.AnnotationCheckFactory;
import org.sonar.api.checks.CheckFactory;
import org.sonar.api.checks.NoSonarFilter;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Measure;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.resources.File;
import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.web.analyzers.ComplexityVisitor;
import org.sonar.plugins.web.analyzers.PageCountLines;
import org.sonar.plugins.web.checks.AbstractPageCheck;
import org.sonar.plugins.web.checks.WebViolation;
import org.sonar.plugins.web.core.Web;
import org.sonar.plugins.web.lex.PageLexer;
import org.sonar.plugins.web.node.Node;
import org.sonar.plugins.web.rules.CheckClasses;
import org.sonar.plugins.web.rules.WebRulesRepository;
import org.sonar.plugins.web.visitor.DefaultNodeVisitor;
import org.sonar.plugins.web.visitor.HtmlAstScanner;
import org.sonar.plugins.web.visitor.NoSonarScanner;
import org.sonar.plugins.web.visitor.WebSourceCode;

import com.google.common.collect.ImmutableList;

import webperf.Run.ExcludeViolationList;
import webperf.Run.ProfileRules;
import webperf.Run.SonarProjectFileSystem;
import webperf.Run.WebRuleFinder;
import webperf.tools.Configure;
import webperf.checks.CssCheck;
import webperf.tools.DirSearchBase;
import webperf.tools.JsVersionList;
import webperf.tools.ParserFile;
import webperf.tools.XmlMarshaller;

public class JavaVersionMain extends DirSearchBase {

	private static final Logger logger = LoggerFactory
			.getLogger(JavaVersionMain.class);

	private static Project project;

	private ArrayList<WebSourceCode> SourceList = new ArrayList<WebSourceCode>();
	// private String webRootDir;

	private PageLexer lexer;
	private HtmlAstScanner scanner;
	/**
	 * 检查主函数 主要执行： 1、初始化各检查类 2、调用 serachFiles 循环处理所有子目录、文件 3、处理结果并输出报告
	 */

	private final String XMLResult = "<?xml version=\"1.0\" encoding=\"GBK\" ?>\r\n"
			+ "<SONAR_WEB_PERF>\r\n"
			+ "\t<BASE_DIR>#BASE_DIR</BASE_DIR>\r\n"
			+ "\t<CHECK_DIR>#CHEKC_DIR</CHECK_DIR>\r\n"
			+ "\t<CHECK_TIME>#CHECK_TIME</CHECK_TIME>\r\n"
			+ "\t<VIOLATIONS>#VIOLATIONS</VIOLATIONS>\r\n</SONAR_WEB_PERF>";

	private String baseDir;

	private static AnnotationCheckFactory annotationCheckFactory;

	public void check(String dirName, String webRoot) {
		baseDir = webRoot;

		lexer = new PageLexer();

		// configure page scanner and the visitors
		scanner = setupScanner(project);

		// JSP 预编译

		java.io.File root = new java.io.File(dirName);
		// 判断是否为目录
		if (root.isDirectory())
			serachFiles(dirName);
		else {
			DoingWithFile(root);

		}
		try {
			JsVersionList.readJsVersionList().setWebRoot(webRoot);
			JsVersionList.WriteResultFile(JsVersionList.readJsVersionList());
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (JAXBException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		// 输出错误信息
		String xml = XMLResult.replace("#BASE_DIR", StringEscapeUtils
				.escapeXml(webRoot));
		xml = xml.replace("#CHEKC_DIR", StringEscapeUtils.escapeXml(dirName));
		xml = xml.replace("#CHECK_TIME", String.valueOf(new Date()));
		StringBuffer vios = new StringBuffer();

		ExcludeViolationList Excludelist = null;
		try {
			Excludelist = ExcludeViolationList.readExcludeViolations();
		} catch (FileNotFoundException e2) {
			logger.error(e2.getLocalizedMessage(), e2);
		} catch (IOException e2) {
			logger.error(e2.getLocalizedMessage(), e2);
		}
		for (WebSourceCode sourceCode : SourceList) {

			for (WebViolation violation : sourceCode.getViolations()) {
				if (Excludelist != null) {
					if (Excludelist.isExcludeViolation(violation))
						continue;
				}
				vios.append(violation.toXML());
				// logger.info("violation:"+violation.toString());

			}

		}
		xml = xml.replace("#VIOLATIONS", vios);

		String filename = dirName.replace("/", "_");
		filename = filename.replace("\\", "_").replace(":", "_") + ".xml";
		FileOutputStream temp;
		try {
			temp = new FileOutputStream(Configure.getConfig().getOutputDir()
					+ filename);

			temp.write(xml.getBytes());
			temp.close();
			logger.info("Check Result is write to "
					+ Configure.getConfig().getOutputDir() + filename);
		} catch (FileNotFoundException e1) {
			logger.error(e1.getLocalizedMessage(), e1);
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 单个文件检查函数 只是简单调用各检查类的DoingWithFile
	 * 
	 */
	@Override
	public void DoingWithFile(java.io.File file) {
		String fileName;
		try {
			fileName = file.getCanonicalPath();
			if (!ParserFile.AllowExtension(fileName))
				return;
		} catch (IOException e1) {
			logger.debug(e1.getLocalizedMessage(), e1.getStackTrace());
			return;
		}
		logger.debug(fileName);
		// configure the lexer

		FileReader reader = null;
		try {
			File resource = File.fromIOFile(file, project);
			WebSourceCode sourceCode = new WebSourceCode(file, resource);

			reader = new FileReader(file);
			List<Node> nodeList = lexer.parse(reader);
			scanner.scan(nodeList, sourceCode, project.getFileSystem()
					.getSourceCharset());
			/*
			 * for (Measure measure : sourceCode.getMeasures()) { //
			 * sensorContext.saveMeasure(sourceCode.getResource(), measure);
			 * 
			 * logger.info(measure.toString()); }
			 */
			if (sourceCode.getViolations().size() > 0)
				SourceList.add(sourceCode);
			/*
			 * for (Violation violation : sourceCode.getViolations()) {
			 * logger.info("violation:"+sourceCode.getFile().getAbsolutePath());
			 * logger.info("violation:"+violation.toString());
			 * 
			 * }
			 */

		} catch (Exception e) {
			logger.error("Can not analyze file " + file.getAbsolutePath(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

	}

	/**
	 * 
	 * 进入子目录时候的处理函数，目前只简单输出logger
	 * 
	 * @see webperf.tools.DirSearchBase#DoingWithDir(java.io.File)
	 */
	@Override
	public void DoingWithDir(java.io.File dir) {

		logger.debug(dir + " checking.");
		String absPath = getAbsPath(getBasedir(), dir
				.getAbsolutePath());
		java.io.File outdir = new java.io.File(Configure.getConfig()
				.getOutputDir() + absPath);
		if (!outdir.exists())
			outdir.mkdirs();
	}

	
	/**
	 * command 方式调用入口，只判断参数是否正确，不正确调用USAGE函数，否则调用CHeck
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String s = System.getProperty("java.version", "未定义");
		logger.debug("java version :" + s);
		if (args.length != 2) {
			useage();
			return;
		}
		JavaVersionMain sonar = new JavaVersionMain();

		project = new Project("CheckbySonar");
		project.setLanguage(new Web(new Settings()));

		SonarProjectFileSystem fileSystem = new SonarProjectFileSystem();
		fileSystem.setBaseDir(new java.io.File(args[1]));
		fileSystem.setCharset(Charset.forName("GBK"));
		project.setFileSystem(fileSystem);

		RulesProfile profile = createStandardRulesProfile();
		annotationCheckFactory = AnnotationCheckFactory.create(profile,
				WebRulesRepository.REPOSITORY_KEY,
				CheckClasses.getCheckClasses());
		sonar.check(args[0], args[1]);

	}

	/**
	 * 给出命令调用例子
	 */
	private static void useage() {
		System.out.println("必须使用JDK1.6或者以上运行，输入参数有误，使用方式如下：");
		System.out
				.println("java -Dencoding=gb2312 -Dfile.encoding=GBK -Duser.language=zh -Duser.region=CN  webPerf.Main workdir webroot");
		System.out.println("workdir 要检查的目录/文件");
		System.out.println("webroot web文件根目录");

	}

	private HtmlAstScanner setupScanner(Project project2) {
		List<DefaultNodeVisitor> list=new ArrayList<DefaultNodeVisitor>();
		list.add(new NoSonarScanner(new NoSonarFilter(null)));
		HtmlAstScanner scanner = new HtmlAstScanner(project2,list);
 		for (AbstractPageCheck check : (Collection<AbstractPageCheck>) annotationCheckFactory
				.getChecks()) {
			scanner.addVisitor(check);
			check.setRule(annotationCheckFactory
					.getActiveRule(check).getRule());
		}			
	 
		return scanner;
	}

	@SuppressWarnings("deprecation")
	public static RulesProfile createStandardRulesProfile() {
		ProfileDefinition profileDefinition = new ProfileRules(
				new XMLProfileParser(newRuleFinder()));
		ValidationMessages messages = ValidationMessages.create();
		RulesProfile profile = profileDefinition.createProfile(messages);

		return profile;
	}

	protected static RuleFinder newRuleFinder() {
		return new WebRuleFinder();
	}

	@Override
	public String getBasedir() {
		return baseDir;
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
	static public String getAbsPath(java.io.File root, java.io.File file)
			throws IOException {
		String rootname;
		String filename;

		rootname = root.getCanonicalPath();
		filename = file.getCanonicalPath();

		if (filename.startsWith(rootname)) {
			return filename.substring(rootname.length());
		}
		return filename;

	}
}
