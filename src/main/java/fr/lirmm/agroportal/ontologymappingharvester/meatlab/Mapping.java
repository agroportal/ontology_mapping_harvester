package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Mapping {

   String id;
   String faccet;
   String concept;
   String map;
   String foodid;
   String origfdnam;
   String engfdnam;
   String langualcodes;
   String remarks;
   int originSize;
   int targetSize;
   int sameFaccetCount;
   int sameBranchFaccetCount;
   double score;

    public BigDecimal getScore() {
        return new BigDecimal(score).setScale(2, RoundingMode.HALF_DOWN);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setSameFaccetCount(int sameFaccetCount) {
        this.sameFaccetCount = sameFaccetCount;
    }

    public int getSameBranchFaccetCount() {
        return sameBranchFaccetCount;
    }

    public void setSameBranchFaccetCount(int sameBranchFaccetCount) {
        this.sameBranchFaccetCount = sameBranchFaccetCount;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaccet() {
        return faccet;
    }

    public void setFaccet(String faccet) {
        this.faccet = faccet;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getOrigfdnam() {
        return origfdnam;
    }

    public void setOrigfdnam(String origfdnam) {
        this.origfdnam = origfdnam;
    }

    public String getEngfdnam() {
        return engfdnam;
    }

    public void setEngfdnam(String engfdnam) {
        this.engfdnam = engfdnam;
    }

    public String getLangualcodes() {
        return langualcodes;
    }

    public void setLangualcodes(String langualcodes) {
        this.langualcodes = langualcodes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void calcSimilarityScore(String targetId, double alpha, double beta){

        // Phase 1 - Calculate count of identhical faccets
        int count = 0;
        String[] origin =  getLangualcodes().split(" ");
        String originClean = getLangualcodes();
        originSize = origin.length;
        String[] target = targetId.split(" ");
        String targetClean = targetId;
        targetSize = target.length;
        for(int i =0;i<origin.length;i++){
            if(targetId.indexOf(origin[i])>-1){
                count++;
                originClean = originClean.replace(origin[i],"").replace("  ", " ").trim();
                targetClean = targetClean.replace(origin[i],"").replace("  "," ").trim();
            }
        }
        sameFaccetCount = count;

        // Phase 2 - Calculate count of faccets on the same branch

        int countSameBranch = 0;

        if(originClean.length()>0 && targetClean.length()>0) {

            //System.out.println("-->"+originClean+"<-->"+targetClean+"<--");

            String[] originC = originClean.split(" ");
            String[] targetC = targetClean.split(" ");
            String originBrachChar="";


            for (int i = 0; i < originC.length; i++) {
                originBrachChar = originC[i].substring(0, 1);
                for (int j = 0; j < targetC.length; j++) {
                    if (targetC[j].indexOf(originBrachChar) == 0) {
                        countSameBranch++;
                    }
                }
            }
        }

        sameBranchFaccetCount = countSameBranch;

        score = ((alpha * sameFaccetCount) + (beta * sameBranchFaccetCount)) / (alpha+beta);

    }

    public int getSameFaccetCount(){
        return this.sameFaccetCount;
    }


    public String getLIneFormatedHeader(){
        return "id+;faccet;concept;map;foodid;origfdnam;engfdnam;langualcodes;remarks;originSize;targetSize;sameFaccetCount;sameBranchFaccetCount;score";
    }

    public String getLIneFormated(){
        return ""+id+";"+faccet+";"+concept+";"+map+";"+foodid+";"+origfdnam+";"+engfdnam+";"+langualcodes+";"+remarks+";"+originSize+";"+targetSize+";"+sameFaccetCount+";"+sameBranchFaccetCount+";"+getScoreFormated()+ "\n";
    }

    public BigDecimal getScoreFormated(){
        return new BigDecimal(score).setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "id='" + id + '\'' +
                ", faccet='" + faccet + '\'' +
                ", concept='" + concept + '\'' +
                ", map='" + map + '\'' +
                ", foodid='" + foodid + '\'' +
                ", origfdnam='" + origfdnam + '\'' +
                ", engfdnam='" + engfdnam + '\'' +
                ", langualcodes='" + langualcodes + '\'' +
                ", remarks='" + remarks + '\'' +
                ", originSize=" + originSize +
                ", targetSize=" + targetSize +
                ", sameFaccetCount=" + sameFaccetCount +
                ", sameBranchFaccetCount=" + sameBranchFaccetCount +
                ", score=" + getScoreFormated() +
                '}';
    }
}
