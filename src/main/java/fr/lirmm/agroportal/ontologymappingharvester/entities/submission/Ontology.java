package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ontology {

    @SerializedName("administeredBy")
    @Expose
    private List<String> administeredBy = null;
    @SerializedName("acronym")
    @Expose
    private String acronym;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("summaryOnly")
    @Expose
    private Object summaryOnly;
    @SerializedName("ontologyType")
    @Expose
    private String ontologyType;
    @SerializedName("group")
    @Expose
    private List<String> group = null;
    @SerializedName("hasDomain")
    @Expose
    private List<String> hasDomain = null;
    @SerializedName("viewingRestriction")
    @Expose
    private String viewingRestriction;
    @SerializedName("flat")
    @Expose
    private Object flat;
    @SerializedName("viewOf")
    @Expose
    private Object viewOf;
    @SerializedName("@id")
    @Expose
    private String id2;
    @SerializedName("@type")
    @Expose
    private String type;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("@context")
    @Expose
    private Context_ context;

    public List<String> getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(List<String> administeredBy) {
        this.administeredBy = administeredBy;
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

    public Object getSummaryOnly() {
        return summaryOnly;
    }

    public void setSummaryOnly(Object summaryOnly) {
        this.summaryOnly = summaryOnly;
    }

    public String getOntologyType() {
        return ontologyType;
    }

    public void setOntologyType(String ontologyType) {
        this.ontologyType = ontologyType;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public List<String> getHasDomain() {
        return hasDomain;
    }

    public void setHasDomain(List<String> hasDomain) {
        this.hasDomain = hasDomain;
    }

    public String getViewingRestriction() {
        return viewingRestriction;
    }

    public void setViewingRestriction(String viewingRestriction) {
        this.viewingRestriction = viewingRestriction;
    }

    public Object getFlat() {
        return flat;
    }

    public void setFlat(Object flat) {
        this.flat = flat;
    }

    public Object getViewOf() {
        return viewOf;
    }

    public void setViewOf(Object viewOf) {
        this.viewOf = viewOf;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
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