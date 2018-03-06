package fr.lirmm.agroportal.ontologymappingharvester;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;


import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HarvestFromSKOSFile extends BaseService implements HarvestService {



    private String[] SKOS_MATCH;


    public HarvestFromSKOSFile() {
        super();
        SKOS_MATCH = new String[]{"http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
    }


    @Override
    public boolean loadOntology() {


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileIN = fileChooser.getSelectedFile();


            //fileA = new File("/home/abrahao/data/downloads/thesaurus_pour_agroportal.rdf");

            try {
                oA = man.loadOntologyFromOntologyDocument(fileIN);
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
            return true;
        } else {
            System.out.println("No file selected !!!");
            return false;
        }
    }

    @Override
    public void findMatches() {

        boolean countIndividualsFlag = true;

        sb.append("----------------------------------------------------------\n");

        sb.append("Classes\n");
        for (OWLClass c : oA.getClassesInSignature()) {
            sb.append(c.toString()+"\n");
            countClasses++;
        }
        sb.append("Total Classes: " + countClasses+"\n");
        sb.append("----------------------------------------------------------\n");

        sb.append("Filter - Individuals\n");

        for (int x = 0; x < SKOS_MATCH.length; x++) {

            sb.append("======================= Searching for: " + SKOS_MATCH[x] + "=======================\n");

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
                        sb.append(an.toString()+"\n");
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

            if(countIndividualsFlag) {
                countIndividuals++;
            }

            }
            if (mappings.size() > 0) {

                HashMap<String, Integer> mmp = SerializationUtils.clone(mappings);
                maps.put(SKOS_MATCH[x], mmp);

            }
            mappings.clear();
            countIndividualsFlag=false;
        }

    }


    @Override
    public void printResults() {

        System.out.println(sb);


    }

    @Override
    public void saveFile() {


        String path = fileIN.getAbsolutePath();

        Path p = Paths.get(path);
        String fileName = p.getFileName().toString();
        String directory = p.getParent().toString();


        if (an != null && an.getOntology1() != null) {

            sb.append("----------------------------------------------------------\n");
            sb.append("Ontology Examined: " + an.getOntology1()+"\n");
            sb.append("----------------------------------------------------------\n");

            sb.append("Total Individuals: " + countIndividuals+"\n");
            sb.append("----------------------------------------------------------\n");


            for (Map.Entry<String, HashMap<String, Integer>> entry : maps.entrySet()) {
                String key = entry.getKey();
                HashMap<String, Integer> value = entry.getValue();

                sb.append("Matches to: " + key+"\n");
                sb.append("----------------------------------------------------------\n");

                for (Map.Entry<String, Integer> entry2 : value.entrySet()) {
                    String key2 = entry2.getKey();
                    Integer value2 = entry2.getValue();

                    sb.append("SubTotal: " + key2 + " --> " + value2+"\n");
                    sb.append("----------------------------------------------------------\n");

                }


            }




        } else {
            sb.append("No matchs founded!\n");
        }


        File f = new File(directory + File.separator+(fileName.replace(".rdf","").replace(".xrdf",""))+".txt");
        System.out.println(fileName);
        System.out.println(directory);
        System.out.println(f.getAbsolutePath());

        try {
            FileUtils.writeStringToFile(f,sb.toString(),"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
