package webperf.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


 

/**
 * 目录索引处理基础类.
 * 子类必须实现DoingWithDir，DoingWithFile
 * 进入子目录的时候会调用DoingWithDir，每个文件会调用DoingWithFile
 * 
 * @author xiehq
 *
 */
public   abstract class DirSearchBase  {
	
	private static transient Logger logger = LoggerFactory.getLogger(DirSearchBase.class);
	
	

 	/**
	 * @param dir
	 * @throws NullPointerException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void serachFiles(String dir) {

		File root = new File(dir);
		try {
			DoingWithDir( root);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			System.exit(-1);
		}
		File[] filesOrDirs = root.listFiles();

		for (int i=0;i<filesOrDirs.length;i++) {
			File file=filesOrDirs[i];
			if (file.isDirectory()) {
				try{
					String fileName=file.getAbsolutePath();
					fileName=	HtmlTool.getAbsPath(this.getBasedir(), fileName);
					//String f2=file.getCanonicalPath();
					//logger.info("scan dir "+fileName);
					boolean exclude=false;
					for(String dirName:Configure.getConfig().getExcludeDir()){
						if (fileName.matches(dirName)){
							logger.info("dir is exclude:"+fileName);
							exclude=true;
							break;
						}
					}
					if (exclude==false){
						DoingWithDir(file);				 
						serachFiles(file.getAbsolutePath());
					}
				}catch (Exception e){
					logger.error(e.getMessage(),e);
					
				}
				
			} else {
				String fileName=file.getAbsolutePath();
				boolean exclude=false;
				for(String dirName:Configure.getConfig().getExcludeFiles()){
					if (fileName.matches(dirName)){
						logger.info("file is exclude:"+fileName);
						exclude=true;
						break;
					}
				}
				if (exclude==false){
					try{				
						
						DoingWithFile(file);
					}catch (Exception e){
						logger.error(e.getMessage(),e);
						
					}
				}
			}
		}

		 

	}
	
	public abstract String getBasedir(); 
	public abstract void  DoingWithFile(File file) throws Exception;
	
	public abstract void DoingWithDir(File dir) throws Exception;
	
	public String StacktoString(StackTraceElement s[]){
		String temp="";
		for (int i=0;i<s.length;i++){
			temp+=s[i].toString()+"\r\n";
		}
		return temp;
	}
}