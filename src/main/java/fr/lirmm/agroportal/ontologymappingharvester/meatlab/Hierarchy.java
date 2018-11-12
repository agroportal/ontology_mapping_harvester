package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Hierarchy {

    private String concept;
    private String descriptionFoodon;
    private int depth;
    private String parents;

    public String getDescriptionFoodon() {
        return descriptionFoodon;
    }

    public void setDescriptionFoodon(String descriptionFoodon) {
        this.descriptionFoodon = descriptionFoodon;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getLineFormated() {
        return  concept + ";" + depth +";" + parents;
    }

    @Override
    public String toString() {
        return  "concept: " + concept + " depth: " + depth +" parents: " + parents;
    }
}
