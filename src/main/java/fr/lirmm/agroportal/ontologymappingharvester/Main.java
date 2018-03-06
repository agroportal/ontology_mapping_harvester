package fr.lirmm.agroportal.ontologymappingharvester;


public class Main {

    public static void main(String[] args){



        HarvestFromSKOSFile hfsf = new HarvestFromSKOSFile();
        if(hfsf.loadOntology()){
            hfsf.findMatches();
            hfsf.saveFile();
            hfsf.printResults();
        }


//        HarvestFromOBOFile hfof = new HarvestFromOBOFile();
//        hfof.loadOntology();
//        hfof.findMatches();
//        hfof.printResults();

//        HarvestFromOWLFile hfowlf = new HarvestFromOWLFile();
//        hfowlf.loadOntology();
//        hfowlf.findMatches();
//        hfowlf.printResults();


    }

}
