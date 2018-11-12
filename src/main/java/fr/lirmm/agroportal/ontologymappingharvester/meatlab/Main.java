package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Main {

    public static void main(String[] args){

        //Parser parser = new Parser();
        //parser.execute("FOODON.json", "foodon","siren");

        SimilarityProcessor sp = new SimilarityProcessor();

        sp.setInputFile("/home/abrahao/data/meatylab/phase2/foodon_siren_siren_maps.txt");

        //sp.generateSimilarityScore();


        int sameFaccetMinimalCount = 3;
        int sameBranchFaccetMinimalCount = 0;
        double alphaWeight = 0.7d;
        double betaWeight = 0.3d;


        //String outFileName = "ex_boeuf";
        //String description = "Boeuf, steak ou bifteck, cru";
        //String description = "Beef, steak or beef steak, raw";
        //String targetFaccets = "A0150 A0794 B1161 C0268 E0124 F0003 G0003 H0003 J0001 K0003 M0001 N0001 P0024 R0001 Z0018";

        //String outFileName = "ex_veau";
        //String description = "Veau, escalope, cuite";
        //String description = "Veal, escalope, cooked";
        //String targetFaccets = "A0150 A0794 B1349 C0268 E0152 F0014 G0001 H0001 J0001 K0003 M0001 N0001 P0024 Z0136";
        //                      A0150 B1349 C0175 E0151 F0003 G0003 H0003 J0001 K0003 M0001 N0001 P0024 Z0018;

        String outFileName = "ex_cheval";
        //String description = "Cheval, entrecôte, grillée/poêlée";
        String description = "Horse, rib steak, grilled/pan-fried";
        String targetFaccets = "A0150 A0794 B1229 C0268 E0152 F0014 G0004 H0001 J0001 K0003 M0001 N0001 P0024 R0001 Z0027 Z0142";





        sp.findFoodOnCandidateConcepts(outFileName,targetFaccets,description,sameFaccetMinimalCount,sameBranchFaccetMinimalCount, alphaWeight,betaWeight);


        //sp.calculateDistanceBetweenProducts(targetFaccets,targetFaccets,0.7,0.3);


    }
}
