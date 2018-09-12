package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.Identifier;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.RestMappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Define the signature for the REST access
 */
public interface AgroportalService {

    @GET("/ontologies")
    Call<List<OntologyEntity>> getAnnotation( @Query("apikey") String apiKey);

    @GET("/ontologies/{acronym}/latest_submission")
    Call<Submission> getLatestSubmission(@Path("acronym") String acronym, @Query("apikey") String apiKey, @Query("display") String value);

    @GET("/search")
    Call<ClassQuery> getOntologyByConcept(@Query("q") String concept, @Query("apikey") String apiKey);

    @GET("/rest/collections")
    Call<List<Identifier>> getIdentifiers();


    @POST("/mappings")
    Call<String> postMapping(@Header("Authorization") String key,@Header("Content-Type") String key2,@Body MappingEntity me);

    @FormUrlEncoded
    @POST("/mappings")
    Call<String> postMapping2(@Header("Authorization") String key, @Header("Content-Type") String key2, @Field("creator") String creator, @Field("source_contact_info") String sourceContact, @Field("relation") String[] relation , @Field("source") String source , @Field("source_name") String sourceName , @Field("comment") String comment, @Field("classes") Map<String,String> map) ;

    @GET("/ontologies/{acronym}/mappings")
    Call<RestMappingEntity> getRestMappings(@Path("acronym") String acronym, @Query("apikey") String apiKey, @Query("page") int page, @Query("display_links") String displayLink,@Query("display_context") String displayCOntext);

    @DELETE("/mappings/{id}")
    Call<String> deleteMapping(@Path("id") String id, @Query("apikey") String key);

}
