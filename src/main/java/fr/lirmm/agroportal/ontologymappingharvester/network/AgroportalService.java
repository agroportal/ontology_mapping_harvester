package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.Identifier;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

/**
 * Define the signature for the REST access
 */
public interface AgroportalService {

    @GET("/ontologies")
    Call<List<OntologyEntity>> getAnnotation( @Query("apikey") String apiKey);

    @GET("/ontologies/{acronym}/latest_submission")
    Call<Submission> getLatestSubmission( @Path("acronym") String acronym, @Query("apikey") String apiKey, @Query("display") String value);

    @GET("/search")
    Call<ClassQuery> getOntologyByConcept(@Query("q") String concept, @Query("apikey") String apiKey);

    @GET("/rest/collections")
    Call<List<Identifier>> getIdentifiers();

    @POST("/mappings")
    Call<String> postMapping(MappingEntity me, @Query("apikey") String apiKey);

}
