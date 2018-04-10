package fr.lirmm.agroportal.ontologymappingharvester.entities.reference;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.ExternalReference;

/**
 * Entity collection to represent external references
 */
public class ExtRefList {

    @SerializedName("ExternalReference")
    @Expose
    public ExternalReference[] externalReferences;

    public ExternalReference[] getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(ExternalReference[] externalReferences) {
        this.externalReferences = externalReferences;
    }
}
