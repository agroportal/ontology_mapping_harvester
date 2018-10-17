package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Mapping {

   String id;
   String faccet;
   String concept;
   String map;
   String ciqual_id;
   String description_french;
   String description_english;
   String obs;
   String faccet_list;
   String complete;
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

    public String getCiqual_id() {
        return ciqual_id;
    }

    public void setCiqual_id(String ciqual_id) {
        this.ciqual_id = ciqual_id;
    }

    public String getDescription_french() {
        return description_french;
    }

    public void setDescription_french(String description_french) {
        this.description_french = description_french;
    }

    public String getDescription_english() {
        return description_english;
    }

    public void setDescription_english(String description_english) {
        this.description_english = description_english;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getFaccet_list() {
        return faccet_list;
    }

    public void setFaccet_list(String faccet_list) {
        this.faccet_list = faccet_list;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public void calcSimilarityScore(String targetId, double alpha, double beta){

        // Phase 1 - Calculate count of identhical faccets
        int count = 0;
        String[] origin =  getFaccet_list().split(" ");
        String originClean = getFaccet_list();
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

    @Override
    public String toString() {
        return "Mapping{" +
                "concept='" + concept + '\'' +
                ", ciqual_id='" + ciqual_id + '\'' +
                ", faccet_list='" + faccet_list + '\'' +
                ", originSize=" + originSize +
                ", targetSize=" + targetSize +
                ", sameFaccetCount=" + sameFaccetCount +
                ", sameBranchFaccetCount=" + sameBranchFaccetCount +
                ", score=" + new BigDecimal(score).setScale(2, RoundingMode.HALF_DOWN) +
                '}';
    }
}
