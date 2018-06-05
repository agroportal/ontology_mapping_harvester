package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OntologyType {

	@SerializedName("@id")
	@Expose
	private String id9;
	@SerializedName("@type")
	@Expose
	private String type;

	public String getId9() {
		return id9;
	}

	public void setId9(String id9) {
		this.id9 = id9;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}