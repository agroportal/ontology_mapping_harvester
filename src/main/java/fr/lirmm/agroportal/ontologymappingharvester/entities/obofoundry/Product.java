package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Product {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ontology_purl")
    @Expose
    private String ontologyPurl;
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOntologyPurl() {
        return ontologyPurl;
    }

    public void setOntologyPurl(String ontologyPurl) {
        this.ontologyPurl = ontologyPurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("ontologyPurl", ontologyPurl).append("title", title).toString();
    }

}
