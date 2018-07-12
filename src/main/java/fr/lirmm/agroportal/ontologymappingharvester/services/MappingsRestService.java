package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MappingsRestService {

    public void postMappings(String[] args){

        if(args.length>=4){

            if(args[4].equalsIgnoreCase("all")){

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

    }


    private void doPost(String acronym){

        AgroportalRestService ars = new AgroportalRestService();

        String folder = ManageProperties.loadPropertyValue("stageagroportaladdress");
        File file = new File(folder+File.separator+acronym);

        MappingEntity[] maps = loadJSONFile(file);

        String result="";

        int counter=0;
        for(MappingEntity me: maps){
            result = ars.postMappings(me);
            System.out.println("Retorno: "+result);
            counter++;
            if(counter%5==0){
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
