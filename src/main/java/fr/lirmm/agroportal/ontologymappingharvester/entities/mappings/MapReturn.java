package fr.lirmm.agroportal.ontologymappingharvester.entities.mappings;

public class MapReturn{

    public String id1;
    public String id2;
    public String relation;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getId1() {
        return id1+id2+relation;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2+id1+relation;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }
}