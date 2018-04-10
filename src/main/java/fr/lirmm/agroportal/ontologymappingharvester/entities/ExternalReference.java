package fr.lirmm.agroportal.ontologymappingharvester.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Entity used to represent external mapping
 */
public class ExternalReference {

    @SerializedName("search_string")
    @Expose
    private String searchString;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("iri")
    @Expose
    private String iri;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }
}
