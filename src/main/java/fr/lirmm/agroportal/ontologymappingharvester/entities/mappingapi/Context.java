package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("@vocab")
    @Expose
    private String vocab;
    @SerializedName("prefLabel")
    @Expose
    private String prefLabel;

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public String getPrefLabel() {
        return prefLabel;
    }

    public void setPrefLabel(String prefLabel) {
        this.prefLabel = prefLabel;
    }

    @Override
    public String toString() {
        return "Context{" +
                "vocab='" + vocab + '\'' +
                ", prefLabel='" + prefLabel + '\'' +
                '}';
    }
}