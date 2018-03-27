package fr.lirmm.agroportal.ontologymappingharvester.services;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateJSService {


    private StringBuffer js;
    private HashMap<String,String> node;
    private HashMap<String,String> edge;
    private HashMap<String,Integer> mapCounter;


    public GenerateJSService(){
        js = new StringBuffer();
        node = new HashMap<>();
        edge = new HashMap<>();
        mapCounter = new HashMap<>();
    }

    public void generateJs(String dirName){



        File dir = new File(dirName);
        String[] extensions = new String[] { "sts" };

            List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
            for (File file : files) {
               processFile(file);
            }

        generateOutPut();
        saveFile(dirName);

    }

    private void appendReader() {
        js.append("function defineMatches(){nodes = [\n");
    }
    private void appendMidle() {
        js.append("];edges = [\n");
    }
    private void appendFooter() {
        js.append("];}");
    }


    private void processFile(File file)  {

        String[] values = new String[5];

        int s1=0;
        int s2 = 0;
        String val="";


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                values = line.split(";");
                if(values[0].equalsIgnoreCase("node")){
                    val = node.get(values[1]);
                    if (val != null && !val.equalsIgnoreCase("")) {
                        s1 = Integer.parseInt(val.split(";")[2]);
                        s2 = Integer.parseInt(values[2]);
                        if (s2 >= s1) {
                            node.put(values[1], line);
                            System.out.println("Common Node s2>=s1: "+values[1]+" " + s2+">="+s1 + " " + line);
                        } else {

                            System.out.println("Common Node s2<s1: "+values[1]+" " + s2+"<"+s1 + " " + line);
                        }
                    } else {
                        node.put(values[1], line);
                        System.out.println("Common Node: -->" + values[1] + "<-- " + line);
                    }
                }
                if(values[0].equalsIgnoreCase("edge")){
                    edge.put(values[1]+values[2],line);
                    System.out.println("Edge: "+values[2]+" "+line);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            for(String line; (line = br.readLine()) != null; ) {
                values = line.split(";");
                if (values[0].equalsIgnoreCase("rootnode")) {

                    val = node.get(values[1]);
                    if (val != null && !val.equalsIgnoreCase("")) {
                        s1 = Integer.parseInt(val.split(";")[2]);
                        s2 = Integer.parseInt(values[2]);
                        if (s2 >= s1) {
                            node.put(values[1], line);
                            System.out.println("RootNode s2>=s1: "+values[1]+" " + s2+">="+s1 + " " + line);
                        } else {

                            System.out.println("RootNode s2<s1: "+values[1] +" " + s2+"<"+s1 + " " + line);
                        }
                    } else {
                        node.put(values[1], line);
                        //System.out.println("RootNode: " + values[1] + " " + line);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    private void generateOutPut(){

        String[] values = new String[5];
        appendReader();

        int counter=1;
        int mapSize = node.size();
        for (Map.Entry<String, String> entry : node.entrySet()) {
            String key = entry.getKey();
            values = entry.getValue().split(";");
            System.out.println("VALUE--->"+entry.getValue());
            mapCounter.put(key,counter);
            System.out.println("MapCounter: "+key+" "+counter);
            js.append("{id: "+counter+",  value: "+values[2]+",  label: '"+values[3]+"' }");
            if(counter < mapSize){
                js.append(",");
            }
            js.append("\n");
            counter++;
        }

        appendMidle();

        mapSize = edge.size();
        counter = 1;
        int idFrom =0;
        int idTo =0;

        for (Map.Entry<String, String> entry : edge.entrySet()) {
            String key = entry.getKey();
            values = entry.getValue().split(";");
            idFrom = mapCounter.get(values[1]);
            idTo = mapCounter.get(values[2]);
            js.append("{from: "+idFrom+", to: "+idTo+", value: "+values[3]+", title: '"+values[4]+"'}");
            if(counter < mapSize){
                js.append(",");
            }
            js.append("\n");
            counter++;
        }

        appendFooter();

    }

    private void saveFile(String dirName){

        File f = new File(dirName + File.separator + "matches.js");
        try {
            FileUtils.writeStringToFile(f, js.toString(), "UTF-8");
        } catch (IOException e) {
            e.getMessage();
        }


    }

}

