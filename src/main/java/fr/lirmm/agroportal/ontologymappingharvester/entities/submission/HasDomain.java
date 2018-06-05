package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HasDomain {

	@SerializedName("@id")
	@Expose
	private String id7;
	@SerializedName("@type")
	@Expose
	private String type;

	public String getId7() {
		return id7;
	}

	public void setId7(String id7) {
		this.id7 = id7;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}