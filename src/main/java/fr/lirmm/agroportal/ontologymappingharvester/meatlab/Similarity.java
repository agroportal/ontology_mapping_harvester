package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Similarity {

    public String concept;
    public String originId;
    public String targetId;
    public String originFaccet;
    public int originSize;
    public String targetFaccet;
    public int targetSize;
    public int score;

    public int getOriginSize() {
        return originSize;
    }

    public void setOriginSize(int originSize) {
        this.originSize = originSize;
    }

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getOriginFaccet() {
        return originFaccet;
    }

    public void setOriginFaccet(String originFaccet) {
        this.originFaccet = originFaccet;
    }

    public String getTargetFaccet() {
        return targetFaccet;
    }

    public void setTargetFaccet(String targetFaccet) {
        this.targetFaccet = targetFaccet;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
