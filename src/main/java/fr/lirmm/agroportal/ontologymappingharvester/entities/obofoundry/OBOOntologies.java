package fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OBOOntologies {

    @SerializedName("@context")
    @Expose
    private String context;
    @SerializedName("ontologies")
    @Expose
    private List<Ontology> ontologies = null;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<Ontology> getOntologies() {
        return ontologies;
    }

    public void setOntologies(List<Ontology> ontologies) {
        this.ontologies = ontologies;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("context", context).append("ontologies", ontologies).toString();
    }

}