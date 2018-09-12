package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Class {

    @SerializedName("@id1")
    @Expose
    private String id1;
    @SerializedName("@type1")
    @Expose
    private String type1;
    @SerializedName("@id2")
    @Expose
    private String id2;
    @SerializedName("@type2")
    @Expose
    private String type2;
    @SerializedName("@context")
    @Expose
    private Context context;

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

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Class{" +
                "id1='" + id1 + '\'' +
                ", type1='" + type1 + '\'' +
                ", id2='" + id2 + '\'' +
                ", type2='" + type2 + '\'' +
                ", context=" + context +
                '}';
    }
}