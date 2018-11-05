package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Main {

    public static void main(String[] args){

        Parser parser = new Parser();
        parser.execute("FOODON.json", "foodon","siren");
        //parser.execute("FOODON.json", "foodon","USDA_Standard_Reference_8");

        //SimilarityProcessor sp = new SimilarityProcessor();

        //sp.setInputFile("/home/abrahao/data/meatylab/foodon_langual_CIQUAL_2017_EuroFIR_2018_04_18_LanguaL_maps.txt");

        //sp.generateSimilarityScore();

        String targetFaccets = "A0273 A0810 B1457 C0253 E0119 F0001 G0003 H0001 J0001 K0003 M0001 N0001 P0024 R0001 Z0002";
        int sameFaccetMinimalCount = 10;
        int sameBranchFaccetMinimalCount = 0;
        double alphaWeight = 0.7d;
        double betaWeight = 0.3d;

        //sp.findFoodOnCandidateConcepts(targetFaccets,sameFaccetMinimalCount,sameBranchFaccetMinimalCount, alphaWeight,betaWeight);


        //sp.calculateDistanceBetweenProducts(targetFaccets,targetFaccets,0.7,0.3);


    }
}
