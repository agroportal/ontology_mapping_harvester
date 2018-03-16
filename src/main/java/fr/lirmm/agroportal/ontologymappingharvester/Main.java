package fr.lirmm.agroportal.ontologymappingharvester;


import fr.lirmm.agroportal.ontologymappingharvester.services.HarvestAllFormatsService;
import fr.lirmm.agroportal.ontologymappingharvester.services.HarvestOWLFormatService;

public class Main {

    public static void main(String[] args){


//
//        HarvestSKOSFormatService hfsf = new HarvestSKOSFormatService();
//        if(hfsf.loadOntology()){
//            hfsf.findMatches();
//            hfsf.saveFile();
//            hfsf.printResults();
//        }


//        HarvestFromOBOFile hfof = new HarvestFromOBOFile();
//        hfof.loadOntology();
//        hfof.findMatches();
//        hfof.printResults();

//        HarvestAllFormatsService hfowlf = new HarvestAllFormatsService();
//        if(hfowlf.loadOntology()) {
//            hfowlf.findMatches();
//            hfowlf.saveFile();
//
//        }

//        HarvestOWLFormatService hfowlf = new HarvestOWLFormatService();
//        if(hfowlf.loadOntology()) {
//            hfowlf.findMatches();
//            try {
//                hfowlf.shouldUseReasoner();
//                hfowlf.shouldCreateAndReadAnnotations();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            hfowlf.saveFile();
//
//        }

        HarvestAllFormatsService harvestAllFormatsService = new HarvestAllFormatsService();
        if(harvestAllFormatsService.loadOntology()){
            harvestAllFormatsService.findMatches();
            harvestAllFormatsService.saveFile();
        }


    }

}
