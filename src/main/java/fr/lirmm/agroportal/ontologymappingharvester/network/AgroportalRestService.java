package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;

import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class AgroportalRestService {


    /**
     * REST Service to acess PORTALS for ontologies
     * @param command
     * @return
     */
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


    public Submission getLatestSubmission(String command, String acronym){

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
        Call<Submission> latestSubmissionCall = service.getLatestSubmission(acronym,apiKey );

        Submission submission = null;

        try {
            submission = latestSubmissionCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage());
            e.printStackTrace();
        }

        //System.out.println("Size: "+ontologies.size());

        return submission;

    }

    public ClassQuery getOntologyByConcept(String property, String concept){

        String link=ManageProperties.loadPropertyValue(property);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);

        String apiKey = ManageProperties.loadPropertyValue("apikey");

        Call<ClassQuery> classQueryCall = service.getOntologyByConcept(concept, apiKey);

        ClassQuery classQuery = null;

        try {
            classQuery = classQueryCall.execute().body();
        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage());
            e.printStackTrace();
        }


        return classQuery;

    }


}
