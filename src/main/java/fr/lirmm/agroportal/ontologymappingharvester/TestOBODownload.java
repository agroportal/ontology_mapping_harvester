package fr.lirmm.agroportal.ontologymappingharvester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry.OBOOntologies;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class TestOBODownload {

    public static void main(String[] args) {

        try {
            URL url = new URL("http://obofoundry.org/registry/ontologies.jsonld");
            Scanner s = new Scanner(url.openStream());

            StringBuffer sb = new StringBuffer();

            while(s.hasNext()){
                sb.append(s.nextLine().trim());
            }

            System.out.println(sb.toString());

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();


            OBOOntologies out = gson.fromJson(sb.toString(),OBOOntologies.class);

            System.out.println(out.toString());

            // read from your scanner
        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
            ex.printStackTrace(); // for now, simply output it.
        }


    }

}
