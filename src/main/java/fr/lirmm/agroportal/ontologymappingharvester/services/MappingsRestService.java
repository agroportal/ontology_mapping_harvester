package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.Collection;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.RestMappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingsRestService extends LogService{


    private String command;

    public MappingsRestService(String command) {
        this.command = command;
    }

    public void postMappings(String[] args){

        List<File> files;
        //System.out.println(args.length);
        setupLogProperties(this.command,args[2],ManageProperties.loadPropertyValue("outputfolder"));

        if(args.length>=3){

            System.out.println("-->"+args[0]+" "+args[1]+" "+args[2]);
            if(args[2].equalsIgnoreCase("all")){
                files = getReferencesForJSONFiles(ManageProperties.loadPropertyValue("outputfolder"));
                for(File file: files){
                    doPost(file);
                }
            }else{
                for(int i = 3;i<args.length;i++){
                    doPost(args[i]);
                }
            }
        }
    }



    public void patchMappings(String[] args){

    }

    public void deleteMappings(String[] args){


        List<String> ids = new ArrayList<>();

        setupLogProperties(this.command,args[2],ManageProperties.loadPropertyValue("outputfolder"));

        // FASE 1 - Identify mappings created by the script for this ontology
        stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 1 - Identifing Mappings");

        if(args.length>=3){

            AgroportalRestService service = new AgroportalRestService();

            System.out.println("Arg: "+args[0].replaceAll("-",""));
            String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");
            System.out.println("User: "+user);

            int page = 1;
            Integer nextPage = null;

            do {


                RestMappingEntity mappings = service.getAllRestMappings(args[0].replaceAll("-", ""), args[2], page);

                if (mappings != null) {
                    for (Collection collections : mappings.getCollection()) {
                        if (collections.getId1() != null && collections.getProcess().getCreator() != null) {

                            if (collections.getProcess().getCreator().indexOf(user) > -1) {
                                ids.add(collections.getId1());
                            }

                            //System.out.println("id: " + collections.getId1() + " Creator: " + collections.getProcess().getCreator());
                        } else {
                            //System.out.println(collections.toString());
                        }

                    }
                    nextPage = mappings.getNextPage();
                    if(nextPage!=null){
                        page = nextPage.intValue();
                    }
                } else {
                    errorLogger.error("ERROR: Ontology Mappings Identification for : "+args[2]+" page: "+page+ " is missing: Internal Server Error. Page will be igonered.");
                    page++;
                    nextPage = page;
                }
                System.out.println("Page: "+page);
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }while(nextPage!=null);


            // FASE 2 Intereact over mappings created by this script to delete them.
            stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 2 - Delete Mappings");

            int counter = 0;
            for(String id: ids){
                service.deleteMapping(args[0],id);
                counter++;
            }
            System.out.println("Total mappings delete for: "+args[2]+" -->"+ counter);
            stdoutLogger.info("Total mappings delete for: "+args[2]+" -->"+ counter);
        }
    }


    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "json" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
        return files;
    }


    private void doPost(String acronym){

        String folder = ManageProperties.loadPropertyValue("outputfolder");
        File file = new File(folder+File.separator+acronym+".json");
        doPost(file);

    }

    private void doPost(File file){

        AgroportalRestService ars = new AgroportalRestService();

        MappingEntity[] maps = loadJSONFile(file);

        String result="";

        int counter=0;
        for(MappingEntity me: maps){
            result = ars.postMappings(me, this.command);
            System.out.println("Retorno: "+result);
            counter++;
            if(counter%5==0){
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }


    public MappingEntity[] loadJSONFile(File file){

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();
        MappingEntity[] mappingE = null;


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String everything = sb.toString();
            Gson gson = new GsonBuilder().create();
            mappingE = gson.fromJson(everything, MappingEntity[].class);

            System.out.println("Tamanho: "+mappingE.length);


        } catch (IOException e) {
            System.out.println("Error trying to load external references JSON file located in: "+file.toString()+" - "+e.getMessage());
        }

        return mappingE;
    }

}
