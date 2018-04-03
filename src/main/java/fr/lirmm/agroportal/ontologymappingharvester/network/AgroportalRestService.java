package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.LoadProperties;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class AgroportalRestService {

    public List<OntologyEntity> getOntologyAnnotation(String command){

        String link="http://data.agroportal.lirmm.fr";
        if(command.indexOf("n")>-1){
            link="http://data.bioontology.org";
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);

        String apiKey = LoadProperties.loadPropertyValue("apikey");

        Call<List<OntologyEntity>> ontologyEntitiesCall = service.getAnnotation(apiKey);

        List<OntologyEntity> ontologies = null;

        try {
            ontologies = ontologyEntitiesCall.execute().body();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println("Size: "+ontologies.size());

        return ontologies;

    }


}
