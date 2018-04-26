package fr.lirmm.agroportal.ontologymappingharvester.entities.classquery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Collection {

    @SerializedName("prefLabel")
    @Expose
    private String prefLabel;
    @SerializedName("synonym")
    @Expose
    private List<String> synonym = null;
    @SerializedName("obsolete")
    @Expose
    private Boolean obsolete;
    @SerializedName("matchType")
    @Expose
    private String matchType;
    @SerializedName("ontologyType")
    @Expose
    private String ontologyType;
    @SerializedName("provisional")
    @Expose
    private Boolean provisional;
    @SerializedName("@id")
    @Expose
    private String id;
    @SerializedName("@type")
    @Expose
    private String type;
    @SerializedName("links")
    @Expose
    private Links_ links;
    @SerializedName("@context")
    @Expose
    private Context_ context;

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    public List<String> getSynonym() {
        return synonym;
    }

    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    public Boolean getObsolete() {
        return obsolete;
    }

    public void setObsolete(Boolean obsolete) {
        this.obsolete = obsolete;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getOntologyType() {
        return ontologyType;
    }

    public void setOntologyType(String ontologyType) {
        this.ontologyType = ontologyType;
    }

    public Boolean getProvisional() {
        return provisional;
    }

    public void setProvisional(Boolean provisional) {
        this.provisional = provisional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Links_ getLinks() {
        return links;
    }

    public void setLinks(Links_ links) {
        this.links = links;
    }

    public Context_ getContext() {
        return context;
    }

    public void setContext(Context_ context) {
        this.context = context;
    }

}