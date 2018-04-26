package fr.lirmm.agroportal.ontologymappingharvester.network;

import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;

import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class AgroportalRestService {


    Logger logger;

    public AgroportalRestService(){
        logger = Logger.getLogger(AgroportalRestService.class.getName());
    }


    /**
     * REST Service to acess PORTALS for ontologies metadata
     * @param command - String with command line parameters
     * @return
     */
    public List<OntologyEntity> getOntologyAnnotation(String command){

        String link=ManageProperties.loadPropertyValue("agroportaladdress");
        String apiKey = ManageProperties.loadPropertyValue("apikey");
        //System.out.println("Command--> "+command+"<--");
        if(command.indexOf("n")>-1){
            link=ManageProperties.loadPropertyValue("bioportaladdress");
            apiKey = ManageProperties.loadPropertyValue("apikeybio");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        //System.out.println("APIKEY: "+apiKey);
        Call<List<OntologyEntity>> ontologyEntitiesCall = service.getAnnotation(apiKey);

        List<OntologyEntity> ontologies = null;

        try {
            ontologies = ontologyEntitiesCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+"getOntologyAnnotation() - see havest_tool_error.log for details.");
            logger.error("Error: "+ e.getStackTrace());
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

        String link=ManageProperties.loadPropertyValue("agroportaladdress");
        String apiKey = ManageProperties.loadPropertyValue("apikey");
        //System.out.println("LINK: "+link);
        if(command.indexOf("n")>-1){
            link=ManageProperties.loadPropertyValue("bioportaladdress");
            apiKey = ManageProperties.loadPropertyValue("apikeybio");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(link)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        AgroportalService service = retrofit.create(AgroportalService.class);


        //System.out.println("APIKEY: "+apiKey);
        Call<Submission> latestSubmissionCall = service.getLatestSubmission(acronym,apiKey );

        Submission submission = null;

        try {
            submission = latestSubmissionCall.execute().body();

        } catch (Exception e) {
            System.out.println("Erro: "+e.getMessage()+"getLatestSubmission() - see havest_tool_error.log for detais.");
            logger.error("Error: "+ e.getStackTrace());
        }

        //System.out.println("Size: "+ontologies.size());

        return submission;

    }


    /**
     * REST call to find if an ontology is hosted on an especific portal
     * @param property - Name of the properti of the poral link on config.properties
     * @param concept - IRI to verify
     * @return
     */
    public ClassQuery getOntologyByConcept(String property, String apikeyProperty, String concept){

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
            logger.error("Error: "+ e.getStackTrace());
        }


        return classQuery;

    }


}
