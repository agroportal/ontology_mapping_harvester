package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links_ {

    @SerializedName("metrics")
    @Expose
    private String metrics;
    @SerializedName("@context")
    @Expose
    private Context__ context;

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public Context__ getContext() {
        return context;
    }

    public void setContext(Context__ context) {
        this.context = context;
    }

}