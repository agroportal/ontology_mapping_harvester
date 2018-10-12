package fr.lirmm.agroportal.ontologymappingharvester.entities.postmappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Class {

    @SerializedName("obsolete")
    @Expose
    private Boolean obsolete;
    @SerializedName("@id1")
    @Expose
    private String id1;
    @SerializedName("@type")
    @Expose
    private String type;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("@context")
    @Expose
    private Context_ context;

    public Boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Context_ getContext() {
        return context;
    }

    public void setContext(Context_ context) {
        this.context = context;
    }

}