package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Class {

    @SerializedName("prefLabel")
    @Expose
    private String prefLabel;
    @SerializedName("@id")
    @Expose
    private String id1;
    @SerializedName("@type")
    @Expose
    private String type1;
    @SerializedName("@context")
    @Expose
    private Context context;

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("prefLabel", prefLabel).append("id1", id1).append("type1", type1).append("context", context).toString();
    }

}