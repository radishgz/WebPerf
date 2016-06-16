package webperf.tools;

/**
 * 记录img在html文件中的使用信息
 * @author xiehq
 *
 */
public class ImgUseHtml{
	
	public ImgUseHtml(String filename,  int height,int width) {
		super();
		this.filename = filename;
		this.width = width;
		this.height = height;
	}
	private String filename;
	private int width;
	private int height;
	public void setHeight(int height) {
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilename() {
		return filename;
	}
	
}