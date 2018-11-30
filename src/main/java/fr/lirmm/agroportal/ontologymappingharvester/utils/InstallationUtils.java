package fr.lirmm.agroportal.ontologymappingharvester.utils;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class InstallationUtils {

    public void checkInstalationFolder(){

        if(getVersion()<1.8){
            printJavaVersionWarnning();
            System.exit(0);
        }


        System.out.println("Working Directory = " +
                System.getProperty("user.home"));
        System.out.println();

        boolean success;

        String folder = System.getProperty("user.home")+ File.separator+"omht"+File.separator+"omht_config";
        String folder2 = System.getProperty("user.home")+File.separator+"omht"+File.separator+"omht_output";

        File f = new File(folder);
        if (!f.exists()) {
            success = (new File(folder)).mkdirs();
            if (!success) {
                System.out.println("Could not create folder for application: "+folder+". Please verify user permissions.");
                System.exit(0);
            }else{
                ManageProperties.setProperty("externalproperties",folder);
                try {
                    FileUtils.copyURLToFile(getResource("OMHT_external_matches_phase_1.cfg"), new File(folder+File.separator+"OMHT_external_matches_phase_1.cfg"));
                }catch(IOException e){
                    e.printStackTrace();
                }
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

        if(ManageProperties.loadPropertyValue("restagroportalurl")==null){
            createProperties();
            System.out.println("Application properties file created with success. Please setup the API keys and User names.");
            System.out.println();
        }


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

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("help.txt").getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result.toString());

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
