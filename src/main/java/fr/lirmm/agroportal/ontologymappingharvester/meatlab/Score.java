package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Score {

    private String concept;
    int sameFaccetCountSum;
    int sameBranchCountSum;
    double scoreSum;
    int conceptCount;

    public Score(String concept) {
        this.sameFaccetCountSum = 0;
        this.sameBranchCountSum = 0;
        this.scoreSum = 0.0;
        this.conceptCount = 0;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getSameFaccetCountSum() {
        return sameFaccetCountSum;
    }

    public void setSameFaccetCountSum(int sameFaccetCountSum) {
        this.sameFaccetCountSum = sameFaccetCountSum;
    }

    public int getSameBranchCountSum() {
        return sameBranchCountSum;
    }

    public void setSameBranchCountSum(int sameBranchCountSum) {
        this.sameBranchCountSum = sameBranchCountSum;
    }

    public BigDecimal getScoreSum() {
        return new BigDecimal(scoreSum).setScale(2, RoundingMode.HALF_DOWN);
    }

    public void setScoreSum(double scoreSum) {
        this.scoreSum = scoreSum;
    }

    public int getConceptCount() {
        return conceptCount;
    }

    public void setConceptCount(int conceptCount) {
        this.conceptCount = conceptCount;
    }

    public BigDecimal getSameFaccetCountAverage(){
        return new BigDecimal(sameFaccetCountSum/conceptCount).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getSameBranchCountAverage(){
        return new BigDecimal(sameBranchCountSum/conceptCount).setScale(2, RoundingMode.HALF_DOWN);
    }

    public BigDecimal getScoreAverage(){
        return new BigDecimal(scoreSum/conceptCount).setScale(2, RoundingMode.HALF_DOWN);
    }

    public void addSameFaccetCount(int c){
        this.sameFaccetCountSum+=c;
    }

    public void addSameBranchCount(int c){
        this.sameBranchCountSum+=c;
    }

    public void addScore(double s){
        this.scoreSum+=s;
    }

    public void addConceptCount(){
        this.conceptCount++;
    }
}
