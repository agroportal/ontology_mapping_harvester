package fr.lirmm.agroportal.ontologymappingharvester.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessPRJson {

    public static void main(String[] args){



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
