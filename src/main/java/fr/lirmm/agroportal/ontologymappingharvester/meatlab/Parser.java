package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.TargetReference;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {




    private HashMap<String, MappingEntity> mappingRereferece;
    private List<String> sirenList;
    private List<String> langualList;
    private List<String> referenceList;
    private int sirenOnLangual;
    private int langualOnLangual;
    private StringBuffer sb1;
    private StringBuffer sb2;


    public Parser(){
        mappingRereferece = new HashMap<>();
        sirenList =  new ArrayList();
        langualList = new ArrayList();
        referenceList = new ArrayList();
        sirenOnLangual=0;
        langualOnLangual=0;
        sb1 = new StringBuffer();
        sb2 = new StringBuffer();
    }


    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "tmp" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }

    public void execute(String ontologyMapsFileName, String ontologyName, String referenceFileName){

        File file = null;
        List<File> files = getReferencesForJSONFiles(ManageProperties.loadPropertyValue("outputfolder"));

        for (File arq: files){

            if(arq.getName().equalsIgnoreCase(ontologyMapsFileName)){
                System.out.println();
                System.out.println("Processing: "+arq);
                processJSON(loadTempJSONFile(arq), ontologyName);
            }
        }

        readLangualFile(referenceFileName+".txt");

        System.out.println("Siren   on "+ontologyName     +" internal mappings: "+sirenList.size());
        System.out.println("Langual on "+ontologyName     +" internal mappings: "+langualList.size());
        System.out.println("Reference  "+referenceFileName+" number of lines  : "+referenceList.size());

        generateMappings();


        System.out.println(ontologyName +"_siren_"+ referenceFileName+ "_maps: "+sirenOnLangual);
        System.out.println(ontologyName +"_langual_"+ referenceFileName+ "_maps: "+langualOnLangual);

        saveFile(ontologyName +"_siren_"+ referenceFileName+ "_maps.txt",sb1);
        saveFile(ontologyName +"_langual_"+ referenceFileName+ "_maps.txt",sb2);

    }


    public void generateMappings(){

        MappingEntity me;

        for(String siren: sirenList){
            for(String line: referenceList){
                if(line.indexOf(siren.replace("f","")+"\t")==0){
                    sirenOnLangual++;
                    me = mappingRereferece.get(siren);
                    sb1.append(siren +" - "+ me.getClassesFormated()+ " FOUNDEDIN: "+line+ "\n");
                }
            }
        }
        for(String langual: langualList){

            for(String line: referenceList){
                if(line.toLowerCase().indexOf(langual)>-1){
                    me = mappingRereferece.get(langual);
                    sb2.append(langual+" - "+me.getClassesFormated()+ " FOUNDEDIN: "+line+"\n");
                    langualOnLangual++;
                }
            }
        }

    }


    public void processJSON(MappingEntity[] mappingEntity, String acronym){

        HashMap<String,String> classesSource = new HashMap<>();
        String key="";
        String value="";
        String target="";
        int count=1;

        for(MappingEntity me: mappingEntity){
            classesSource = me.getClasses();
            //System.out.println(me.toString());
            count = 1;
            for (Map.Entry<String, String> entry : classesSource.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();

                //System.out.println(""+count+") key--> "+key+" value-->"+value);
                count++;

                if(!value.equalsIgnoreCase(acronym)){

                    if(key.indexOf("subset_siren:")==0){
                        sirenList.add(key.substring(key.indexOf(":")+1,key.length()));
                        mappingRereferece.put(key.substring(key.indexOf(":")+1,key.length()),me);
                        //System.out.println("SIREN   - "+key.substring(key.indexOf(":")+1,key.length()));
                    }else if(key.indexOf("langual:")==0){
                        langualList.add(key.substring(key.indexOf(":")+1,key.length()));
                        mappingRereferece.put(key.substring(key.indexOf(":")+1,key.length()),me);
                        //System.out.println("LANGUAL - "+key.substring(key.indexOf(":")+1,key.length()));
                    }


                }


            }


        }

        System.out.println("");

//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
//
//        writeJsonFile(gson.toJson(mappingEntity),true);


    }

    public MappingEntity[] loadTempJSONFile(File file){

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();
        MappingEntity[] mappingE = null;


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String everything = sb.toString();
            Gson gson = new GsonBuilder().create();
            mappingE = gson.fromJson(everything, MappingEntity[].class);

            System.out.println("Size: "+mappingE.length);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappingE;
    }


    public void readLangualFile(String fileName){

        File file = new File("/home/abrahao/data/meatlab/"+fileName);



        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine(); // header
            line = br.readLine();

            while (line != null) {
                referenceList.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveFile(String fileName, StringBuffer js){

        File f = new File("/home/abrahao/data/meatlab/"+fileName);
        try {
            FileUtils.writeStringToFile(f, js.toString(), "UTF-8");
        } catch (IOException e) {
            e.getMessage();
        }


    }

}
