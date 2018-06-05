package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

	@SerializedName("@id")
	@Expose
	private String id6;
	@SerializedName("@type")
	@Expose
	private String type;

	public String getId6() {
		return id6;
	}

	public void setId6(String id6) {
		this.id6 = id6;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}