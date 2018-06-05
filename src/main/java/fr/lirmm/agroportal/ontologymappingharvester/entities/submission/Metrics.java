package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metrics {

	@SerializedName("@id")
	@Expose
	private String id4;
	@SerializedName("@type")
	@Expose
	private String type;

	public String getId4() {
		return id4;
	}

	public void setId4(String id4) {
		this.id4 = id4;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}