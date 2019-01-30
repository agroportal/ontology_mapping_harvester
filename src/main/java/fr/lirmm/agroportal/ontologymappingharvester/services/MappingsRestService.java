package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.Collection;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.RestMappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.CurationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.CustomRetrofitResponse;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;
import retrofit2.Response;

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
        System.out.println(args.length);


        if(args.length>=3){


            if(args[2].toLowerCase().indexOf("all")>-1){


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

        CustomRetrofitResponse mm=null;
        HashMap<String,MappingEntity> list = new HashMap<>();

        setupLogProperties(this.command,args[2],ManageProperties.loadPropertyValue("outputfolder"));

        // FASE 1 - Identify mappings created by the script for this ontology
        stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 1 - Identifing Mappings");

        if(args.length>=3){

            AgroportalRestService service = new AgroportalRestService();

            System.out.println("Arg: "+args[0].replaceAll("-",""));
            String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");
            System.out.println("User: "+user);

            list = getInternalMappings(args,user, false, args[2]);


            // FASE 2 Intereact over mappings created by this script to delete them.
            stdoutLogger.info("Delete Mappings for "+args[2]+" FASE 2 - Delete Mappings");

            int counter = 0;

            String key="";
            MappingEntity me = null;
            for (Map.Entry<String, MappingEntity> entry : list.entrySet()) {
                key = entry.getKey();
                me = entry.getValue();
                mm = service.deleteMapping(args[0],me.getMapId(),args[2]);
                if(mm.getResponseDelete().code()>=400){
                    stdoutLogger.error("TRYING TO DELETE MAPPING ID: " + me.toString()+ " DELETE WITH SUCSSES: "+mm.getResponseDelete().isSuccessful());
                    stdoutLogger.error("ERROR MESSAGE: "+mm.getErrorMessage());
                }else{
                    stdoutLogger.info("MAPPING DELETED WITH SUCSSES: " + me.toString());
                    counter++;
                }

            }


            System.out.println("Total mappings delete for: "+args[2]+" -->"+ counter);
            stdoutLogger.info("Total mappings delete for: "+args[2]+" -->"+ counter);
        }
    }


    private HashMap<String,MappingEntity> getInternalMappings(String[] args, String user, boolean getAllKeys, String ontology){

        HashMap<String,MappingEntity> list = new HashMap<String,MappingEntity>();
        MappingEntity me = null;
        String[] relations = new String[10];
        HashMap<String,String> classes =null;

        int page = 1;
        Integer nextPage = 0;
        Integer totalPages = null;

        AgroportalRestService service = new AgroportalRestService();

        RestMappingEntity mappings = service.getAllRestMappings(args[0].replaceAll("-", ""), ontology, page);

        do {

            System.out.println("Page: "+page);

            if (mappings != null) {

                if(mappings.getCollection().size()>0) {


                    for (Collection collections : mappings.getCollection()) {
                        if (collections.getId() != null && collections.getProcess() != null && collections.getProcess().getCreator() != null) {

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


                                classes.put(collections.getClasses().get(0).getId1(), collections.getClasses().get(0).getType1());
                                classes.put(collections.getClasses().get(1).getId1(), collections.getClasses().get(1).getType1());
                                me.setClasses(classes);

                                relations[0] = collections.getProcess().getRelation().get(0);
                                me.setRelation(relations);
//                                System.out.println("ID1: " + me.getIdentifier().getId1() + " " + collections.getId());
//                                System.out.println("ID2: " + me.getIdentifier().getId2() + " " + collections.getId());
                                if (getAllKeys) {
                                    list.put(me.getIdentifier().getId1(), me);
                                    list.put(me.getIdentifier().getId2(), me);
                                } else {
                                    list.put(me.getClassesFormated() + me.getRelation(), me);
                                }


                            }

                            // System.out.println("id: " + collections.getId1() + " Creator: " + collections.getProcess().getCreator());
                        } else {
                            // System.out.println(collections.toString());
                        }

                    }


                }
            } else {
                errorLogger.error("ERROR: Ontology Mappings Identification for : "+ontology+" page: "+page+ " is missing: Internal Server Error. Page will be igonered.");
                stdoutLogger.error("Ontology: "+ontology+" Missing page: "+page+ " Internal server error.");

            }

            do {
                page++;
                mappings = service.getAllRestMappings(args[0].replaceAll("-", ""), ontology, page);
            } while(mappings==null);



//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        }while(mappings.getCollection().size()>0);

        //System.exit(0);

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
        //doPost(file, args);

        doPost(file,args);
    }

    private void doPost(File file, String[] args){

        setupLogProperties(this.command+ "pl",file.getName().toUpperCase().replace(".JSON",""),ManageProperties.loadPropertyValue("outputfolder"));


        AgroportalRestService ars = new AgroportalRestService();
        String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");


        MappingEntity[] maps = loadJSONFile(file);

        // TODO retirar isso...
        //HashMap<String,MappingEntity> map = getInternalMappings(args,user, true, file.getName().toUpperCase().replace(".JSON",""));

        HashMap<String,MappingEntity> map =  new HashMap<>();

        String key="";
        System.out.println("List size: "+map.size());
        for (Map.Entry<String, MappingEntity> entry : map.entrySet()) {
            key = entry.getKey();
            System.out.println("Chave: "+key);
        }

        //System.exit(0);

        CustomRetrofitResponse response =null;

        int total = maps.length;
        int counter=0;
        int sucess = 0;
        int cc=0;
        int cont=0;
        int errorInserting=0;

        int lastposted = Integer.parseInt(ManageProperties.loadPropertyValue("executionpointer"));

        for(MappingEntity me: maps) {

            cont++;

            if(cont>=lastposted){

            //System.out.println("ID1----->"+me.getIdentifier().getId1()+" --> "+map.get(me.getIdentifier().getId1()));
            //System.out.println("ID2----->"+me.getIdentifier().getId2()+" --> "+map.get(me.getIdentifier().getId2()));

            if (map.get(me.getIdentifier().getId1()) == null && map.get(me.getIdentifier().getId2()) == null) {

                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());

                response = ars.postMappings(me, this.command);


                if (response != null && response.getResponse() != null) {

                    if (response.getResponse().isSuccessful()) {
                        stdoutLogger.info("MAPPING POSTED WITH SUCESS-->: "+cont+"  -->"+me.toString());
                        sucess++;
                        errorInserting=0;

                    } else {
                        errorInserting++;

                        try {
                            stdoutLogger.error("ERROR INSERTTING MAPPING: "+cont+" -->" + me.toString() + " Code: " + response.getResponse().code() + " Message: " + response.getResponse().message() + " - Description: " + response.getErrorMessage());
                            if(errorInserting>300){
                                ManageProperties.setProperty("executionpointer",""+cont);
                                needUserAction("To much consecutive errors. Do you want do procced ?");
                                errorInserting=0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    stdoutLogger.error("ERROR:" + response.getErrorMessage() + " - NULL RESPONSE FROM SERVER FOR MAPPING: " + me.toString());
                    ManageProperties.setProperty("executionpointer",""+cont);
                    needUserAction("Please restart server to procced <ENTER> to restart ?");
                }


//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


//


                stdoutLogger.info("XXXXXXXXX --->" + total + " / " + counter + " / " + sucess);

            } else {
                stdoutLogger.info("MAPPING ALREADY EXISTIS, skiping insert for--> " + me.toString());
            }


//            if(map.get(me.getIdentifier().getId1())!=null || map.get(me.getIdentifier().getId2())!=null){
//
//                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
//                cc++;
//                stdoutLogger.info("EXISTS--> "+me.toString());
//
//            }


//            if(map.get(me.getIdentifier().getId1())==null || map.get(me.getIdentifier().getId2())==null){
//
//                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
//
//                stdoutLogger.info("--> "+me.toString());
//
//            }
        }
        }
        System.out.println("Already exsitent mappings: "+cc);
        System.out.println("UPLOADED WITH SUCESS FOR : "+(file.getName().toUpperCase().replace(".JSON",""))+" --> "+sucess);
        summaryLogger.info(""+(file.getName().toUpperCase().replace(".JSON","")+";uploaded maps;"+sucess));
    }


    private void needUserAction(String message){
        Scanner reader = new Scanner(System.in);
        System.out.print(message);
        String resp=reader.nextLine();
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


    private void doPostExplorer(File file, String[] args){

        setupLogProperties(this.command+ "pl",file.getName().toUpperCase().replace(".JSON",""),ManageProperties.loadPropertyValue("outputfolder"));


        AgroportalRestService ars = new AgroportalRestService();
        String user = ManageProperties.loadPropertyValue(args[0].replaceAll("-","")+"user");


        MappingEntity[] maps = loadJSONFile(file);

        // TODO retirar isso...
        //HashMap<String,MappingEntity> map = getInternalMappings(args,user, true, file.getName().toUpperCase().replace(".JSON",""));

        HashMap<String,MappingEntity> map =  new HashMap<>();

        String key="";
        System.out.println("List size: "+map.size());
        for (Map.Entry<String, MappingEntity> entry : map.entrySet()) {
            key = entry.getKey();
            System.out.println("Chave: "+key);
        }

        //System.exit(0);

        CustomRetrofitResponse response =null;

        int total = maps.length;
        int counter=0;
        int sucess = 0;
        int cc=0;
        MappingEntity me = null;
        for(int xx = 179803; xx<maps.length;xx++){

            me = maps[xx];
            //System.out.println("ID1----->"+me.getIdentifier().getId1()+" --> "+map.get(me.getIdentifier().getId1()));
            //System.out.println("ID2----->"+me.getIdentifier().getId2()+" --> "+map.get(me.getIdentifier().getId2()));

            if(map.get(me.getIdentifier().getId1())==null && map.get(me.getIdentifier().getId2())==null){

                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());

                response = ars.postMappings(me, this.command);


                if(response != null && response.getResponse() !=null){

                    if(response.getResponse().isSuccessful()){
                        stdoutLogger.info("MAPPING POSTED WITH SUCESS-->: "+xx+"  -->"+me.toString());
                        sucess++;
                    }else{
                        try {
                            stdoutLogger.error("ERROR INSERTTING MAPPING: "+xx+" -->" + me.toString() + " Code: " + response.getResponse().code() + " Message: " + response.getResponse().message() + " - Description: " + response.getErrorMessage());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }else{
                    stdoutLogger.error("ERROR:"+response.getErrorMessage()+" - NULL RESPONSE FROM SERVER FOR MAPPING: "+me.toString());
                }


                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                counter++;
                if(counter%30==0){
                    // Problems on the server with 0(zero) and 2 (two) seconds interval
                    stdoutLogger.info("Waiting....");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                stdoutLogger.info("XXXXXXXXX --->"+total+" / "+counter+" / "+sucess);

            }else{
                stdoutLogger.info("MAPPING ALREADY EXISTIS, skiping insert for--> "+me.toString());
            }


//            if(map.get(me.getIdentifier().getId1())!=null || map.get(me.getIdentifier().getId2())!=null){
//
//                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
//                cc++;
//                stdoutLogger.info("EXISTS--> "+me.toString());
//
//            }


//            if(map.get(me.getIdentifier().getId1())==null || map.get(me.getIdentifier().getId2())==null){
//
//                //System.out.println("MAPPINF NOT EXISTIS, INSERTING-->: "+me.toString());
//
//                stdoutLogger.info("--> "+me.toString());
//
//            }

        }
        System.out.println("Already exsitent mappings: "+cc);
        System.out.println("UPLOADED WITH SUCESS FOR : "+(file.getName().toUpperCase().replace(".JSON",""))+" --> "+sucess);
        summaryLogger.info(""+(file.getName().toUpperCase().replace(".JSON","")+";uploaded maps;"+sucess));
    }

}
