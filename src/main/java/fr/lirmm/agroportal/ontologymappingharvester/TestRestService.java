package fr.lirmm.agroportal.ontologymappingharvester;

import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;

public class TestRestService {

    public static void main(String[] args){

        AgroportalRestService ars = new AgroportalRestService();
        ars.getOntologyAnnotation();

    }

}
