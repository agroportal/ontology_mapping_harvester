package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.Collection;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateIRIService extends BaseService {


    Logger logger;

    public ValidateIRIService(){
        super();
        setupLogProperties("","","");
        logger = Logger.getLogger(ValidateIRIService.class.getName());
    }


    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "tmp" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }

    public void validateIRIs(String command, List<String> fileNames){

        this.command = command;
        System.out.println("Ontologies on Agroportal: "+ontologyNameHashMapAgro.size());
        System.out.println("Ontologies on Bioportal : "+ontologyNameHashMapBio.size());
        System.out.println("----------------------------------");

        File file = null;
        List<File> files = new ArrayList<File>();


        if(files.size()==0){

            files = getReferencesForJSONFiles(ManageProperties.loadPropertyValue("outputfolder"));

        }else {
            for (String fileName : fileNames) {
                file = new File(fileName);
                if(file != null){
                    files.add(file);
                }
            }
        }

        for (File arq: files){
            fileIN=arq;
            processJSON(loadTempJSONFile(arq));
        }

    }

    public void processJSON(MappingEntity[] mappingEntity){

        stdoutLogger.info("Begin generation of definitive JSON file");

        HashMap<String,String> classesSource = new HashMap<>();
        HashMap<String,String> classesTarget = new HashMap<>();
        String key="";
        String value="";
        String target="";
        int count = 0;

        for(MappingEntity me: mappingEntity){
            classesSource = me.getClasses();
            classesTarget = new HashMap<>();

            for (Map.Entry<String, String> entry : classesSource.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                System.out.print(".");
                count++;
                if(count%50==0){
                    System.out.println();
                }

                if(command.indexOf("n")>-1){

                    if (value.indexOf("ncbo:") == 0) {
                        target = getReference(me, key, value.substring(value.indexOf(":") + 1, value.length()));
                    } else {
                        target = value;
                    }

                }else {

                    if (value.indexOf("agroportal:") == 0) {
                        target = getReference(me, key, value.substring(value.indexOf(":") + 1, value.length()));
                    } else {
                        target = value;
                    }
                }
                classesTarget.put(key,target);
            }

            me.setClasses(classesTarget);

        }

        System.out.println("");

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        System.out.println("Tamanho-->"+mappingEntity.length);

        writeJsonFile(gson.toJson(mappingEntity),true);
        stdoutLogger.info("Finished generation of definitive JSON file");

    }

    public String getReference(MappingEntity an, String concept, String ontology){

        String ret="";
        if(command.indexOf("n")>-1){
            ret = isAtBioportal(concept);
            if(ret.equalsIgnoreCase("")){
                ret = isAtAgroportal(concept);
                if(!ret.equalsIgnoreCase("")){
                    ret= "agroportal:" + ret;
                }else{
                    ret = "ext:"+ontology;
                }
            }
        }else {
            ret = isAtAgroportal(concept);
            if(ret.equalsIgnoreCase("")){
                ret = isAtBioportal(concept);
                if(!ret.equalsIgnoreCase("")){
                    ret= "ncbo:" + ret;
                }else{
                    ret = "ext:"+ontology;
                }
            }
        }

        return ret;
    }

    private String isAtAgroportal(String concept){
        String ret = "";
        ClassQuery classQuery = agroportalRestService.getOntologyByConcept("agroportaladdress","apikey",  concept);
        if(classQuery != null && classQuery.getCollection().size()>0){
            for(Collection c: classQuery.getCollection()){
                if(!c.getObsolete()){
                    ret = ontologyNameHashMapAgro.get(c.getLinks().getOntology());
                    //System.out.println("Reference: "+ret);
                }
            }

        }
        return ret;
    }

    private String isAtBioportal(String concept){
        String ret = "";
        ClassQuery classQuery = agroportalRestService.getOntologyByConcept("bioportaladdress","apikeybio", concept);
        if(ret.equalsIgnoreCase("")){
            if(classQuery != null && classQuery.getCollection().size()>0){
                for(Collection c: classQuery.getCollection()){
                    if(!c.getObsolete()){
                        ret = ontologyNameHashMapBio.get(c.getLinks().getOntology());
                        //System.out.println("Reference: "+ret);
                    }
                }

            }
        }
        return ret;
    }



}
