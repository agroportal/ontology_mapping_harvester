package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Build {

    @SerializedName("infallible")
    @Expose
    private Integer infallible;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("source_url")
    @Expose
    private String sourceUrl;

    public Integer getInfallible() {
        return infallible;
    }

    public void setInfallible(Integer infallible) {
        this.infallible = infallible;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("infallible", infallible).append("method", method).append("sourceUrl", sourceUrl).toString();
    }

}