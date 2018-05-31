package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Submission {

    @SerializedName("contact")
    @Expose
    private List<Contact> contact = null;
    @SerializedName("ontology")
    @Expose
    private Ontology ontology;
    @SerializedName("hasOntologyLanguage")
    @Expose
    private String hasOntologyLanguage;
    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("URI")
    @Expose
    private String uri;
    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("publication")
    @Expose
    private String publication;
    @SerializedName("documentation")
    @Expose
    private String documentation;
    @SerializedName("version")
    @Expose
    private Object version;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("submissionId")
    @Expose
    private Integer submissionId;
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
    private Context___ context;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public String getHasOntologyLanguage() {
        return hasOntologyLanguage;
    }

    public void setHasOntologyLanguage(String hasOntologyLanguage) {
        this.hasOntologyLanguage = hasOntologyLanguage;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
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

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
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

    public Context___ getContext() {
        return context;
    }

    public void setContext(Context___ context) {
        this.context = context;
    }

}