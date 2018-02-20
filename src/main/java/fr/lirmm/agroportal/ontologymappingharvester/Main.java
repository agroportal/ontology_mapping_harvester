package fr.lirmm.agroportal.ontologymappingharvester;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

public class Main {

    public static void main(String[] args){

        System.out.println("Initial commit");


        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology oA = null;
        OWLOntology oB = null;

        File fileA = new File("/home/abrahao/data/downloads/pizza.owl");
        File fileB = new File("/home/abrahao/data/downloads/tutorial_local.owl");

        try {

            oA = man.loadOntologyFromOntologyDocument(fileA);
            System.out.println(oA);
            oB = man.loadOntologyFromOntologyDocument(fileB);

            System.out.println("-------------------------------------------------------------");

            System.out.println(oB);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

}
