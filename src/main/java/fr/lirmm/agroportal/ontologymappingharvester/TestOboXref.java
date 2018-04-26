package fr.lirmm.agroportal.ontologymappingharvester;


import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;

import java.io.*;

public class TestOboXref {

    public static void main(String[] args){

        String dir = ManageProperties.loadPropertyValue("externalproperties");

        BufferedWriter writer = null;

        try(BufferedReader br = new BufferedReader(new FileReader(dir+File.separator+"AGRO.xrdf"))) {

            File file = new File(dir+File.separator+"AGRO_OUT_2.log");
            writer = new BufferedWriter( new FileWriter( file));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {

                if(line.indexOf("<oboInOwl:hasDbXref")>-1){
                    writer.write( line+"\n");
                }

                line = br.readLine();
            }




        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e)
            {
                e.printStackTrace();
            }
        }


    }

}
