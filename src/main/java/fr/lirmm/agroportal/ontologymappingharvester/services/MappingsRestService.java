package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.Collection;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.RestMappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.CurationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MappingsRestService extends LogService{


    private String command;

    public MappingsRestService(String command) {
        this.command = command;
    }

    public void postMappings(String[] args){

        List<File> files;
        //System.out.println(args.length);
        setupLogProperties(this.command+ "pl",args[2],ManageProperties.loadPropertyValue("outputfolder"));

        if(args.length>=3){

            System.out.println("-->"+args[0]+" "+args[1]+" "+args[2]);
            if(args[2].equalsIgnoreCase("all")){
                files = getReferencesForJSONFiles(ManageProperties.loadPropertyValue("outputfolder"));
                for(File file: files){
                    doPost(file, args);
                }
            }else{
                    doPost(args);
            }
        }
    }



    public void patchMappings(String[] args){

    }

    public void deleteMappings(String[] args){


        HashMap<String,MappingEntity> list = new HashMap<>();

        setupLogProperties(this.command,args[2],ManageProperties.loadPropertyValue("outputfolder"));

        // FASE 1 - Identify mappings created by the script for this ontology
        stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 1 - Identifing Mappings");

        if(args.length>=3){

            AgroportalRestService service = new AgroportalRestService();

            System.out.println("Arg: "+args[0].replaceAll("-",""));
            String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");
            System.out.println("User: "+user);

            list = getInternalMappings(args,user);


            // FASE 2 Intereact over mappings created by this script to delete them.
            stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 2 - Delete Mappings");

            int counter = 0;

            String key="";
            MappingEntity me = null;
            for (Map.Entry<String, MappingEntity> entry : list.entrySet()) {
                key = entry.getKey();
                me = entry.getValue();
                service.deleteMapping(args[0],me.getMapId(),args[2]);
                counter++;

            }


            System.out.println("Total mappings delete for: "+args[2]+" -->"+ counter);
            stdoutLogger.info("Total mappings delete for: "+args[2]+" -->"+ counter);
        }
    }


    private HashMap<String,MappingEntity> getInternalMappings(String[] args, String user){

        HashMap<String,MappingEntity> list = new HashMap<String,MappingEntity>();
        MappingEntity me = null;
        String[] relations = new String[10];
        HashMap<String,String> classes =null;

        int page = 1;
        Integer nextPage = 0;
        Integer totalPages = null;

        AgroportalRestService service = new AgroportalRestService();

        do {


            RestMappingEntity mappings = service.getAllRestMappings(args[0].replaceAll("-", ""), args[2], page);



            if (mappings != null) {

                if(mappings.getPageCount()!=null){
                    totalPages = mappings.getPageCount();
                }

                for (Collection collections : mappings.getCollection()) {
                    if (collections.getId() != null && collections.getProcess()!=null && collections.getProcess().getCreator() != null) {


                        if (collections.getProcess().getCreator().indexOf(user) > -1) {

                            classes = new HashMap<>();
                            me = new MappingEntity();
                            me.setMapId(collections.getId());
                            me.setSourceName(collections.getProcess().getSourceName());
                            me.setCreator(collections.getProcess().getCreator());
//                            System.out.println("Colection : "+collections.toString());
//
//                            System.out.println("Classe   1: "+collections.getClasses().get(1).toString());
//                            System.out.println("Process   : "+collections.getProcess().toString());


                            classes.put(collections.getClasses().get(0).getId1(),collections.getClasses().get(0).getType1());
                            classes.put(collections.getClasses().get(1).getId1(),collections.getClasses().get(1).getType1());
                            me.setClasses(classes);

                            relations[0]=collections.getProcess().getRelation().get(0);
                            me.setRelation(relations);
                            System.out.println("ID1: "+me.getIdentifier().getId1());
                            System.out.println("ID2: "+me.getIdentifier().getId2());
                            //list.put(me.getClassesFormated()+me.getRelation(),me);


                        }

                        // System.out.println("id: " + collections.getId1() + " Creator: " + collections.getProcess().getCreator());
                    } else {
                        // System.out.println(collections.toString());
                    }

                }
                nextPage = mappings.getNextPage();
                if(nextPage!=null){
                    page = nextPage.intValue();
                }
            } else {
                errorLogger.error("ERROR: Ontology Mappings Identification for : "+args[2]+" page: "+page+ " is missing: Internal Server Error. Page will be igonered.");
                stdoutLogger.error("Ontology: "+args[2]+" Missing page: "+page+ " Internal server error.");
                page++;
                nextPage = page;
            }
            System.out.println("Pages/Page: "+totalPages+"/"+page);
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        }while(nextPage!=null);

        return list;
    }



    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "json" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, false);
        return files;
    }


    private void doPost(String[] args){

        String folder = ManageProperties.loadPropertyValue("outputfolder");
        File file = new File(folder+File.separator+args[2]+".json");
        doPost(file, args);

    }

    private void doPost(File file, String[] args){

        AgroportalRestService ars = new AgroportalRestService();
        String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");
        HashMap<String,MappingEntity> list = getInternalMappings(args,user);

        MappingEntity[] maps = loadJSONFile(file);

        HashMap<String,MappingEntity> map = getInternalMappings(args,user);

        String result="";

        int counter=0;
        for(MappingEntity me: maps){

            if(map.get(me.getIdentifier().getId1())==null && map.get(me.getIdentifier().getId2())==null){
                stdoutLogger.info("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
                System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
                result = ars.postMappings(me, this.command);
                stdoutLogger.info("Response: "+result);
                counter++;
                if(counter%5==0){
                    // Problems on the server with 0(zero) and 2 (two) seconds interval
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }else{
                stdoutLogger.info("Mapping already exists, skiping insert for--> "+me.toString());
                System.out.println("Mapping already exists, skiping insert for--> "+me.toString());
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
