package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context___ {

    @SerializedName("@vocab")
    @Expose
    private String vocab;
    @SerializedName("hasOntologyLanguage")
    @Expose
    private String hasOntologyLanguage;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("documentation")
    @Expose
    private String documentation;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public String getHasOntologyLanguage() {
        return hasOntologyLanguage;
    }

    public void setHasOntologyLanguage(String hasOntologyLanguage) {
        this.hasOntologyLanguage = hasOntologyLanguage;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}