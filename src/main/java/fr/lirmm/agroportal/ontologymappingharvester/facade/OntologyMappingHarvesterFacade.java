package fr.lirmm.agroportal.ontologymappingharvester.facade;

import fr.lirmm.agroportal.ontologymappingharvester.services.GenerateJSService;
import fr.lirmm.agroportal.ontologymappingharvester.services.HarvestAllFormatsService;
import fr.lirmm.agroportal.ontologymappingharvester.services.MappingsRestService;
import fr.lirmm.agroportal.ontologymappingharvester.services.ValidadeTargetReferenceService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.InstallationUtils;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class OntologyMappingHarvesterFacade {


    private static final int PROMPTCALLSTART=1;
    private static final int INTERNALLCALLSTART=0;


    /**
     * Facade mathod to call script from command prompt
     * @param args
     */
    public void promptCall(String[] args){

        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("Ontology Mapping Harvest Tool - v.1.3 - Agroportal Project - LIRMM - Montpellier - FR");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println();


        InstallationUtils iu = new InstallationUtils();

        iu.checkInstalationFolder();


        String command="";
        ArrayList<String> files = new ArrayList<>();
        if(args.length==0 || args[0].equalsIgnoreCase("-help")){

            iu.printHelp();
            System.exit(0);


        }else{

            command = args[0].replaceAll("-","");
            if(args.length==0){
                if(command.indexOf("-")==-1){
                    System.out.println();
                    System.out.println("Attention: No parameters provided! Nothing to do!");
                    iu.printHelp();
                    System.exit(0);
                }
                System.out.println();
                System.out.println("Attention: You must provide, at least, one input file to be processed!");
                iu.printHelp();
                System.exit(0);
            }else{
                for(int i=1;i<args.length;i++){
                    files.add(args[i]);
                }
            }


            if(command.indexOf("rest")>-1){

                MappingsRestService mrs = new MappingsRestService(args[0]);

                if(args[1].equalsIgnoreCase("post")){

                    mrs.postMappings(args);

                }else if(args[1].equalsIgnoreCase("patch")){

                }else if(args[1].equalsIgnoreCase("delete")){
                    mrs.deleteMappings(args);
                }


            }else if(command.indexOf("g")>-1) {

                GenerateJSService generateJSService = new GenerateJSService();
                generateJSService.generateJs(args[1], args[2]);

            }else if(command.indexOf("v")>-1){

//                ValidateIRIService validateIRIService = new ValidateIRIService();
//                validateIRIService.setupLogProperties("","","");
//                validateIRIService.loadAndProcessOntologiesMetadata(command);
//                validateIRIService.validateIRIs(command,files);

                ValidadeTargetReferenceService validadeTargetReferenceService = new ValidadeTargetReferenceService();
                validadeTargetReferenceService.setupLogProperties("","","");
                validadeTargetReferenceService.loadAndProcessOntologiesMetadata(command);
                validadeTargetReferenceService.validateTargetReferences(command,files);

            }else if(command.indexOf("r")>-1){

                ManageProperties.setProperty("externalproperties",args[1]);

                System.out.println("New folder for external_references.json file is "+args[1]);

            }else if(command.indexOf("restagroportalapikey")>-1){

                ManageProperties.setProperty("restagroportalapikey",args[1]);

                System.out.println("New api key for Agroportal associated to this script is "+args[1]);

            }else if(command.indexOf("restbioportalapikey")>-1){

                ManageProperties.setProperty("restbioportalapikey",args[1]);

                System.out.println("New api key for Bioportal associated to this script is "+args[1]);

            }else if(command.indexOf("f")>-1){

                ManageProperties.setProperty("outputfolder",args[1]);

                System.out.println("New output folder associated to this script is "+args[1]);

            }else if(command.indexOf("aas")>-1){

                ManageProperties.setProperty("reststageagroportalurl",args[1]);

                System.out.println("New address to STAGEAGROPORTAL: "+args[1]);

            }else if(command.indexOf("abs")>-1){

                ManageProperties.setProperty("reststagebioportalurl",args[1]);

                System.out.println("New address to STAGEBIOPORTAL: "+args[1]);

            }else if(command.indexOf("restagroportalurl")>-1){

                ManageProperties.setProperty("restagroportalurl",args[1]);

                System.out.println("New address to AGROPORTAL: "+args[1]);

            }else if(command.indexOf("restbioportalurl")>-1){

                ManageProperties.setProperty("restbioportalurl",args[1]);

                System.out.println("New address to BIOPORTAL: "+args[1]);

            }else if(command.indexOf("restagroportaluser")>-1){

                ManageProperties.setProperty("restagroportaluser",args[1]);

                System.out.println("New user to AGROPORTAL: "+args[1]);

            }else if(command.indexOf("restbioportaluser")>-1){

                ManageProperties.setProperty("restbioportaluser",args[1]);

                System.out.println("New user to BIOPORTAL: "+args[1]);

            }else{



                HarvestAllFormatsService service = new HarvestAllFormatsService();
                service.setPrintToConsole(true);
                service.setupLogProperties("","","");
                //service.loadExternalReferences();
                service.loadExternalTargetReferences();
                service.loadAndProcessOntologiesMetadata(command);

                if(command.indexOf("c")>-1){
                    service.deleteExecutionHistory();
                    service.deleteLogFiles();
                    System.out.println("Log files deleted deleted. New LOG files created.");
                    System.out.println("Execution history deleted. New execution history created.");
                }


                if(command.indexOf("d")>-1){

                    service.parse(command, ManageProperties.loadPropertyValue("outputfolder"));
                }else if(command.indexOf("u")>-1){

                    service.parse(command, ManageProperties.loadPropertyValue("outputfolder"), args,PROMPTCALLSTART);
                }else  {
                    service.parse(command, files);
                }
            }


        }

    }




        /**
         * Method to be called from other application on the same project context
         * @param acronyms - List of AGROPORTAL acronyms to harvest mappings (UPPER CASE)
         */
    public void harvestFromAgroportal(String[] acronyms){


        HarvestAllFormatsService service = new HarvestAllFormatsService();
        service.setupLogProperties("","","");
        //service.loadExternalReferences();
        service.loadExternalTargetReferences();
        service.loadAndProcessOntologiesMetadata("");
        service.parse("jlsu", ManageProperties.loadPropertyValue("outputfolder"), acronyms, INTERNALLCALLSTART);


    }

    /**
     * Method to harvest all ontologies on AGROPORTAL
     */
    public void harvestAllOntologiesFromAgroportal(){

        HarvestAllFormatsService service = new HarvestAllFormatsService();
        service.setPrintToConsole(false);
        service.setupLogProperties("","","");
        //service.loadExternalReferences();
        service.loadExternalTargetReferences();
        service.loadAndProcessOntologiesMetadata("");
        service.parse("jlsd", ManageProperties.loadPropertyValue("outputfolder"));

    }




}
