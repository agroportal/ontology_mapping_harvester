package fr.lirmm.agroportal.ontologymappingharvester;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HarvestFromOWLFile extends BaseService  implements HarvestService {


    File fileA;

    public HarvestFromOWLFile(){
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


            //OWLDocumentFormat format = oA.getNonnullFormat();

            //System.out.println("ONTOLOGY FORMAT:" + format.getKey());


            System.out.println("ONTOLOGY:" + oA.toString());

            OWLOntologyID ontologyID = oA.getOntologyID();

//            if (ontologyID != null) {
//                System.out.println("ID :" + ontologyID.toString());
//                //Optional<IRI> ontologyIRI = ontologyID.getOntologyIRI();
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

//        System.out.println("Classes");
//        for (OWLClass c : oA.getClassesInSignature()) {
//            System.out.println(c.toString());
//            countClasses++;
//        }
        System.out.println("Total Classes: "+countClasses);
        System.out.println("----------------------------------------------------------");


//        Set<OWLOntology> allOntologies = man.getImportsClosure(oA);
//        // System.out.println(allOntologies);
//        OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
//        OWLReasoner reasoner = reasonerFactory.createReasoner(oA);
//        // pizzaOntology
//        OWLDataFactory factory = man.getOWLDataFactory();
//        Map<OWLAnnotationSubject, String> allLabels = new HashMap<>();
//        Map<OWLAnnotationSubject, String> allExactSynonyms = new HashMap<>();
//        for (OWLAnnotationAssertionAxiom ax : oA
//                .getAxioms(AxiomType.ANNOTATION_ASSERTION)) {
//            // Check if the axiom is a label and write to file
//            OWLAnnotationSubject subject = ax.getSubject();
//            if (ax.getProperty().toString().contains("hasExactSynonym")) {
//                allExactSynonyms.put(subject, ax.getValue().toString());
//            }
//            if (ax.getProperty().equals(factory.getRDFSLabel())) {
//                String label = ax.getValue().toString();
//                label = label.toLowerCase();
//                label = label.replaceAll("[^\\p{L}\\p{Nd}]+", " ");
//                allLabels.put(subject, label);
//            }
//        }



        System.out.println("Filter - Individuals");
        for (OWLNamedIndividual ind : oA.getIndividualsInSignature()) {


            Set<? extends OWLAxiom> axioms = oA.getAxioms(ind);
            for (OWLAxiom ax : axioms) {
                System.out.println("Axiom " + ax);
                //System.out.println("Annotation Properties " + ax.annotationPropertiesInSignature().toString());



            }
//            axioms = oA.getAnnotationAssertionAxioms(ind.getIRI());
//            for (OWLAxiom ax : axioms) {
//                System.out.println("Axiom " + ax);
//
//                aux=ax.toString();
//                if(aux.indexOf("<http://www.w3.org/2004/02/skos/core#exactMatch>")>-1){
//
//                    an = Util.getAnnotationAssertationEntity(ax,countExactMatch++);
//                    System.out.println(an.toString());
//                    MapIRI = an.getOntology2();
//                    if(mappings.containsKey(MapIRI)){
//                        counter = mappings.get(MapIRI);
//                        counter++;
//                        mappings.put(MapIRI,counter);
//                    }else{
//                        mappings.put(MapIRI,1);
//                    }
//                }
//
//            }
            // (<http://www.w3.org/2004/02/skos/core#exactMatch>

//
//            axioms = oA.getDataPropertyAssertionAxioms(ind);
//            for (OWLAxiom ax : axioms) {
//                System.out.println("Data Property Assertion Axiom " + ax);
//
//            }
//            axioms = oA.getObjectPropertyAssertionAxioms(ind);
//            for (OWLAxiom ax : axioms) {
//                System.out.println("Object Property Assertion Axiom " + ax);
//            }



            countIndividuals++;

        }

    }

    @Override
    public void printResults() {

        System.out.println("Ontology Examined: "+an.getOntology1());
        System.out.println("----------------------------------------------------------");

        System.out.println("Total Individuals: "+countIndividuals);
        System.out.println("----------------------------------------------------------");

        System.out.println("Matches to: http://www.w3.org/2004/02/skos/core#exactMatch");
        System.out.println("----------------------------------------------------------");

        System.out.println("Total ExactMatches: "+countExactMatch);
        System.out.println("----------------------------------------------------------");


        System.out.println("Number of matched ontologies: "+mappings.size());
        System.out.println("----------------------------------------------------------");
        Iterator it = mappings.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

    }
}
