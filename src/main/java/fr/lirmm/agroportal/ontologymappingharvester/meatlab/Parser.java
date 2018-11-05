package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.TargetReference;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
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
    private List<String> referenceList;
    private int sirenOnFoodON;
    private StringBuffer sb1;
    private Hierarchy hh;
    HashMap<String, Hierarchy> hierarchyHashMap;


    public Parser(){
        mappingRereferece = new HashMap<>();
        sirenList =  new ArrayList();
        referenceList = new ArrayList();
        sirenOnFoodON=0;
        sb1 = new StringBuffer();
        hierarchyHashMap = new HashMap<>();

    }


    public List<File> getReferencesForJSONFiles(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "json" };

        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        return files;
    }

    public void execute(String ontologyMapsFileName, String ontologyName, String referenceFileName){

        File file = null;
        List<File> files = getReferencesForJSONFiles(ManageProperties.loadPropertyValue("outputfolder"));


        loadFoodMaterialHiearchy("/home/abrahao/data/meatylab/phase2/FOODON_MEATLAB.log");

        for (File arq: files){

            if(arq.getName().equalsIgnoreCase(ontologyMapsFileName)){
                System.out.println();
                System.out.println("Processing: "+arq);
                processJSON(loadTempJSONFile(arq), ontologyName);
            }
        }

        //readLangualFile(referenceFileName+".txt");

        readSirenFile("/home/abrahao/data/meatylab/phase2/"+referenceFileName+".txt");


        //System.out.println("Siren   on "+ontologyName     +" internal mappings: "+sirenList.size());
        System.out.println("Siren on "+ontologyName     +" internal mappings  : "+sirenList.size());
        System.out.println("Reference  "+referenceFileName+" number of lines  : "+referenceList.size());
        System.out.println("Food Material Hierarchy concetps number           : "+hierarchyHashMap.size());



        generateMappings();


        //System.out.println(ontologyName +"_siren_"+ referenceFileName+ "_maps: "+sirenOnLangual);
        System.out.println(ontologyName +"_siren_"+ referenceFileName+ "_maps: "+sirenOnFoodON);

        //saveFile(ontologyName +"_siren_"+ referenceFileName+ "_maps.txt",sb1);
        saveFile(ontologyName +"_siren_"+ referenceFileName+ "_maps.txt",sb1);

    }


    public void generateMappings(){

        MappingEntity me;
        int idCounter=0;

        sb1.append("id;faccet;concept;map;foodid;origfdnam;engfdnam;langualcodes;remarks\n");


        for(String siren: sirenList){
            for(String line: referenceList){
                if(line.indexOf(siren)==0){
                    idCounter++;
                    sirenOnFoodON++;
                    me = mappingRereferece.get(siren);
                    //sb1.append(siren +" - "+ me.getClassesFormated()+ " FOUNDEDIN: "+line+ "\n");
                    sb1.append(""+idCounter+";"+siren.toUpperCase()+";"+me.getClassesFormated()+ ";"+line+"\n");
                }
            }
        }


    }


    public void processJSON(MappingEntity[] mappingEntity, String acronym){

        HashMap<String,String> classesSource = new HashMap<>();
        String key="";
        String value="";
        String target="";
        String concept = "";
        int count=1;

        for(MappingEntity me: mappingEntity){
            classesSource = me.getClasses();
            //System.out.println(me.toString());



            for (Map.Entry<String, String> entry : classesSource.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if(value.equalsIgnoreCase(acronym)){
                    concept = key.substring(key.lastIndexOf("/")+1);
                    System.out.println("Concept   : "+concept);
                    System.out.println("hierarquia: "+hierarchyHashMap.get(concept));
                }
            }

            // verify if the concept is on the food material hierarchy
            if(hierarchyHashMap.get(concept)!=null) {
                count = 1;
                for (Map.Entry<String, String> entry : classesSource.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue();
                    //System.out.println(""+count+") key--> "+key+" value-->"+value);
                    count++;
                    if (!value.equalsIgnoreCase(acronym)) {
                        if (key.indexOf("http://www.langual.org/download/IndexedDatasets/FDA/SIREN/") == 0) {
                            target = key.substring(key.lastIndexOf("/") + 1);
                            sirenList.add(target);
                            mappingRereferece.put(target, me);
                            System.out.println("SIREN   - " + target);
                        }
                    }
                }
            }


        }

        System.out.println("");



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


    public void readSirenFile(String fileName){

        File file = new File(fileName);



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

        File f = new File("/home/abrahao/data/meatylab/phase2/"+fileName);
        try {
            FileUtils.writeStringToFile(f, js.toString(), "UTF-8");
        } catch (IOException e) {
            e.getMessage();
        }


    }

    public void loadFoodMaterialHiearchy(String inputFileName){

        try {
            //Create the CSVFormat object
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(';');

            //initialize the CSVParser object
            CSVParser parser = new CSVParser(new FileReader(inputFileName), format);

            for (CSVRecord record : parser) {

                hh = new Hierarchy();

                hh.setConcept(record.get("concept"));
                hh.setDepth(Integer.parseInt(record.get("depth")));
                hh.setParents(record.get("parents"));
                //System.out.println("CCC: "+hh.getConcept());

                hierarchyHashMap.put(hh.getConcept(),hh);

            }

            parser.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
