package fr.lirmm.agroportal.ontologymappingharvester;

import fr.lirmm.agroportal.ontologymappingharvester.facade.OntologyMappingHarvesterFacade;
import fr.lirmm.agroportal.ontologymappingharvester.services.*;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;

import java.util.ArrayList;

public class MappingHarvester {

    /**
     * Main method call to run the script
     * from command prompt
     * @param args
     */
    public static void main (String[] args){

        OntologyMappingHarvesterFacade facade = new OntologyMappingHarvesterFacade();
        facade.promptCall(args);

    }



}
