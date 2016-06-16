package JsVersion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.digest.Md5Crypt;
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
import org.sonar.plugins.web.node.NodeType;
import org.sonar.plugins.web.node.TagNode;
import org.sonar.plugins.web.node.TextNode;

import webperf.tools.Configure;
import webperf.tools.HtmlTool;
import webperf.tools.JsVersionConf;
import webperf.tools.JsVersionList;

@Rule(key = "ScriptVersionCheck", priority = Priority.CRITICAL)
public class ScriptVersionCheck extends AbstractPageCheck {

	private static final Logger LOG = LoggerFactory
			.getLogger(ScriptVersionCheck.class);
	TreeSet<String> usedScript = new TreeSet<String>();
	private String webRootDir;
	List<Node> allNodes;
	ArrayList<ScriptNodeInfo> scriptNodes = new ArrayList<ScriptNodeInfo>();
	ScriptNodeInfo scriptnode = null;

	@Override
	public void directive(DirectiveNode node) {
		super.directive(node);
	}

	boolean changeJStoBottom = false;

	@Override
	public void endDocument() {
		super.endDocument();

		File file = this.getWebSourceCode().getFile();
		StringBuffer buffer = new StringBuffer();
		if (changeJStoBottom) {
			List<Node> tempnodes = moveJs();
			for (Node node : tempnodes)
				buffer.append(node.getCode());
		} else {
			for (Node node : allNodes) {
				buffer.append(node.getCode());
			}
		}
		try {
			WriteOutFile(file, buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		usedScript.clear();

	}

	@Override
	public void endElement(TagNode node) {
		// allNodes.indexOf(node);
		if ("script".equalsIgnoreCase(node.getNodeName())) {
			LOG.debug("script end" + node.getCode());
			if (scriptnode != null) {
				scriptnode.endPos = allNodes.indexOf(node);
			} else {
				scriptnode = new ScriptNodeInfo();
				scriptnode.endPos = allNodes.indexOf(node);
				scriptnode.startPos = scriptnode.endPos;
			}
			scriptNodes.add(scriptnode);

			scriptnode = null;
		}
		super.endElement(node);
	}

	@Override
	public void expression(ExpressionNode node) {
		super.expression(node);
	}

	@Override
	public void startDocument(List<Node> nodes) {

		super.startDocument(nodes);
		allNodes = nodes;
		usedScript.clear();
		scriptNodes.clear();

	}

	@Override
	public void characters(TextNode textNode) {

	}

	private final static String verPerf = "?ver=";
	private final static char splitChar = '&';

	@Override
	public void startElement(TagNode node) {

		if (node.getLocalName() == null) {
			return;
		}
		//LOG.debug("node local:" + node.getLocalName());
		// check external link
		if (StringUtils.equalsIgnoreCase(node.getLocalName(), "script")) {
			scriptnode = new ScriptNodeInfo();
			LOG.debug("script start" + node.getCode());
			scriptnode.startPos = allNodes.indexOf(node);
			scriptnode.endPos = allNodes.indexOf(node);

			String srcname = node.getAttribute("src");
			String fullsrcname = srcname;
			if (srcname != null && srcname.trim().length() > 0) {
				String versionStr = "";
				// 去掉？后面的代码
				if (srcname.indexOf("?") > 0) {
					versionStr = srcname.substring(srcname.indexOf("?"));
					srcname = srcname.substring(0, srcname.indexOf("?"));
					fullsrcname = srcname;
				}
				File scriptFile;
				File file = this.getWebSourceCode().getFile();

				srcname = HtmlTool.SpecialfileName(srcname);

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
					if (scriptFile.exists()) {
						// check js version
						int verNum = getJsFileVersion(scriptFile,
								scriptFilename);
						String code = node.getCode();
						String newVersionStr=null;
						if (versionStr != null && versionStr.length() > 0) {
							// have version perf ,change version
							if (versionStr.startsWith(verPerf)) {
								int temp = versionStr.indexOf(splitChar);

								if (temp > 0)
									newVersionStr = verPerf + verNum
											+ versionStr.substring(temp);
								else
									newVersionStr = verPerf + verNum;
							}
							else{
								if (versionStr.startsWith("?")){
									newVersionStr = verPerf + verNum +"&"+versionStr.substring(1);
								}
								else
									newVersionStr = verPerf + verNum + versionStr;
							}
						} else {
							newVersionStr = verPerf + verNum;
						}
						// add version
						code = code.replace(fullsrcname + versionStr,
								fullsrcname +  newVersionStr);
						createViolation(node.getStartLinePosition(),
								scriptFilename, node, "ChangeVersion");
						node.setCode(code);
						//
					}
				} catch (IOException e1) {
					LOG.error(e1.getLocalizedMessage(), e1);
					super.startElement(node);
					return;
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		super.startElement(node);
	}

	private List<Node> moveJs() {
		List<Node> tempnodes = new ArrayList<Node>();
		int nodeinfo_index = 0;

		for (int i = 0; i < scriptNodes.size(); i++) {
			ScriptNodeInfo tempnode = scriptNodes.get(i);
			// an javascript between jsp code ,it maybe control by jsp code
			// ,don't move it
			int j = 1;
			Node beforeNode = null;
			if (tempnode.startPos - j > 0) {
				beforeNode = allNodes.get(tempnode.startPos - j);
				while (beforeNode.getNodeType() == NodeType.TEXT
						&& beforeNode.getCode().trim().length() == 0) {
					j++;
					if (tempnode.startPos - j > 0)
						beforeNode = allNodes.get(tempnode.startPos - j);
					else
						break;
				}
			}

			LOG.debug("BEFORE NODE TYPE:" + beforeNode.getNodeType() + ":"
					+ beforeNode.getCode());

			j = 1;
			Node aflterNode = null;
			if (tempnode.endPos + j < allNodes.size()) {
				aflterNode = allNodes.get(tempnode.endPos + j);
				while (aflterNode.getNodeType() == NodeType.TEXT
						&& aflterNode.getCode().trim().length() == 0) {
					j++;
					if (tempnode.endPos + j < allNodes.size())
						aflterNode = allNodes.get(tempnode.endPos + j);
					else
						break;
				}
			}

			LOG.debug("AFTER NODE TYPE:" + aflterNode.getNodeType() + ":"
					+ aflterNode.getCode());
			if (beforeNode != null && aflterNode != null
					&& beforeNode.getNodeType() == NodeType.EXPRESSION
					&& aflterNode.getNodeType() == NodeType.EXPRESSION) {
				scriptNodes.remove(i);
				i--;
			}

		}
		ScriptNodeInfo nodeInfo = null;
		if (scriptNodes.size() > 0)
			nodeInfo = scriptNodes.get(nodeinfo_index);
		for (int i = 0; i < allNodes.size(); i++) {
			if (nodeInfo == null || i < nodeInfo.startPos) {
				tempnodes.add(allNodes.get(i));
			} else {

				i = nodeInfo.endPos;
				nodeinfo_index++;
				if (nodeinfo_index < scriptNodes.size())
					nodeInfo = scriptNodes.get(nodeinfo_index);
				else
					nodeInfo = null;
			}
		}

		for (ScriptNodeInfo nodeInfo2 : scriptNodes) {
			System.out.println(nodeInfo2.startPos + "," + nodeInfo2.endPos);
			for (int i = nodeInfo2.startPos; i <= nodeInfo2.endPos; i++) {
				System.out
						.println("getting.." + i + allNodes.get(i).toString());

				tempnodes.add(allNodes.get(i));
				// allNodes.remove(nodeInfo.startPos);

			}
		}

		return tempnodes;

	}

	private static JsVersionList VersionList;

	public int getJsFileVersion(File file, String filePath)
			throws JAXBException, IOException {
		if (VersionList == null) {
			VersionList = JsVersionList.readJsVersionList();
		}
		JsVersionConf ver = VersionList.getVersionConf(filePath);
		// is new js file or don't check md5.
		if (ver == null || ver.isChecked() == false) {
			String keyBytes = HtmlTool.readFileAndRemoveEmptyLine(file)
					.toString() ;
			String md5 = MD5(keyBytes);
			if (ver == null) {
				ver = new JsVersionConf();
				ver.setJsName(filePath);
				ver.setMd5(md5);
				ver.setChecked(true);
				ver.setVersion(1);
				VersionList.ModifyJSFile(ver);

			} else {
				// file changed
				if (!ver.getMd5().equals(md5)) {
					ver.setMd5(md5);
					ver.setVersion(ver.getVersion() + 1);
				}
				VersionList.ModifyJSFile(ver);

			}
		}
		return ver.getVersion();
	}

	
	 public   static String MD5(String s) {
	        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
	        try {
	            byte[] btInput = s.getBytes();
	            // 获得MD5摘要算法的 MessageDigest 对象
	            MessageDigest mdInst = MessageDigest.getInstance("MD5");
	            // 使用指定的字节更新摘要
	            mdInst.update(btInput);
	            // 获得密文
	            byte[] md = mdInst.digest();
	            // 把密文转换成十六进制的字符串形式
	            int j = md.length;
	            char str[] = new char[j * 2];
	            int k = 0;
	            for (int i = 0; i < j; i++) {
	                byte byte0 = md[i];
	                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	                str[k++] = hexDigits[byte0 & 0xf];
	            }
	            return new String(str);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	 }
	 
	@Override
	public void setProject(Project project) {
		webRootDir = project.getFileSystem().getBasedir().getAbsolutePath();

		super.setProject(project);
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

	public void WriteOutFile(java.io.File oldFile, String newContent)
			throws IOException {
		//

		String absPath = getAbsPath(webRootDir, oldFile.getAbsolutePath());
		java.io.File outFile = new java.io.File(Configure.getConfig()
				.getOutputDir()
				+ absPath);
		if (!outFile.getParentFile().exists()) {

		}
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(outFile, false), "GBK");
		writer.write(newContent);
		writer.close();

	}

}
