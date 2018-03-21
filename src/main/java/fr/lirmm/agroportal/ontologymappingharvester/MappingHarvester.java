package fr.lirmm.agroportal.ontologymappingharvester;

import fr.lirmm.agroportal.ontologymappingharvester.services.GenerateJSService;
import fr.lirmm.agroportal.ontologymappingharvester.services.HarvestAllFormatsService;

import java.util.ArrayList;

public class MappingHarvester {

    public static void main (String[] args){


        String command="";
        ArrayList<String> files = new ArrayList<>();
        if(args.length==0 || args[0].equalsIgnoreCase("-help")){

            printHelp();
            System.exit(0);


        }else{
            command = args[0].replace("-","");
            if(args.length==1){
                if(command.indexOf("-")==-1 || command.length()==1){
                    System.out.println();
                    System.out.println("Attention: No parameters provided! Nothing to do!");
                    printHelp();
                    System.exit(0);
                }
                System.out.println();
                System.out.println("Attention: You must provide, at least, one input file to be processed!");
                printHelp();
                System.exit(0);
            }else{
                for(int i=1;i<args.length;i++){
                    files.add(args[i]);
                }
            }


            if(command.indexOf("g")>-1) {

                GenerateJSService generateJSService = new GenerateJSService();
                generateJSService.generateJs(args[1]);

            }else {
                HarvestAllFormatsService service = new HarvestAllFormatsService();
                service.parse(command, files);
            }


        }






    }

    private static void printHelp(){
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Ontology Mapping Harvest Tool - v.1.0 - Agroportal Project - LIRMM - FR");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("Parameters:");
        System.out.println("./MappingHarvest [parameters] FILE1 FILE2 FILE3");
        System.out.println("-j Generate JSON files");
        System.out.println("-l Generate LOG files");
        System.out.println("-s Gerate Statistics");
        System.out.println("-p print LOG on screen");
        System.out.println("-a Append LOG and/or Statistics to a single file");
        System.out.println("-g Generate javascript for Graph representation of matches");
        System.out.println("Example of usage:");
        System.out.println("/.MappingHarvest -jlsap path1/onto1.ref path2/onto2.ref pathn/onto3.ref");
        System.out.println("/.MappingHarvest -g path_to_sts_files");
        System.out.println("The output files will have the same names as the input files and the");
        System.out.println("follow sufixes:");
        System.out.println(".json - JSON file for upload on AGROPORTAL");
        System.out.println(".log  - LOG file of mapping process");
        System.out.println(".sts  - text file with mapping statistics");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("");
    }


}
