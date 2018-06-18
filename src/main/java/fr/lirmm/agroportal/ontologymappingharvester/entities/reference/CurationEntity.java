package fr.lirmm.agroportal.ontologymappingharvester.entities.reference;

public class CurationEntity implements Comparable {

    private String number;
    private String targetFounded;
    private String foundedIn;
    private String exampleList;
    private int counter;
    private String ontology;
    private String curatedTarget;
    private String baseClassURI;
    private String curedtedBy;
    private String date;
    private String comments;
    private int status;
    private String mappingProperty;

    public CurationEntity() {
        this.number = "";
        this.targetFounded = "";
        this.foundedIn = "";
        this.exampleList = "";
        this.counter = 0;
        this.ontology = "";
        this.curatedTarget = "";
        this.baseClassURI = "";
        this.curedtedBy = "";
        this.date = "";
        this.comments = "";
        this.status = 0;
        this.mappingProperty = "";
    }

    public CurationEntity(String number, String targetFounded, String foundedIn, String exampleList, int counter, String ontology, String curatedTarget, String baseClassURI, String curedtedBy, String date, String comments, int status, String mappingProperty) {
        this.number = number;
        this.targetFounded = targetFounded;
        this.foundedIn = foundedIn;
        this.exampleList = exampleList;
        this.counter = counter;
        this.ontology = ontology;
        this.curatedTarget = curatedTarget;
        this.baseClassURI = baseClassURI;
        this.curedtedBy = curedtedBy;
        this.date = date;
        this.comments = comments;
        this.status = status;
        this.mappingProperty = mappingProperty;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public void addMatch(){
        ++counter;
    }

    public void addFoundedIn(String acronym){
        acronym = acronym.replaceAll(";","");
        if(foundedIn.indexOf(acronym)==-1){
            if(foundedIn.length()>0) {
                foundedIn += "," + acronym;
            }else{
                foundedIn = acronym;
            }
        }
    }

    public void addExampleList(String example){
        if(example!=null) {
            example = example.replaceAll(";", "");
            if (counter < 10) {
                if (exampleList.indexOf(example) == -1) {
                    if (exampleList.length() > 0) {
                        exampleList += "," + example;
                    } else {
                        exampleList = example;
                    }
                }
            }
        }
    }


    public String getTargetFounded() {
        return targetFounded;
    }

    public void setTargetFounded(String targetFounded) {
        this.targetFounded = targetFounded.replaceAll(";","");
    }

    public String getFoundedIn() {
        return foundedIn;
    }

    public void setFoundedIn(String foundedIn) {
        this.foundedIn = foundedIn.replaceAll(";","");
    }

    public String getExampleList() {
        return exampleList;
    }

    public void setExampleList(String exempleList) {
        this.exampleList = exempleList;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getCuratedTarget() {
        return curatedTarget;
    }

    public void setCuratedTarget(String curatedTarget) {
        this.curatedTarget = curatedTarget;
    }

    public String getBaseClassURI() {
        return baseClassURI;
    }

    public void setBaseClassURI(String baseClassURI) {
        this.baseClassURI = baseClassURI;
    }

    public String getCuredtedBy() {
        return curedtedBy;
    }

    public void setCuredtedBy(String curedtedBy) {
        this.curedtedBy = curedtedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMappingProperty() {
        return mappingProperty;
    }

    public void setMappingProperty(String mappingProperty) {
        this.mappingProperty = mappingProperty;
    }

    @Override
    public int compareTo(Object o) {

        CurationEntity c = (CurationEntity)o;
        if(c.getCounter()>counter){
            return 1;
        }else if(c.getCounter()<counter){
            return -1;
        } else{
            return 0;
        }

    }

    @Override
    public String toString() {
        return "CurationEntity{" +
                "number='" + number + '\'' +
                ", targetFounded='" + targetFounded + '\'' +
                ", foundedIn='" + foundedIn + '\'' +
                ", exampleList='" + exampleList + '\'' +
                ", counter=" + counter +
                ", curatedTarget='" + curatedTarget + '\'' +
                ", baseClassURI='" + baseClassURI + '\'' +
                ", curedtedBy='" + curedtedBy + '\'' +
                ", date='" + date + '\'' +
                ", comments='" + comments + '\'' +
                ", status=" + status +
                ", mappingProperty='" + mappingProperty + '\'' +
                '}';
    }
}
