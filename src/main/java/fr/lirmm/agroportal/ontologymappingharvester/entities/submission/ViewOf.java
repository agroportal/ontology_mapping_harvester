package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewOf {

	@SerializedName("@id")
	@Expose
	private String id8;
	@SerializedName("@type")
	@Expose
	private String type;

	public String getId8() {
		return id8;
	}

	public void setId8(String id8) {
		this.id8 = id8;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}