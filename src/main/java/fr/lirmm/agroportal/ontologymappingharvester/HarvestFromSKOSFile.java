package fr.lirmm.agroportal.ontologymappingharvester;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.swing.*;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HarvestFromSKOSFile extends  BaseService  implements HarvestService {


    File fileA;

    public HarvestFromSKOSFile(){
        super();
    }


    @Override
    public void loadOntology() {


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileA = fileChooser.getSelectedFile();


            //fileA = new File("/home/abrahao/data/downloads/thesaurus_pour_agroportal.rdf");

            try {
                oA = man.loadOntologyFromOntologyDocument(fileA);
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
//            System.out.println(oA);
//            oA.logicalAxioms().forEach(System.out::println);


//            OWLDocumentFormat format = oA.getNonnullFormat();
//
//            System.out.println("ONTOLOGY FORMAT:" + format.getKey());
//
//
//            System.out.println("ONTOLOGY:" + oA.toString());
//
//            OWLOntologyID ontologyID = oA.getOntologyID();
//
//            if (ontologyID != null) {
//                System.out.println("ID :" + ontologyID.toString());
//                Optional<IRI> ontologyIRI = ontologyID.getOntologyIRI();
//                if (ontologyIRI.isPresent()) {
//                    System.out.println("ONTOLOGYIRI :" + ontologyIRI.toString());
//                } else {
//                    System.out.println("Sem IRI");
//                }
//            } else {
//                System.out.println("Sem ID");
//            }
        }else{
            System.out.println("No file selected !!!");
        }
    }

    @Override
    public void findMatches() {

        System.out.println("----------------------------------------------------------");

        System.out.println("Classes");
        for (OWLClass c : oA.getClassesInSignature()) {
            System.out.println(c.toString());
            countClasses++;
        }
        System.out.println("Total Classes: "+countClasses);
        System.out.println("----------------------------------------------------------");

        System.out.println("Filter - Individuals");
        for (OWLNamedIndividual ind : oA.getIndividualsInSignature()) {


            Set<? extends OWLAxiom> axioms = oA.getAxioms(ind);
                for (OWLAxiom ax : axioms) {
                    System.out.println("Axiom " + ax);
                }
            axioms = oA.getAnnotationAssertionAxioms(ind.getIRI());
            for (OWLAxiom ax : axioms) {
                aux=ax.toString();
                if(aux.indexOf("<http://www.w3.org/2004/02/skos/core#exactMatch>")>-1){

                    an = Util.getAnnotationAssertationEntity(ax,countExactMatch++);
                    System.out.println(an.toString());
                    MapIRI = an.getOntology2();
                    if(mappings.containsKey(MapIRI)){
                        counter = mappings.get(MapIRI);
                        counter++;
                        mappings.put(MapIRI,counter);
                    }else{
                        mappings.put(MapIRI,1);
                    }


                }

            }
            // (<http://www.w3.org/2004/02/skos/core#exactMatch>


                axioms = oA.getDataPropertyAssertionAxioms(ind);
                for (OWLAxiom ax : axioms) {
                    System.out.println("Data Property Assertion Axiom " + ax);

                }
                axioms = oA.getObjectPropertyAssertionAxioms(ind);
                for (OWLAxiom ax : axioms) {
                    System.out.println("Object Property Assertion Axiom " + ax);
                }



            countIndividuals++;

        }

    }

    @Override
    public void printResults() {

        if(an.getOntology1() != null) {

            System.out.println("Ontology Examined: " + an.getOntology1());
            System.out.println("----------------------------------------------------------");

            System.out.println("Total Individuals: " + countIndividuals);
            System.out.println("----------------------------------------------------------");

            System.out.println("Matches to: http://www.w3.org/2004/02/skos/core#exactMatch");
            System.out.println("----------------------------------------------------------");

            System.out.println("Total ExactMatches: " + countExactMatch);
            System.out.println("----------------------------------------------------------");


            System.out.println("Number of matched ontologies: " + mappings.size());
            System.out.println("----------------------------------------------------------");
            Iterator it = mappings.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }

        }else{
            System.out.println("No matchs for skos exact match!");
        }

    }
}
