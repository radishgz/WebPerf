package webperf.checks;
 

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 图片信息定义类
 * @author xiehq
 *
 */
@XmlRootElement(name="ImgInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class ImgInfo {

	public ImgInfo(String imgName,  int realHeight,int realWidth) {
		super();
		this.imgName = imgName;
		this.realWidth = realWidth;
		this.realHeight = realHeight;
	}

	@XmlElement
	String imgName;
	
	@XmlElement
	int realWidth;
	
	public ImgInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@XmlElement
	int realHeight;
	
	//public ArrayList<ImgUseHtml> badHtml=new ArrayList<ImgUseHtml>();
	
	
	 

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public int getRealWidth() {
		return realWidth;
	}

	public void setRealWidth(int realWidth) {
		this.realWidth = realWidth;
	}

	public int getRealHeight() {
		return realHeight;
	}

	public void setRealHeight(int realHeight) {
		this.realHeight = realHeight;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		
		
		if (obj instanceof ImgInfo){
			/*
			System.err.println(
					this.imgName+":"+(((ImgInfo) obj).getImgName()) 
			+"."+	this.realWidth+"."+((ImgInfo) obj).getRealWidth() +"."+
				 		this.realHeight +"."+((ImgInfo) obj).getRealHeight());
			*/
			boolean b =this.imgName.equalsIgnoreCase(((ImgInfo) obj).getImgName()) 
			&&	this.realWidth==((ImgInfo) obj).getRealWidth() &&
	 		this.realHeight==((ImgInfo) obj).getRealHeight();
			return b;
		}
			 
		return false;
	} 
}
