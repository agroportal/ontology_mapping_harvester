package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.OntologyEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface AgroportalService {

    // agroportal DEMO KEY
    @GET("/ontologies?apikey=528c4e4a-5c3e-4798-a2e2-11d96761b8ce")
    Call<List<OntologyEntity>> getAnnotation();

}
