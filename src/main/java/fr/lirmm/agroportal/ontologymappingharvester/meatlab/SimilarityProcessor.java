package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityProcessor {


    private String inputFileName;
    private Hierarchy hh;
    HashMap<String, Hierarchy> hierarchyHashMap;

    public void setInputFile(String inputFileName){
        this.inputFileName = inputFileName;
    }

    public void generateSimilarityScore(){

        Map<String,ArrayList<Mapping>> uniqueConcept = new HashMap<>();
        StringBuffer output = new StringBuffer();

        try {
            //Create the CSVFormat object
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(';');

            //initialize the CSVParser object
            CSVParser parser = new CSVParser(new FileReader(inputFileName), format);

            ArrayList<Mapping> mm = null;
            Mapping map=null;

            List<Mapping> maps = new ArrayList<Mapping>();
            for (CSVRecord record : parser) {

             //if(record.get("faccet").substring(0,1).equalsIgnoreCase("a")|| record.get("faccet").substring(0,1).equalsIgnoreCase("b")) {

                System.out.println("Faccet letter: "+record.get("faccet"));

                map = new Mapping();
                map.setId(record.get("id"));
                map.setFaccet(record.get("faccet"));
                map.setConcept(record.get("concept"));
                map.setMap(record.get("map"));
                map.setFoodid(record.get("foodid"));
                map.setOrigfdnam(record.get("origfdnam"));
                map.setEngfdnam(record.get("engfdnam"));
                map.setLangualcodes(record.get("langualcodes"));
                map.setRemarks(record.get("remarks"));
                maps.add(map);
                mm = uniqueConcept.get(map.getConcept());
                if (mm != null) {
                    mm.add(map);
                } else {
                    mm = new ArrayList<>();
                    mm.add(map);
                }
                uniqueConcept.put(map.getConcept(), mm);
             //}
            }
            //close the parser
            parser.close();

            System.out.println("Size: "+maps.size());


            BufferedWriter writer = Files.newBufferedWriter(Paths.get(inputFileName.replace(".txt","_out.cvs")));
            CSVPrinter printer = new CSVPrinter(writer, format.withDelimiter(';'));
            printer.printRecord("id", "concept", "origin_id", "target_id","origin_faccet", "target_faccet", "origin_description","target_description", "origin_size", "target_size","score");
            int counter = 1;


            String key = "";
            ArrayList<Mapping> value = null;
            for (Map.Entry<String, ArrayList<Mapping>> entry : uniqueConcept.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                int c = 1;
                for(Mapping m: value){
                    if(c<value.size()) {
                        for (int i = c; i < value.size(); i++) {

                            List<String> outData = new ArrayList<String>();
                            outData.add(""+counter++);
                            outData.add(m.getConcept());
                            outData.add(m.getFoodid());
                            outData.add(value.get(i).getFoodid());
                            outData.add(m.getLangualcodes());
                            outData.add(value.get(i).getLangualcodes());
                            outData.add(m.getOrigfdnam());
                            outData.add(value.get(i).getOrigfdnam());
                            m.calcSimilarityScore(value.get(i).getLangualcodes(),1.0,1.0,"");
                            outData.add(""+m.getOriginSize());
                            outData.add(""+m.getTargetSize());
                            outData.add(""+m.getSameFaccetCount());
                            printer.printRecord(outData);
                        }
                    }
                    c++;
                }
           }

            printer.flush();
            printer.close();

            System.out.println("Similarity Size: "+counter);



        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void findFoodOnCandidateConcepts(String outputFileName, String targetFaccets, String targetDescription, int sameFaccetMinimalCount, int sameBranchFaccetMinimalCount, double alpha, double beta ){

        hierarchyHashMap = new HashMap<>();
        loadFoodMaterialHiearchy("/home/abrahao/data/meatylab/phase2/FOODON_MEATLAB.log");

        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();


        try {
            //Create the CSVFormat object
            CSVFormat format = CSVFormat.RFC4180.withHeader().withDelimiter(';');

            //initialize the CSVParser object
            CSVParser parser = new CSVParser(new FileReader(inputFileName), format);

            ArrayList<Mapping> mm = null;
            Mapping map = null;

            HashMap<String,Score> concetpCount = new HashMap<>();
            Score score = null;

            List<Mapping> maps = new ArrayList<Mapping>();
            for (CSVRecord record : parser) {

                //if (record.get("faccet").substring(0, 1).equalsIgnoreCase("a") || record.get("faccet").substring(0, 1).equalsIgnoreCase("b")) {


                    map = new Mapping();
                    map.setId(record.get("id"));
                    map.setFaccet(record.get("faccet"));
                    map.setConcept(record.get("concept"));
                    map.setMap(record.get("map"));
                    map.setFoodid(record.get("foodid"));
                    map.setOrigfdnam(record.get("origfdnam"));
                    map.setEngfdnam(record.get("engfdnam"));
                    map.setLangualcodes(record.get("langualcodes"));
                    map.setRemarks(record.get("remarks"));
                    map.calcSimilarityScore(targetFaccets,alpha,beta,targetDescription);

                    if(map.getSameFaccetCount()>=sameFaccetMinimalCount && map.getSameBranchFaccetCount()>=sameBranchFaccetMinimalCount ){
                        maps.add(map);
                        if(concetpCount.get(map.getConcept())!=null){
                            score = concetpCount.get(map.getConcept());
                            score.addConceptCount();
                            score.addSameFaccetCount(map.getSameFaccetCount());
                            score.addSameBranchCount(map.getSameBranchFaccetCount());
                            score.addScore(map.getScore().doubleValue());
                            concetpCount.put(map.getConcept(),score);
                        }else{
                            score = new Score(map.getConcept(),map.getOrigfdnam(),map.getLangualcodes(),map.getGama());
                            score.setConceptCount(1);
                            score.addSameFaccetCount(map.getSameFaccetCount());
                            score.addSameBranchCount(map.getSameBranchFaccetCount());
                            score.addScore(map.getScore().doubleValue());
                            concetpCount.put(map.getConcept(),score);
                        }
                    }

                //}
            }
            //close the parser
            parser.close();


            System.out.println();
            System.out.println("Similarity Report");
            System.out.println();
            System.out.println("Desciption: " + targetDescription);
            System.out.println();
            System.out.println("New Product Faccets: " + targetFaccets);
            System.out.println();
            System.out.println("Minimal number of same faccets: "+  sameFaccetMinimalCount);
            System.out.println("Minimal number of same branch faccets: "+sameBranchFaccetMinimalCount);
            System.out.println("Alpha: "+alpha+" Beta: "+beta);
            System.out.println();

            sb1.append("id;faccet;concept;map;foodid;origfdnam;engfdnam;langualcodes;remarks;originSize;targetSize;sameFaccetCount;sameBranchFaccetCount;score\n");

            for(Mapping m: maps){
                sb1.append(m.getLIneFormated());
                System.out.println(m.toString());
            }

            sb1.append("\n");
            sb1.append("Similarity Report\n");
            sb1.append("\n");
            sb1.append("Desciption: " + targetDescription+"\n\n");
            sb1.append("New Product Faccets: " + targetFaccets+"\n");
            sb1.append("\n");
            sb1.append("Minimal number of same faccets: "+  sameFaccetMinimalCount+"\n");
            sb1.append("Minimal number of same branch faccets: "+sameBranchFaccetMinimalCount+"\n");
            sb1.append("Alpha: "+alpha+" Beta: "+beta+ "\n");


            String key = "";
            Score ss =  null;

            System.out.println();
            System.out.println("Legend: NP=Number of Products, SF=Same Faccets Count , SB=Sabe Branch Count , G=Gama Label Words Count, S=Score ");
            System.out.println();

            String concept="";

            sb2.append("concept;description;count;SameFaccetCount;SameBranchCount;Gama;Score;concept;depth;parents;faccetList\n");

            for (Map.Entry<String, Score> entry : concetpCount.entrySet()) {
                key = entry.getKey();
                ss = entry.getValue();
                concept = key.substring(key.lastIndexOf("/")+1);
                hh = hierarchyHashMap.get(concept);
                sb2.append(key+";"+ss.getConceptDescription()+";"+ss.getConceptCount()+";"+ss.getSameFaccetCountAverage()+";"+ss.getSameBranchCountAverage()+";"+ss.getGama()+";"+ss.getScoreAverage()+";"+hh.getLineFormated()+";" +ss.getFaccetList()+"\n");
                System.out.println("Concept: "+key+" NP: "+ss.getConceptCount()+" SF: "+ss.getSameFaccetCountAverage()+" SB: "+ss.getSameBranchCountAverage()+ " G: "+ss.getGama()+" S: "+ss.getScoreAverage()+" "+hh.toString()+" Faccets: " +ss.getFaccetList());
            }

            sb2.append("\n");
            sb2.append("Similarity Report\n");
            sb2.append("\n");
            sb2.append("Desciption: " + targetDescription+"\n\n");
            sb2.append("New Product Faccets: " + targetFaccets+"\n");
            sb2.append("\n");
            sb2.append("Minimal number of same faccets: "+  sameFaccetMinimalCount+"\n");
            sb2.append("Minimal number of same branch faccets: "+sameBranchFaccetMinimalCount+"\n");
            sb2.append("Alpha: "+alpha+" Beta: "+beta+ "\n");

            saveFile(outputFileName+".xls",sb1);
            saveFile(outputFileName+"_summary.xls",sb2);

        }catch(IOException e){
            e.printStackTrace();
        }




    }



    public void calculateDistanceBetweenProducts(String p1, String p2, double alpha, double beta){

        Mapping map = new Mapping();
        map.setLangualcodes(p1);
        map.calcSimilarityScore(p2,alpha,beta,"");

        System.out.println();
        System.out.println("Distance between Products");
        System.out.println();
        System.out.println("Number of P1 Faccets                 : " + map.getOriginSize());
        System.out.println("Number of P2 Faccets                 : " + map.getTargetSize());
        System.out.println("Minimal number of same faccets       : "+  map.getSameFaccetCount());
        System.out.println("Minimal number of same branch faccets: "+map.getSameBranchFaccetCount());
        System.out.println("Alpha/Beta                           : "+alpha+" / "+beta);
        System.out.println("Score                                : "+map.getScore());
        System.out.println();

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
            //e.printStackTrace();
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


    }




