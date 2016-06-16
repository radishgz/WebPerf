package webperf.tools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "JsVersionConf")
@XmlAccessorType(XmlAccessType.NONE)
public class JsVersionConf {
	
	@XmlElement(name="jsName")
	private String jsName;
	
	@XmlElement(name="md5")
	private String md5;
	@XmlElement(name="version")
	private int version;
	
	private boolean Checked=false;
	
	public String getJsName() {
		return jsName;
	}
	public void setJsName(String jsName) {
		this.jsName = jsName;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setChecked(boolean checked) {
		Checked = checked;
	}
	public boolean isChecked() {
		return Checked;
	}
	
	
}
