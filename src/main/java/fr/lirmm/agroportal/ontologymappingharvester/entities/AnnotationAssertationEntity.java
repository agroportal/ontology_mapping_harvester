package fr.lirmm.agroportal.ontologymappingharvester.entities;

public class AnnotationAssertationEntity {

    private int id;
    private String assertion;
    private String ontology1;
    private String ontology2;
    private String ontologyConcept1;
    private String ontologyConcept2;
    private String ontologyIdPortal;
    private String baseClassURI;
    private String ontology2Curated;


    /**
     * Main object responsable for the mapping representation
     */
    public AnnotationAssertationEntity() {
        // on UNKNOW ONOLOGIES this is necessary due target list verification of null values
        this.ontologyConcept2="";
        this.ontologyIdPortal="";
        this.ontology2Curated="";
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
        this.assertion = assertion.toLowerCase();
    }

    public String getOntology1() {
        return ontology1;
    }

    public void setOntology1(String ontology1) {
        this.ontology1 = ontology1.toLowerCase();
    }

    public String getOntology2() {
        return ontology2;
    }

    public void setOntology2(String ontology2) {
        this.ontology2 = ontology2.toLowerCase();
    }

    public String getOntologyConcept1() {
        return ontologyConcept1;
    }

    public void setOntologyConcept1(String ontologyConcept1) {
        this.ontologyConcept1 = ontologyConcept1.toLowerCase();
    }

    public String getOntologyConcept2() {
        return ontologyConcept2;
    }

    public void setOntologyConcept2(String ontologyConcept2) {
        this.ontologyConcept2 = ontologyConcept2.toLowerCase();
    }

    public String getOntologyIdPortal() {
        return ontologyIdPortal;
    }

    public void setOntologyIdPortal(String ontologyIdPortal) {
        this.ontologyIdPortal = ontologyIdPortal;
    }

    public String getBaseClassURI() {
        return baseClassURI;
    }

    public void setBaseClassURI(String baseClassURI) {
        this.baseClassURI = baseClassURI;
    }

    public String getOntology2Curated() {
        if(ontology2Curated.trim().length()==0){
            return ontology2;
        }else {
            return ontology2Curated;
        }
    }

    public void setOntology2Curated(String ontology2Curated) {
        this.ontology2Curated = ontology2Curated;
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
                ", ontologyIdPortal='" + ontologyIdPortal + '\'' +
                ", baseClassURI='" + baseClassURI + '\'' +
                ", ontology2Curated='" + ontology2Curated + '\'' +
                '}';
    }

    public String toStringFlat() {
        return assertion.replaceAll(";","") + ";"+ ontology1.replaceAll(";","") + ";" +ontologyConcept1.replaceAll(";","")+";"+ontology2.replaceAll(";","") + ";" + ontologyConcept2.replaceAll(";","");
    }
}
