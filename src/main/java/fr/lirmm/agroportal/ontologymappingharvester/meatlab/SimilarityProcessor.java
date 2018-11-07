package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
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
                            m.calcSimilarityScore(value.get(i).getLangualcodes(),1.0,1.0);
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


    public void findFoodOnCandidateConcepts(String targetFaccets, int sameFaccetMinimalCount, int sameBranchFaccetMinimalCount, double alpha, double beta ){

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
                    map.calcSimilarityScore(targetFaccets,alpha,beta);

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
                            score = new Score(map.getConcept());
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
            System.out.println("New Product Faccets: " + targetFaccets);
            System.out.println("Minimal number of same faccets: "+  sameFaccetMinimalCount);
            System.out.println("Minimal number of same branch faccets: "+sameBranchFaccetMinimalCount);
            System.out.println("Alpha: "+alpha+" Beta: "+beta);
            System.out.println();

            for(Mapping m: maps){
                System.out.println(m.toString());
            }

            String key = "";
            Score ss =  null;

            System.out.println();
            System.out.println("Legend: NP=Number of Products, SFA=Same Faccets Count Average, SBA=Sabe Branch Count Average, SA=Score Average");
            System.out.println();

            for (Map.Entry<String, Score> entry : concetpCount.entrySet()) {
                key = entry.getKey();
                ss = entry.getValue();
                System.out.println("Concept: "+key+" NP: "+ss.getConceptCount()+" SFA: "+ss.getSameFaccetCountAverage()+" SBA: "+ss.getSameBranchCountAverage()+ " SA: "+ss.getScoreAverage());
            }



        }catch(IOException e){
            e.printStackTrace();
        }




    }



    public void calculateDistanceBetweenProducts(String p1, String p2, double alpha, double beta){

        Mapping map = new Mapping();
        map.setLangualcodes(p1);
        map.calcSimilarityScore(p2,alpha,beta);

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







    }




