package fr.lirmm.agroportal.ontologymappingharvester;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.Set;

public class Main {

    public static void main(String[] args){

        System.out.println("Initial commit");


        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology oA = null;

        int countClasses=0;
        int countIndividuals=0;

        File fileA = new File("/home/abrahao/data/downloads/thesaurus_pour_agroportal.rdf");


        try {

            oA = man.loadOntologyFromOntologyDocument(fileA);
//            System.out.println(oA);
//            oA.logicalAxioms().forEach(System.out::println);

            System.out.println("ONTOLOGY:"+oA.toString());

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
                    System.out.println("Annotation Assertion Axiom " + ax);
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
            System.out.println("Total Individuals: "+countIndividuals);
            System.out.println("----------------------------------------------------------");

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }

}
