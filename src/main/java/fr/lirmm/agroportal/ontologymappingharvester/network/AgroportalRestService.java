package fr.lirmm.agroportal.ontologymappingharvester.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.Identifier;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi.RestMappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;

import fr.lirmm.agroportal.ontologymappingharvester.entities.postmappingapi.PostApiResponse;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.services.LogService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.CustomRetrofitResponse;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class AgroportalRestService extends LogService {


    String link;
    String apiKey;




    /**
     * REST Service to acess PORTALS for ontologies metadata
     * @param command - String with command line parameters
     * @return
     */
    public List<OntologyEntity> getOntologyAnnotation(String command){

        setupLogProperties(command,"", ManageProperties.loadPropertyValue("outputfolder"));

        setCredentials(command);

        //System.out.println("Link: "+link);
        //System.out.println("Key : "+apiKey);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(link)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(getGson()))
//                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        //System.out.println("APIKEY: "+apiKey);
        Call<List<OntologyEntity>> ontologyEntitiesCall = service.getAnnotation(apiKey);

        List<OntologyEntity> ontologies = null;

        try {
            ontologies = ontologyEntitiesCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+"getOntologyAnnotation() - see havest_tool_error.log for details.");
            errorLogger.error("Error: "+ e.getStackTrace());
        }

        //System.out.println("Size: "+ontologies.size());

        return ontologies;

    }

    /**
     * REST call to get the latest submission info of an ontology
     * @param command - String with command line parameters
     * @param acronym - as identified on the respective portal
     * @return
     */
    public Submission getLatestSubmission(String command, String acronym){

        setupLogProperties(command,acronym, ManageProperties.loadPropertyValue("outputfolder"));

        setCredentials(command);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        //System.out.println("APIKEY: "+apiKey);
        Call<Submission> latestSubmissionCall = service.getLatestSubmission(acronym,apiKey,"all" );

        Submission submission = null;

        try {
            submission = latestSubmissionCall.execute().body();
            if(submission==null){
                submission = new Submission();
                submission.setURI("UNKNOW_REFERENCE");
            }

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+" getLatestSubmission() - see havest_tool_error.log for detais."+" Error: "+ e.getStackTrace().toString());
            errorLogger.error("Error: "+ e.getStackTrace());
            submission = new Submission();
            submission.setURI("UNKNOW_REFERENCE");
        }


        return submission;

    }



    /**
     * REST call to find if an ontology is hosted on an especific portal
     * @param property - Name of the properti of the poral link on config.properties
     * @param concept - IRI to verify
     * @return
     */
    public ClassQuery getOntologyByConcept(String property, String apikeyProperty, String concept){

        setupLogProperties("jsl","concept_query", ManageProperties.loadPropertyValue("outputfolder"));

        String link=ManageProperties.loadPropertyValue(property);
        String apiKey = ManageProperties.loadPropertyValue(apikeyProperty);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);



        Call<ClassQuery> classQueryCall = service.getOntologyByConcept(concept, apiKey);

        ClassQuery classQuery = null;

        try {
            classQuery = classQueryCall.execute().body();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage()+" getOntologyByConcept() - see havest_tool_error.log for detais.");
            errorLogger.error("Error: "+ e.getStackTrace());
        }


        return classQuery;

    }

    /**
     * Restore the list of Identifiers from Identifiers.org rest service
     * @return
     */
    public List<Identifier> getIdentifiers(){

        setupLogProperties("jsl","identifiers_query", ManageProperties.loadPropertyValue("outputfolder"));


        String link=ManageProperties.loadPropertyValue("identifiersaddress");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AgroportalService service = retrofit.create(AgroportalService.class);

        Call<List<Identifier>> identifiersEntitiesCall = service.getIdentifiers();

        List<Identifier> identifiers = null;

        try {
            identifiers = identifiersEntitiesCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+"getIdentifiers() - see havest_tool_error.log for details.");
            errorLogger.error("Error: "+ e.getStackTrace());
        }

        //System.out.println("Size: "+ontologies.size());

        return identifiers;

    }


    public CustomRetrofitResponse postMappings(MappingEntity me, String command){

        setupLogProperties(command,me.getSourceName(), ManageProperties.loadPropertyValue("outputfolder"));


        String link=ManageProperties.loadPropertyValue(command+"url");
        String key=ManageProperties.loadPropertyValue(command+"apikey");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(link)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(getGson()))
//                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);

        //System.out.println("apikey token="+key + "  "+ me.toString()     );

        Call<PostApiResponse> postMapping = service.postMapping("apikey token="+key,"application/json", me);



        int result = 0;
        CustomRetrofitResponse resp = new  CustomRetrofitResponse();

        Response<PostApiResponse> response = null;


        try {

            response = postMapping.execute();

            resp.setResponse(response);
            if(response.isSuccessful()){
                resp.setErrorMessage("");
            }else{
                resp.setErrorMessage(response.errorBody().string());
            }


        } catch (Exception e) {

            resp.setResponse(null);
            resp.setErrorMessage(e.getMessage());
            System.out.println("Error on POST Mapping: "+me.toString()+"\nError REST POST MAPPINGS: "+ e.getMessage());
            errorLogger.error("Error on POST Mapping: "+me.toString()+"\nError REST POST MAPPINGS: "+ e.getMessage());
        }

        return resp;


    }

    private static Gson getGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }


    /**
     * Get mappings from the rest API
     * @param command to select URL and APIKey
     * @param acronym name of the ontology to get mappings
     * @return List of Mappings
     */
    public RestMappingEntity getAllRestMappings(String command, String acronym, int page){

        setupLogProperties(command,acronym, ManageProperties.loadPropertyValue("outputfolder"));


        String link=ManageProperties.loadPropertyValue(command+"url");
        String key=ManageProperties.loadPropertyValue(command+"apikey");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(link)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        Call<RestMappingEntity> restMappings = service.getRestMappings(acronym,key,page, "false", "false");

        RestMappingEntity mappings = null;

        try {
            mappings = restMappings.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+"get rest mappings");
            errorLogger.error("Error REST MAPPINGS: "+ e.getMessage());
        }

        return mappings;


    }


    public CustomRetrofitResponse deleteMapping(String command, String id, String ontologyName){

        setupLogProperties(command,ontologyName, ManageProperties.loadPropertyValue("outputfolder"));

        String link=ManageProperties.loadPropertyValue(command+"url");

        String key=ManageProperties.loadPropertyValue(command+"apikey");



        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        //System.out.println("HERE--->"+link);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(link)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create(getGson()))
//                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        Call<String> deleteMapping = service.deleteMapping(id,key);

        Response<String> message = null;
        CustomRetrofitResponse mm= new CustomRetrofitResponse();

        try {
            message = deleteMapping.execute();

            mm.setResponseDelete(message);
            if(message.errorBody() != null && message.errorBody().string()!=null){
                mm.setErrorMessage(message.errorBody().string());
            }else{
                mm.setErrorMessage("");
            }

        } catch (Exception e) {
            stdoutLogger.error("Ontology: "+ontologyName+" Error TRY BLOCK: "+e.getMessage());

        }

        return mm;


    }



    public void setCredentials(String command){
        //System.out.println("Command: "+command);

        link=ManageProperties.loadPropertyValue("restagroportalurl");
        apiKey = ManageProperties.loadPropertyValue("restagroportalapikey");

        if(command.indexOf("n")>-1){
            link=ManageProperties.loadPropertyValue("restbioportalurl");
            apiKey = ManageProperties.loadPropertyValue("restbioportalapikey");
        }

        if(command.indexOf("h")>-1) {
            link = ManageProperties.loadPropertyValue("reststageagroportalurl");
            apiKey = ManageProperties.loadPropertyValue("reststageagroportalapikey");
        }
        System.out.println("Command--> "+command+"<--");
        System.out.println("URL--> "+link+"<--");
        System.out.println("KEY--> "+apiKey+"<--");
    }


}
