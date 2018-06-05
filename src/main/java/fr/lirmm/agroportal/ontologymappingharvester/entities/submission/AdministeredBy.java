package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdministeredBy {

    @SerializedName("@id")
    @Expose
    private String id5;
    @SerializedName("@type")
    @Expose
    private String type;

    public String getId5() {
        return id5;
    }

    public void setId5(String id5) {
        this.id5 = id5;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}