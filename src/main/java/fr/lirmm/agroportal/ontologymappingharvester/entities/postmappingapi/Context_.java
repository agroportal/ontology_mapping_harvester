package fr.lirmm.agroportal.ontologymappingharvester.entities.postmappingapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context_ {

    @SerializedName("@vocab")
    @Expose
    private String vocab;
    @SerializedName("obsolete")
    @Expose
    private String obsolete;

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public String getObsolete() {
        return obsolete;
    }

    public void setObsolete(String obsolete) {
        this.obsolete = obsolete;
    }

}