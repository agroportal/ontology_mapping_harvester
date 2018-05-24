package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Ontology {

    @SerializedName("browsers")
    @Expose
    private List<Browser> browsers = null;
    @SerializedName("contact")
    @Expose
    private Contact contact;
    @SerializedName("depicted_by")
    @Expose
    private String depictedBy;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("domain")
    @Expose
    private String domain;

    private List<String> homepage = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("in_foundry_order")
    @Expose
    private Integer inFoundryOrder;
    @SerializedName("layout")
    @Expose
    private String layout;
    @SerializedName("license")
    @Expose
    private License license;
    @SerializedName("mailing_list")
    @Expose
    private String mailingList;
    @SerializedName("ontology_purl")
    @Expose
    private String ontologyPurl;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("tracker")
    @Expose
    private String tracker;
    @SerializedName("usages")
    @Expose
    private List<Usage> usages = null;
    @SerializedName("alternatePrefix")
    @Expose
    private String alternatePrefix;
    @SerializedName("build")
    @Expose
    private Build build;
    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("publications")
    @Expose
    private List<Publication> publications = null;

    public List<Browser> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(List<Browser> browsers) {
        this.browsers = browsers;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getDepictedBy() {
        return depictedBy;
    }

    public void setDepictedBy(String depictedBy) {
        this.depictedBy = depictedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getHomepage() {
        return homepage;
    }

    public void setHomepage(List<String> homepage) {
        this.homepage = homepage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getInFoundryOrder() {
        return inFoundryOrder;
    }

    public void setInFoundryOrder(Integer inFoundryOrder) {
        this.inFoundryOrder = inFoundryOrder;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public String getMailingList() {
        return mailingList;
    }

    public void setMailingList(String mailingList) {
        this.mailingList = mailingList;
    }

    public String getOntologyPurl() {
        return ontologyPurl;
    }

    public void setOntologyPurl(String ontologyPurl) {
        this.ontologyPurl = ontologyPurl;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }

    public List<Usage> getUsages() {
        return usages;
    }

    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }

    public String getAlternatePrefix() {
        return alternatePrefix;
    }

    public void setAlternatePrefix(String alternatePrefix) {
        this.alternatePrefix = alternatePrefix;
    }

    public Build getBuild() {
        return build;
    }

    public void setBuild(Build build) {
        this.build = build;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("browsers", browsers).append("contact", contact).append("depictedBy", depictedBy).append("description", description).append("domain", domain).append("homepage", homepage).append("id", id).append("inFoundryOrder", inFoundryOrder).append("layout", layout).append("license", license).append("mailingList", mailingList).append("ontologyPurl", ontologyPurl).append("products", products).append("title", title).append("tracker", tracker).append("usages", usages).append("alternatePrefix", alternatePrefix).append("build", build).append("page", page).append("twitter", twitter).append("publications", publications).toString();
    }

}