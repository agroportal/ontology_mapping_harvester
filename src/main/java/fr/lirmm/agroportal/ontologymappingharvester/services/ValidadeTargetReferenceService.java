package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.TargetReference;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidadeTargetReferenceService extends BaseService{


    private HashMap<String, TargetReference> hashMap;


    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "tmp" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }

    public void validateTargetReferences(String command, List<String> fileNames){

        this.command = command;
        hashMap = readTargetReferenceHashMap();


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
            System.out.println();
            System.out.println("Processing: "+arq);
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
            if(count%1000==0){
                System.out.print(".");
            }
            count++;
            if(count%100000==0){
                System.out.println();
            }

            for (Map.Entry<String, String> entry : classesSource.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();

                if(command.indexOf("n")>-1){

                    if (value.indexOf("ncbo:") == 0) {
                        target = getReference(value.substring(value.indexOf(":") + 1, value.length()));
                    } else {
                        target = value;
                    }

                }else {

                    if (value.indexOf("agroportal:") == 0) {
                        target = getReference(value.substring(value.indexOf(":") + 1, value.length()));
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

        writeJsonFile(gson.toJson(mappingEntity),true);
        stdoutLogger.info("Finished generation of definitive JSON file");

    }

    public String getReference(String ontology){

        String ret="";
        TargetReference tr = null;

        tr = hashMap.get(ontology);

        if(command.indexOf("n")>-1){
            if(tr != null){
                ret = tr.getAtBioportal().trim();
                if(ret.equalsIgnoreCase("")){
                    ret = tr.getAtAgroportal().trim();
                    if(ret.equalsIgnoreCase("")){
                        ret = "ext:"+tr.getAtExternal();
                    }
                }
            }else{
                ret = "ext:"+ontology;
            }

        }else {
            if(tr != null){
                ret = tr.getAtAgroportal().trim();
                if(ret.equalsIgnoreCase("")){
                    ret = tr.getAtBioportal().trim();
                    if(ret.equalsIgnoreCase("")){
                        ret = "ext:"+tr.getAtExternal();
                    }
                }
            }{
                ret = "ext:"+ontology;
            }

        }

        return ret;
    }



}
