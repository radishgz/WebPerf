package webperf.checks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import javax.imageio.ImageIO;

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

@Rule(key = "ImgCheck", priority = Priority.CRITICAL)
public class ImgCheck extends AbstractPageCheck {

	private static final Logger LOG = LoggerFactory.getLogger(ImgCheck.class);
	private HashMap<String, ImgInfo> imgMap = new HashMap<String, ImgInfo>();
	private String webRootDir;

	@RuleProperty(key = "ExcludeFiles", defaultValue = "")
	public String ExcludeFiles = "";

	private String[] ExcludeFilesArray;
	
	@Override
	public void directive(DirectiveNode node) {
		// TODO Auto-generated method stub
		super.directive(node);
	}

	@Override
	public void endDocument() {
		// clear css list
		imgMap.clear();
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
 		ExcludeFilesArray = StringUtils.split(ExcludeFiles, ',');

	}

	@Override
	public void startElement(TagNode node) {
		//check by css 
		if (node.getLocalName()==null){
			return;
		}
		// check for style
		if (!node.getLocalName().equalsIgnoreCase("img")) {
			super.startElement(node);
			return;
		}

		String imgname = node.getAttribute("src");

		// 动态设置的图像，不处理
		if (imgname  == null ) {
			super.startElement(node);
			return;
		}else if (imgname.trim().length() == 0){
			super.startElement(node);
			return;
		}

		if (imgname.startsWith("/")) {
			createViolation(node.getStartLinePosition(),
					"link a img file using absolute path,please use relative path", node,"ABSOLUTE_PATH");

		}
		File file = this.getWebSourceCode().getFile();
		File imgFile;
		imgname=HtmlTool.SpecialfileName(imgname);
		if (imgname.isEmpty()){
			return;
		}
		if (ExcludeFile(imgname)){
			return;
		}
		if (imgname.startsWith("/"))
			imgFile = new File(webRootDir, imgname);
		else
			imgFile = new File(file.getParentFile(), imgname);

		String imgFilename;
		try {
			imgFilename = imgFile.getCanonicalPath();
			// 修改filename为相对WEBROOT的文件名
			imgFilename = HtmlTool.getAbsPath(webRootDir, imgFilename);
		} catch (IOException e1) {

			createViolation(node.getStartLinePosition(),
					"link img file  not exist", node,"FILE_NOT_EXIST");
			super.startElement(node);
			return;
		}

		if (!imgFile.exists()) {

			createViolation(node.getStartLinePosition(),
					"link img file  not exist", node,"FILE_NOT_EXIST");
			super.startElement(node);
			return;
		}

		ImgInfo imgInfo = imgMap.get(imgFilename);
		if (imgInfo == null) {
			// 读取图像属性
			try {
				imgInfo = readImgInfo(imgFile);
			} catch (IOException e) {

				createViolation(node.getStartLinePosition(),
						"read img file  fail", node,"READ_FILE_FAIL");

				super.startElement(node);
				return;
			} catch (NullPointerException e) {
				LOG.error("NullPointerException..." + imgFilename);
				createViolation(node.getStartLinePosition(),
						"read img file  fail", node,"READ_FILE_FAIL");

				super.startElement(node);
				return;
			}
			imgMap.put(imgFilename, imgInfo);

		}
		String hStr = node.getAttribute("height");
		
 		int height = imgInfo.getRealHeight();
		String wStr = node.getAttribute("Width");
 		
		int width = imgInfo.getRealWidth();

		if (hStr != null && hStr.trim().length() > 0) {
			try {
				hStr=hStr.toLowerCase().replace("px", "");
				height = Integer.parseInt(hStr);
			} catch (NumberFormatException e) {
				createViolation(node.getStartLinePosition(),
						"img height value is not a digital", node,"INVAILD_DIGITAL");

				height = -1;
			}
		}

		if (wStr != null && wStr.trim().length() > 0) {
			try {
				wStr=wStr.toLowerCase().replace("px", "");

				width = Integer.parseInt(wStr);
			} catch (NumberFormatException e) {
				createViolation(node.getStartLinePosition(),
						"img width value is not a digital", node,"INVAILD_DIGITAL");

				width = -1;
			}
		}
		if (width != imgInfo.getRealWidth()
				|| height != imgInfo.getRealHeight()) {
			createViolation(node.getStartLinePosition(),
					"img file size not equal to node set", node,"NOT_EQUAL");

		}

		super.startElement(node);
	}

	private boolean ExcludeFile(String imgname) {
		
		for (int i=0;i<ExcludeFilesArray.length;i++) {					
			if (imgname.toLowerCase().indexOf(ExcludeFilesArray[i].toLowerCase())>=0){
				return true;
			}		

		}
		return false;
	}

	@Override
	public void setProject(Project project) {
		super.setProject(project);
		webRootDir = project.getFileSystem().getBasedir().getAbsolutePath();
	}

	public ImgInfo readImgInfo(File file) throws IOException {
		BufferedImage imgInput = ImageIO.read(file);
		return new ImgInfo(file.getAbsolutePath(), imgInput.getHeight(),
				imgInput.getWidth());

	}
}
