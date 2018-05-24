package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Contact {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("label")
    @Expose
    private String label;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("email", email).append("label", label).toString();
    }

}