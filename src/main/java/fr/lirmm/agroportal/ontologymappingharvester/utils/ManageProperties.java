package fr.lirmm.agroportal.ontologymappingharvester.utils;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.util.Properties;

public class ManageProperties {



    public static void createPropertiesFile(){

        String current="";
        try {
            current = new java.io.File(".").getCanonicalPath();
        }catch(IOException e){
            e.printStackTrace();
        }

        File f = new File(current+File.separator+"config.properties");
        if(f.exists() && !f.isDirectory()) {
        }else{
            try {
                FileOutputStream fr = new FileOutputStream(current+File.separator+"config.properties");
                Properties prop = new Properties();
                prop.store(fr, null);
                fr.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }


    }


    public static String loadPropertyValue(String propertyName){

        String current="";
        try {
            current = new java.io.File(".").getCanonicalPath();
        }catch(IOException e){
            e.printStackTrace();
        }


        propertyName = propertyName.replaceAll("-","");

        Properties prop = new Properties();
        //InputStream input = null;

        try {

                FileInputStream fi=new FileInputStream(current+File.separator+"config.properties");
                prop.load(fi);
                fi.close();


        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop.getProperty(propertyName);
    }

    public static void setProperty(String key, String value){

        String current="";
        try {
            current = new java.io.File(".").getCanonicalPath();
        }catch(IOException e){
            e.printStackTrace();
        }

        Properties prop = new Properties();


        try {

            FileInputStream fi=new FileInputStream(current+File.separator+"config.properties");
            prop.load(fi);
            fi.close();
            FileOutputStream fo = new FileOutputStream(current+File.separator+"config.properties");
            prop.setProperty(key, value);
            prop.store(fo,null);
            fo.close();

        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
