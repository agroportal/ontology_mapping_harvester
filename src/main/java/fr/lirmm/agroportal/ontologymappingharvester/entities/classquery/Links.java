package fr.lirmm.agroportal.ontologymappingharvester.entities.classquery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("nextPage")
    @Expose
    private Object nextPage;
    @SerializedName("prevPage")
    @Expose
    private Object prevPage;

    public Object getNextPage() {
        return nextPage;
    }

    public void setNextPage(Object nextPage) {
        this.nextPage = nextPage;
    }

    public Object getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Object prevPage) {
        this.prevPage = prevPage;
    }

}