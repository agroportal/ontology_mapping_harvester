package fr.lirmm.agroportal.ontologymappingharvester;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ExtRefList;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ExternalReference;

public class TesteJson {

    public static void main(String[] args){

        ExternalReference e = new ExternalReference();

        e.setIri("aaa");
        e.setLink("bbb");
        e.setSearchString("ccc");

        ExtRefList el = new ExtRefList();

        ExternalReference[] elist = new ExternalReference[3];

        elist[0] = e;
        elist[1] = e;
        elist[2] = e;

        el.setExternalReferences(elist);

        Gson gson = new GsonBuilder().create();
        String saida = gson.toJson( el);
        System.out.println(saida);

    }

}
