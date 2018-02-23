package fr.lirmm.agroportal.ontologymappingharvester;

public class AnnotationAssertationEntity {

    private int id;
    private String assertion;
    private String ontology1;
    private String ontology2;
    private String ontologyConcept1;
    private String ontologyConcept2;


    public AnnotationAssertationEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssertion() {
        return assertion;
    }

    public void setAssertion(String assertion) {
        this.assertion = assertion;
    }

    public String getOntology1() {
        return ontology1;
    }

    public void setOntology1(String ontology1) {
        this.ontology1 = ontology1;
    }

    public String getOntology2() {
        return ontology2;
    }

    public void setOntology2(String ontology2) {
        this.ontology2 = ontology2;
    }

    public String getOntologyConcept1() {
        return ontologyConcept1;
    }

    public void setOntologyConcept1(String ontologyConcept1) {
        this.ontologyConcept1 = ontologyConcept1;
    }

    public String getOntologyConcept2() {
        return ontologyConcept2;
    }

    public void setOntologyConcept2(String ontologyConcept2) {
        this.ontologyConcept2 = ontologyConcept2;
    }

    @Override
    public String toString() {
        return "AnnotationAssertationEntity{" +
                "id=" + id +
                ", assertion='" + assertion + '\'' +
                ", ontology1='" + ontology1 + '\'' +
                ", ontology2='" + ontology2 + '\'' +
                ", ontologyConcept1='" + ontologyConcept1 + '\'' +
                ", ontologyConcept2='" + ontologyConcept2 + '\'' +
                '}';
    }
}
