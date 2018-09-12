package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("nextPage")
    @Expose
    private String nextPage;
    @SerializedName("prevPage")
    @Expose
    private String prevPage;

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(String prevPage) {
        this.prevPage = prevPage;
    }

    @Override
    public String toString() {
        return "Links{" +
                "nextPage='" + nextPage + '\'' +
                ", prevPage='" + prevPage + '\'' +
                '}';
    }
}