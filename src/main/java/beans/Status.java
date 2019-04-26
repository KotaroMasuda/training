package beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Status {
	private int id;
	private String type;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
