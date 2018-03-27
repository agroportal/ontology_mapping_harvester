package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.OntologyEntity;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class AgroportalRestService {

    public List<OntologyEntity> getOntologyAnnotation(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://data.agroportal.lirmm.fr")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);

        Call<List<OntologyEntity>> ontologyEntitiesCall = service.getAnnotation();

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
