package fr.lirmm.agroportal.ontologymappingharvester.utils;

import java.io.*;
import java.util.Properties;

public class ManageProperties {

    public static void main(String[] args) {


        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            //System.out.println(prop.getProperty("externalproperties"));


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static String loadPropertyValue(String propertyName){


        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop.getProperty(propertyName);
    }

    public static void setProperty(String key, String value){

        Properties props = new Properties();
        try {
            FileInputStream in = new FileInputStream("config.properties");
            props.load(in);
            in.close();


        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                FileOutputStream out = new FileOutputStream("config.properties");
                props.setProperty(key, value);
                props.store(out, null);
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
