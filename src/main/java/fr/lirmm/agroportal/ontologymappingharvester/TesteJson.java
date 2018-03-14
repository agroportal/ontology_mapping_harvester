package fr.lirmm.agroportal.ontologymappingharvester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.MappingEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class TesteJson {


    public static void main(String[] args){

        MappingEntity me;

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();


        for(int i = 0;i<10;i++) {

            me = new MappingEntity();
            me.setId(1);
            me.setCreator("http://data.agroportal.lirmm.fr/users/elcioabrahao");
            me.setSourceContactInfo("elcio.abrahao@lirmm.fr");
            me.setSource("rest");
            me.setSourceName("Test Json");
            me.setComment("This is a test for a mapping JSON generation");

            HashMap<String, String> classes = new HashMap<>();

            classes.put("http://purl.obolibrary.org/obo/BFO_0000050", "BFO");
            classes.put("http://www.southgreen.fr/agrold/vocabulary/part_of", "ext:http://www.southgreen.fr/agrold/vocabulary");

            me.setClasses(classes);

            mappingEntities.add(me);

        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        String json = gson.toJson(mappingEntities);

        System.out.println(json);



    }


}
