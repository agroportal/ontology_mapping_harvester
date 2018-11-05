package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Hierarchy {

    private String concept;
    private int depth;
    private String parents;

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

    @Override
    public String toString() {
        return "Hierarchy{" +
                "concept='" + concept + '\'' +
                ", depth=" + depth +
                ", parents='" + parents + '\'' +
                '}';
    }
}
