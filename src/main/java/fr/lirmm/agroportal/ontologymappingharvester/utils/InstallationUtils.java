package fr.lirmm.agroportal.ontologymappingharvester.utils;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class InstallationUtils {

    public void checkInstalationFolder(){

        if(getVersion()<1.8){
            printJavaVersionWarnning();
            System.exit(0);
        }


        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        System.out.println();

        boolean success;

        String folder = System.getProperty("user.dir")+ File.separator+"omht_config";
        String folder2 = System.getProperty("user.dir")+File.separator+"omht_output";

        File f = new File(folder);
        if (!f.exists()) {
            success = (new File(folder)).mkdirs();
            if (!success) {
                System.out.println("Could not create folder for application: "+folder+". Please verify user permissions.");
                System.exit(0);
            }else{
                ManageProperties.setProperty("externalproperties",folder);

            }
        }

        f = new File(folder2);
        if(!f.exists()) {
            success = (new File(folder2)).mkdirs();
            if (!success) {
                System.out.println("Could not create folder for application: " + folder2 + ". Please verify user permissions.");
                System.exit(0);
            } else {
                ManageProperties.setProperty("outputfolder", folder2);
            }
        }

        createProperties();

        try {
            FileUtils.copyURLToFile(getResource("OMHT_external_matches_phase_1.cfg"), new File(folder+File.separator+"OMHT_external_matches_phase_1.cfg"));
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Application properties file created with success. Please setup the API keys and User names.");
        System.out.println();
        printHelp();



    }

    static double getVersion () {
        String version = System.getProperty("java.version");
        int pos = version.indexOf('.');
        pos = version.indexOf('.', pos+1);
        return Double.parseDouble (version.substring (0, pos));
    }

    private void printJavaVersionWarnning(){
        System.out.println("-----------------------------");
        System.out.println("        ATTENTION!!");
        System.out.println("-----------------------------");
        System.out.println("Current Java Version  : "+getVersion());
        System.out.println("Required Java Version : 1.8");
        System.out.println("Please, install the required");
        System.out.println("Java package and run the app");
        System.out.println("again.");
        System.out.println("-----------------------------");



    }

    private void createProperties(){

        ManageProperties.setProperty("restagroportalapikey","");
        ManageProperties.setProperty("reststageagroportalapikey","");
        ManageProperties.setProperty("restbioportalapikey","");
        ManageProperties.setProperty("reststagebioportalapikey","");
        ManageProperties.setProperty("restagroportaluser","");
        ManageProperties.setProperty("reststageagroportaluser","");
        ManageProperties.setProperty("restbiopotaluser","");
        ManageProperties.setProperty("reststagebiopotaluser","");
        ManageProperties.setProperty("restagroportalurl","http://data.agroportal.lirmm.fr");
        ManageProperties.setProperty("reststageagroportalurl","http://data.stageportal.lirmm.fr/");
        ManageProperties.setProperty("restbioportalurl","https://data.bioontology.org");
        ManageProperties.setProperty("reststagebioportalurl","http://data.bioontology.org");
        ManageProperties.setProperty("obofoundryontologies","http://obofoundry.org/registry/ontologies.jsonld");
        ManageProperties.setProperty("identifiersaddress","http://identifiers.org");
        ManageProperties.setProperty("minimummappings","10");
        ManageProperties.setProperty("executionhistory",";PO2_DG;");

    }

    /**
     * Method to show help context for command prompt
     */
    public void printHelp(){

        System.out.println("Parameters:");
        System.out.println("./MappingHarvest [parameters] FILE1 FILE2 FILE3");
        System.out.println("-j Generate JSON files");
        System.out.println("-l Generate LOG files");
        System.out.println("-s Gerate Statistics");
        System.out.println("-p print LOG on screen");
        System.out.println("-b bulk load files from folder");
        System.out.println("-g Generate javascript for Graph representation of matches");
        System.out.println("-d Download ontologies from AGROPORTAL");
        System.out.println("-n dowload ontologies from BIOPORTAL");
        System.out.println("-h Download ontologies from STAGE AGROPORTAL");
        System.out.println("-f dowload ontologies from STAGE BIOPORTAL");
        System.out.println("-u Download an unique ontology (require destination folder and acronym)");
        System.out.println("-vj Validate IRIs and generate definitive JSON files on output folder");
        System.out.println("-r Setup location folder for configuration files (.cfg)");
        System.out.println("-ka Setup api key to acess Agroportal from this script");
        System.out.println("-kb Setup api key to acess Bioportal from this script");
        System.out.println("-f Setup output folder");
        System.out.println("-aa Setup AGROPORTAL API ADDRESS");
        System.out.println("-ab Setup BIOPORTAL API ADDRESS");
        System.out.println("-aas Setup STAGE AGROPORTAL API ADDRESS");
        System.out.println("-abs Setup STAGE BIOPORTAL API ADDRESS");
        System.out.println("-rest[agroportal,stageagroportal,bioportal,stagebioportal] [post,patch,delete] ONTOLOGY_ACRONYMS_LIST to manage matches on Agroportal");
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


    public URL getResource(String resource){

        URL url ;

        //Try with the Thread Context Loader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader != null){
            url = classLoader.getResource(resource);
            if(url != null){
                return url;
            }
        }

        //Let's now try with the classloader that loaded this class.
        classLoader = Loader.class.getClassLoader();
        if(classLoader != null){
            url = classLoader.getResource(resource);
            if(url != null){
                return url;
            }
        }

        //Last ditch attempt. Get the resource from the classpath.
        return ClassLoader.getSystemResource(resource);
    }

    public static void main(String[] args){

        InstallationUtils ui = new InstallationUtils();

        File source = new File(ui.getResource("OMHT_external_matches_phase_1.cfg").toString());
        File dest = new File(ManageProperties.loadPropertyValue("externalproperties") +File.separator+"OMHT_external_matches_phase_2.cfg");
        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
