package webperf.Run;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.sonar.api.resources.InputFile;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;

public class SonarProjectFileSystem implements ProjectFileSystem {

	private Charset charset;
	private File baseDir;
	
	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	public void setBuildDir(File buildDir) {
		BuildDir = buildDir;
	}

	private File BuildDir;
	
	public Charset getSourceCharset() {
		 
		return charset;
	}

	public File getBasedir() {
	 
		return baseDir;
	}

	public File getBuildDir() {
		 
		return BuildDir;
	}

	public File getBuildOutputDir() {
 		return null;
	}

	public List<File> getSourceDirs() {
 		return null;
	}

	public ProjectFileSystem addSourceDir(File paramFile) {
 		return null;
	}

	public List<File> getTestDirs() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProjectFileSystem addTestDir(File paramFile) {
		// TODO Auto-generated method stub
		return null;
	}

	public File getReportOutputDir() {
		// TODO Auto-generated method stub
		return null;
	}

	public File getSonarWorkingDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	public File resolvePath(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<File> getSourceFiles(Language... paramArrayOfLanguage) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<File> getJavaSourceFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasJavaSourceFiles() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<File> getTestFiles(Language... paramArrayOfLanguage) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasTestFiles(Language paramLanguage) {
		// TODO Auto-generated method stub
		return false;
	}

	public File writeToWorkingDirectory(String paramString1, String paramString2)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public File getFileFromBuildDirectory(String paramString) {
		// TODO Auto-generated method stub
		return null;
	}

	public Resource toResource(File paramFile) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<InputFile> mainFiles(String... paramArrayOfString) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<InputFile> testFiles(String... paramArrayOfString) {
		// TODO Auto-generated method stub
		return null;
	}

}
