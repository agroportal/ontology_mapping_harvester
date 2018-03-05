package fr.lirmm.agroportal.ontologymappingharvester;

import org.apache.commons.lang3.SerializationUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HarvestFromSKOSFile extends BaseService implements HarvestService {


    private File fileA;
    private String[] SKOS_MATCH;


    public HarvestFromSKOSFile() {
        super();
        SKOS_MATCH = new String[]{"http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
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
        } else {
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
        System.out.println("Total Classes: " + countClasses);
        System.out.println("----------------------------------------------------------");

        System.out.println("Filter - Individuals");

        for (int x = 0; x < SKOS_MATCH.length; x++) {

            System.out.println("======================" + SKOS_MATCH[x] + "=======================");

            for (OWLNamedIndividual ind : oA.getIndividualsInSignature()) {


                Set<? extends OWLAxiom> axioms = oA.getAxioms(ind);
//                for (OWLAxiom ax : axioms) {
//                    System.out.println("Axiom " + ax);
//                }
                axioms = oA.getAnnotationAssertionAxioms(ind.getIRI());


                for (OWLAxiom ax : axioms) {
                    aux = ax.toString();
                    if (aux.indexOf(SKOS_MATCH[x]) > -1) {

                        an = Util.getAnnotationAssertationEntity(ax, countMatch++);
                        System.out.println(an.toString());
                        MapIRI = an.getOntology2();
                        if (mappings.containsKey(MapIRI)) {
                            counter = mappings.get(MapIRI);
                            counter++;
                            mappings.put(MapIRI, counter);
                        } else {
                            mappings.put(MapIRI, 1);
                        }
                    }
                }

                // (<http://www.w3.org/2004/02/skos/core#exactMatch>


//                axioms = oA.getDataPropertyAssertionAxioms(ind);
//                for (OWLAxiom ax : axioms) {
//                    System.out.println("Data Property Assertion Axiom " + ax);
//
//                }
//                axioms = oA.getObjectPropertyAssertionAxioms(ind);
//                for (OWLAxiom ax : axioms) {
//                    System.out.println("Object Property Assertion Axiom " + ax);
//                }


                countIndividuals++;

            }
            if (mappings.size() > 0) {

                HashMap<String, Integer> mmp = SerializationUtils.clone(mappings);
                maps.put(SKOS_MATCH[x], mmp);
                System.out.println("Mappings: " + mappings.size());
                System.out.println("Maps: " + maps.size());

            }
            mappings.clear();
        }

    }


    @Override
    public void printResults() {

        if (an.getOntology1() != null) {

            System.out.println("Ontology Examined: " + an.getOntology1());
            System.out.println("----------------------------------------------------------");

            System.out.println("Total Individuals: " + countIndividuals);
            System.out.println("----------------------------------------------------------");


            for (Map.Entry<String, HashMap<String, Integer>> entry : maps.entrySet()) {
                String key = entry.getKey();
                HashMap<String, Integer> value = entry.getValue();

                System.out.println("Matches to: " + key);
                System.out.println("----------------------------------------------------------");

                for (Map.Entry<String, Integer> entry2 : value.entrySet()) {
                    String key2 = entry2.getKey();
                    Integer value2 = entry2.getValue();

                    System.out.println("SubTotal: " + key2 + " --> " + value2);
                    System.out.println("----------------------------------------------------------");

                }


            }


            System.out.println("Number of matched ontologies: " + mappings.size());
            System.out.println("----------------------------------------------------------");
            Iterator it = mappings.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }

        } else {
            System.out.println("No matchs for skos exact match!");
        }

    }
}
