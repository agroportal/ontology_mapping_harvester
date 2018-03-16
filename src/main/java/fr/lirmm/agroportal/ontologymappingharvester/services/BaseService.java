package fr.lirmm.agroportal.ontologymappingharvester.services;

import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class BaseService {


    OWLOntologyManager man;
    OWLOntology oA;

    int countClasses;
    int countIndividuals;
    int countMatch;
    int countTotalMatch;
    int totalAnnotationAssertationEntities;
    String aux;
    AnnotationAssertationEntity an;
    HashMap<String,AnnotationAssertationEntity> deduplicationHash;
    HashMap<String,HashMap<String,Integer>> maps;
    HashMap<String,Integer> mappings;
    int counter;
    String MapIRI;
    File fileIN;
    File fileOUT;
    StringBuffer sb;
    OWLOntologyID ontologyID;


    public BaseService(){

        man = OWLManager.createOWLOntologyManager();
        OWLOntology oA = null;
        totalAnnotationAssertationEntities=0;
        countClasses=0;
        countIndividuals=0;
        countMatch=0;
        countTotalMatch=0;
        aux="";
        an = null;
        mappings = new HashMap<>();
        maps = new HashMap<>();
        deduplicationHash = new HashMap<>();
        counter = 0;
        MapIRI="";
        sb = new StringBuffer("");

    }

    public void writeFile(){

        String path = fileIN.getAbsolutePath();

        Path p = Paths.get(path);
        String fileName = p.getFileName().toString();
        String directory = p.getParent().toString();

        File f = new File(directory + File.separator+(fileName.replace(".rdf","").replace(".xrdf",""))+".log");
        System.out.println(fileName);
        System.out.println(directory);
        System.out.println(f.getAbsolutePath());

        try {
            FileUtils.writeStringToFile(f,sb.toString(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void printAndAppend(String text){

        System.out.println(text);
        sb.append(text+"\n");

    }


    public void addToDeduplicationHash(AnnotationAssertationEntity an){
        deduplicationHash.put(an.getOntologyConcept1()+an.getAssertion()+an.getOntologyConcept2(),an);
        if(an.getId()>totalAnnotationAssertationEntities){
            totalAnnotationAssertationEntities=an.getId();
        }
    }




}
