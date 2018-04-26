package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context_ {

    @SerializedName("@vocab")
    @Expose
    private String vocab;
    @SerializedName("acronym")
    @Expose
    private String acronym;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("administeredBy")
    @Expose
    private AdministeredBy administeredBy;
    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("hasDomain")
    @Expose
    private HasDomain hasDomain;
    @SerializedName("viewOf")
    @Expose
    private ViewOf viewOf;
    @SerializedName("ontologyType")
    @Expose
    private OntologyType ontologyType;

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdministeredBy getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(AdministeredBy administeredBy) {
        this.administeredBy = administeredBy;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public HasDomain getHasDomain() {
        return hasDomain;
    }

    public void setHasDomain(HasDomain hasDomain) {
        this.hasDomain = hasDomain;
    }

    public ViewOf getViewOf() {
        return viewOf;
    }

    public void setViewOf(ViewOf viewOf) {
        this.viewOf = viewOf;
    }

    public OntologyType getOntologyType() {
        return ontologyType;
    }

    public void setOntologyType(OntologyType ontologyType) {
        this.ontologyType = ontologyType;
    }

}