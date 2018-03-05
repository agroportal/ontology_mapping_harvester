package fr.lirmm.agroportal.ontologymappingharvester;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.HashMap;

public class BaseService {


    OWLOntologyManager man;
    OWLOntology oA;

    int countClasses;
    int countIndividuals;
    int countMatch;
    int countTotalMatch;
    String aux;
    AnnotationAssertationEntity an;
    HashMap<String,HashMap<String,Integer>> maps;
    HashMap<String,Integer> mappings;
    int counter;
    String MapIRI;


    public BaseService(){

        man = OWLManager.createOWLOntologyManager();
        OWLOntology oA = null;

        countClasses=0;
        countIndividuals=0;
        countMatch=0;
        countTotalMatch=0;
        aux="";
        an = null;
        mappings = new HashMap<>();
        maps = new HashMap<>();
        counter = 0;
        MapIRI="";

    }




}
