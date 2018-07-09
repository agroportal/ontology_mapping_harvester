package fr.lirmm.agroportal.ontologymappingharvester.services;

import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.CurationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import fr.lirmm.agroportal.ontologymappingharvester.utils.SortMapByValue;
import fr.lirmm.agroportal.ontologymappingharvester.utils.Util;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MergeCurationFIlesService {

    private HashMap<String, CurationEntity> originalCurationHashMap;
    private HashMap<String, CurationEntity> mergedCurationHashMap;
    private Logger mergedLogger;
    private int lastCounter;
    String header="";



    public void execute(){

        setupLog();

        String dir = ManageProperties.loadPropertyValue("externalproperties");

        File source = new File(dir+File.separator+"OMHT_external_matches_phase_1.cfg");
        File dest = new File(dir+File.separator+"BACKUP_"+ Util.getDateTime()+"_OMHT_external_matches_phase_1.cfg");

        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        originalCurationHashMap = loadCurationFile("OMHT_external_matches_phase_1.cfg");
        mergedCurationHashMap = loadCurationFile("OMHT_external_matches_phase_1_to_be_curated.xls");
        System.out.println("Original size: "+originalCurationHashMap.size());
        System.out.println("Merged   size: "+mergedCurationHashMap.size());

        mergeFiles();
        saveFile();

    }


    private void mergeFiles(){

        CurationEntity ce;
        CurationEntity ce2;

        for (Map.Entry<String, CurationEntity> entry : mergedCurationHashMap.entrySet()) {
            String key = entry.getKey();
            ce = originalCurationHashMap.get(key);
            if(ce!=null){
                ce2 = entry.getValue();
                //externalTargetReferenceHashMapOut.put(key1,ce);
                mergedLogger.info("ORIGEN ---> "+ce.toString());
                mergedLogger.info("DESTINY---> "+ce2.toString());
                ce.setNumber(ce2.getNumber());
                ce.setCounter(ce2.getCounter());
                mergedCurationHashMap.put(ce.getTargetFounded(),ce);

            }
        }

        mergedCurationHashMap = SortMapByValue.sortByValues(mergedCurationHashMap,SortMapByValue.DESC);

    }



    private HashMap<String,CurationEntity> loadCurationFile(String fileName){

        lastCounter = 0;

        HashMap<String, CurationEntity> hash = new HashMap<>();

        String dir = ManageProperties.loadPropertyValue("externalproperties");


        try(BufferedReader br = new BufferedReader(new FileReader(dir+File.separator+fileName))) {

            //System.out.println("Entrou qui....");

            String line = br.readLine();
            header = line;
            String[] content = new String[12];
            CurationEntity er;
            // exclude hearder
            line = br.readLine();
            String property = "";

            while (line != null) {
                lastCounter++;
                content = line.split(";");
                if(content.length<13){
                    property = "";
                }else{
                    property = content[12];
                }
                er = new CurationEntity(content[0],content[1],content[2],content[3],Integer.parseInt(content[4]),content[5],content[6],content[7],content[8],content[9],content[10],Integer.parseInt(content[11]),property);

                hash.put(content[1].toLowerCase(),er);
                line = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error trying to load external target references txt file located in: "+dir+" message:"+e.getMessage());
        }
        return hash;
    }


    private void setupLog(){

        String path = ManageProperties.loadPropertyValue("externalproperties");

        Properties logProperties =  new Properties();

        logProperties.setProperty("log4j.logger.merged","TRACE, merged_curation_files");

        logProperties.setProperty("log4j.appender.merged_curation_files.File", path+"/OMHT_merged_curation_files.log");
        logProperties.setProperty("log4j.appender.merged_curation_files", "org.apache.log4j.FileAppender");
        logProperties.setProperty("log4j.appender.merged_curation_files.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.merged_curation_files.layout.ConversionPattern","%m%n");

        PropertyConfigurator.configure(logProperties);

        mergedLogger = Logger.getLogger("merged");
    }


    private void saveFile(){

        String path = ManageProperties.loadPropertyValue("externalproperties");

        File f = new File(path + File.separator + "OMHT_external_matches_phase_1.cfg");

        mergedLogger.info("Writing merged file: " + f.getAbsolutePath());

        StringBuffer sb = new StringBuffer();

        sb.append(header+"\n");

        for (Map.Entry<String, CurationEntity> entry : mergedCurationHashMap.entrySet()) {
            String key = entry.getKey();
            CurationEntity ce = entry.getValue();
            sb.append(ce.getSerializedLine()+ "\n");
        }



        try {
            FileUtils.writeStringToFile(f, sb.toString(), "UTF-8");
        } catch (IOException e) {
            mergedLogger.error("Error trying to write merged file: " + e.getMessage());
        }

    }


}
