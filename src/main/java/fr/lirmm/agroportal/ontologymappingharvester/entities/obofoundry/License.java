package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class License {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("url")
    @Expose
    private String url;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("label", label).append("logo", logo).append("url", url).toString();
    }

}