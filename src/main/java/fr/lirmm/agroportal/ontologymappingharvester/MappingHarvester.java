package fr.lirmm.agroportal.ontologymappingharvester;

import fr.lirmm.agroportal.ontologymappingharvester.services.GenerateJSService;
import fr.lirmm.agroportal.ontologymappingharvester.services.HarvestAllFormatsService;
import fr.lirmm.agroportal.ontologymappingharvester.services.ValidateIRIService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;

import java.util.ArrayList;

public class MappingHarvester {

    /**
     * Main method call to run the script
     * @param args
     */
    public static void main (String[] args){


        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ontology Mapping Harvest Tool - v.1.0 - Agroportal Project - LIRMM - Montpellier - FR");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println();

        String command="";
        ArrayList<String> files = new ArrayList<>();
        if(args.length==0 || args[0].equalsIgnoreCase("-help")){

            printHelp();
            System.exit(0);


        }else{

            command = args[0].replace("-","");
            if(args.length==0){
                if(command.indexOf("-")==-1){
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

            }else if(command.indexOf("v")>-1){

                ValidateIRIService validateIRIService = new ValidateIRIService();
                validateIRIService.setupLogProperties("","","");
                validateIRIService.loadAndProcessOntologiesMetadata(command);
                validateIRIService.validateIRIs(command,files);

            }else if(command.indexOf("r")>-1){

                ManageProperties.setProperty("externalproperties",args[1]);

                System.out.println("New folder for external_references.json file is "+args[1]);

            }else if(command.indexOf("ka")>-1){

                ManageProperties.setProperty("apikey",args[1]);

                System.out.println("New api key for Agroportal associated to this script is "+args[1]);

            }else if(command.indexOf("kb")>-1){

                ManageProperties.setProperty("apikeybio",args[1]);

                System.out.println("New api key for Bioportal associated to this script is "+args[1]);

            }else if(command.indexOf("f")>-1){

                ManageProperties.setProperty("outputfolder",args[1]);

                System.out.println("New output folder associated to this script is "+args[1]);

            }else if(command.indexOf("aa")>-1){

                ManageProperties.setProperty("agroportaladdress",args[1]);

                System.out.println("New address to AGROPORTAL: "+args[1]);

            }else if(command.indexOf("ab")>-1){

                ManageProperties.setProperty("bioportaladdress",args[1]);

                System.out.println("New address to BIOPORTAL: "+args[1]);

            }else {



                HarvestAllFormatsService service = new HarvestAllFormatsService();
                service.setupLogProperties("","","");
                service.loadExternalReferences();
                service.loadExternalTargetReferences();
                service.loadAndProcessOntologiesMetadata(command);

                if(command.indexOf("c")>-1){
                    service.deleteExecutionHistory();
                    System.out.println("Execution history deleted. New execution history as initiated.");
                }


                if(command.indexOf("d")>-1){

                    service.parse(command, ManageProperties.loadPropertyValue("outputfolder"));
                }else if(command.indexOf("u")>-1){

                    service.parse(command, ManageProperties.loadPropertyValue("outputfolder"), args[1]);
                }else  {
                    service.parse(command, files);
                }
            }


        }






    }

    private static void printHelp(){

        System.out.println("Parameters:");
        System.out.println("./MappingHarvest [parameters] FILE1 FILE2 FILE3");
        System.out.println("-j Generate JSON files");
        System.out.println("-l Generate LOG files");
        System.out.println("-s Gerate Statistics");
        System.out.println("-p print LOG on screen");
        System.out.println("-b bulk load files from folder");
        System.out.println("-g Generate javascript for Graph representation of matches");
        System.out.println("-d Download ontologies from Agroportal (require destination folder)");
        System.out.println("-n dowload ontologies from BIOPORTAL");
        System.out.println("-u Download an unique ontology (require destination folder and acronym)");
        System.out.println("-vj Validate IRIs and generate definitive JSON files on output folder");
        System.out.println("-r Setup location folder for external_reference.json file");
        System.out.println("-ka Setup api key to acess Agroportal from this script");
        System.out.println("-kb Setup api key to acess Bioportal from this script");
        System.out.println("-f Setup output folder");
        System.out.println("-aa Setup AGROPORTAL API ADDRESS");
        System.out.println("-ab Setup BIOPORTAL API ADDRESS");
        System.out.println("-c Clean execution history for the current script execution");
        System.out.println("Examples of usage:");
        System.out.println("/.MappingHarvest -jlsp path1/onto1.ref path2/onto2.ref pathn/onto3.ref");
        System.out.println("/.MappingHarvest -bjlsp path_to_folder_of_xrdf_files");
        System.out.println("/.MappingHarvest -g path_to_sts_files");
        System.out.println("/.MappingHarvest -djslp path_to_output_files");
        System.out.println("/.MappingHarvest -dnjslp");
        System.out.println("/.MappingHarvest -ujslp ACRONYM");
        System.out.println("/.MappingHarvest -vj (list of files) if not informed process all files on folder");
        System.out.println("/.MappingHarvest -r path_to_external_reference_file");
        System.out.println("/.MappingHarvest -ka api_key");
        System.out.println("/.MappingHarvest -f path_to_output_folder");
        System.out.println("/.MappingHarvest -aa http://data.agroportal.lirmm.fr");
        System.out.println("/.MappingHarvest -ab http://data.bioontology.org");
        System.out.println("The output files will have the same names as the input files and the");
        System.out.println("follow sufixes:");
        System.out.println(".json - JSON file for upload back on the portals");
        System.out.println(".log  - LOG file of mapping process");
        System.out.println(".sts  - text file with mapping statistics");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("");
    }


}
