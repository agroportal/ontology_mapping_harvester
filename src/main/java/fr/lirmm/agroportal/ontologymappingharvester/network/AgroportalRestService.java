package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AgroportalRestService {


    public List<OntologyEntity> getOntologyAnnotation(String command){

        String link=ManageProperties.loadPropertyValue("agroportaladdress");
        //System.out.println("LINK: "+link);
        if(command.indexOf("n")>-1){
            link=ManageProperties.loadPropertyValue("bioportaladdress");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);

        String apiKey = ManageProperties.loadPropertyValue("apikey");
        //System.out.println("APIKEY: "+apiKey);
        Call<List<OntologyEntity>> ontologyEntitiesCall = service.getAnnotation(apiKey);

        List<OntologyEntity> ontologies = null;

        try {
            ontologies = ontologyEntitiesCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage());
            e.printStackTrace();
        }

        //System.out.println("Size: "+ontologies.size());

        return ontologies;

    }


}
