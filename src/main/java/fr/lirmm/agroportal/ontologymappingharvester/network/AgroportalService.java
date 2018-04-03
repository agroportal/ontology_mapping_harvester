package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.OntologyEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface AgroportalService {

    // agroportal DEMO KEY
    @GET("/ontologies")
    Call<List<OntologyEntity>> getAnnotation( @Query("apikey") String apiKey);

}
