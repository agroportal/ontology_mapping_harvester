package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.utils.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import javax.annotation.Nonnull;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HarvestAllFormatsService extends BaseService implements HarvestService {


    File fileA;
    OWLDataFactory df;
    private String[] MATCH;
    private boolean isIRI;


    public HarvestAllFormatsService(){
        super();
        MATCH = new String[]{"http://www.w3.org/2002/07/owl#sameAs","http://www.geneontology.org/formats/oboInOwl#hasDbXref","http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
        isIRI = false;
    }


    public void loadOntology(String fileName){

        try {

            currentOntologyName = fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.lastIndexOf(".")).toUpperCase();

            fileIN = new File(fileName);

            oA = man.loadOntologyFromOntologyDocument(fileIN);
        } catch (OWLOntologyCreationException e) {
            printAndAppend("Error trying to load ontology from file");
            printAndAppend(e.getMessage());
        }


        printAndAppend("ONTOLOGY:" + oA.toString());

        ontologyID = oA.getOntologyID();

    }

    @Override
    public void findMatches() {

        boolean countIndividualsFlag = true;
        String auxProperty="";

        printAndAppend("----------------------------------------------------------");

        printAndAppend("Classes");
        for (OWLClass c : oA.getClassesInSignature()) {
            printAndAppend(c.toString());
            countClasses++;
        }
        printAndAppend("Total Classes: " + countClasses);
        printAndAppend("----------------------------------------------------------");

        printAndAppend("Filter - Annotations");

        for (int x = 0; x < MATCH.length; x++) {

            printAndAppend("======================= Searching for: " + MATCH[x] + "=======================");

            for (OWLClass cls : oA.getClassesInSignature()) {
                // Get the annotations on the class that use the label property

//                System.out.println("------------------------------------------------------------------------");
//                System.out.println("Classes: "+cls.toString());
//                System.out.println("------------------------------------------");

                for (OWLOntology o : oA.getImportsClosure()) {

                    //System.out.println("AnnotationAssetationAxioms: "+o.getAnnotationAssertionAxioms(cls.getIRI()));

                    //annotationObjects(o.getAnnotationAssertionAxioms(cls.getIRI()),label)

                    for (OWLAnnotationAssertionAxiom annotationAssertionAxiom : o.getAnnotationAssertionAxioms(cls.getIRI())) {
                        //System.out.println("Entrou no if do assetation");

                        aux = annotationAssertionAxiom.getProperty().toString();





                        //System.out.println("AnnotationAssertationAxiom: " + annotationAssertionAxiom);

                        if(aux.indexOf(MATCH[x]) > -1) {

                            printAndAppend("Ontology: "+ oA.getOntologyID());
                            printAndAppend("Subject: " + annotationAssertionAxiom.getSubject());
                            printAndAppend("Property: " + annotationAssertionAxiom.getProperty());

                            if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                                //System.out.println("Entrou no if do annotation get value");
                                OWLLiteral val = (OWLLiteral) annotationAssertionAxiom.getValue();
                                //if (val.hasLang("en")) {
                                printAndAppend("PropertyValueLiteral: " + val.getLiteral());
                                auxProperty = val.getLiteral();
                                isIRI = false;
                                //}
                            } else if (annotationAssertionAxiom.getValue() instanceof IRI) {
                                printAndAppend("PropertyValueIRI: " + annotationAssertionAxiom.getValue());
                                auxProperty = annotationAssertionAxiom.getValue().toString();
                                isIRI = true;
                                //}
                            }


                            an = Util.getAnnotationAssertationEntity(currentOntologyName,oA.getOntologyID().toString(),annotationAssertionAxiom.getSubject().toString(),annotationAssertionAxiom.getProperty().toString(),auxProperty,isIRI,++countMatch);
                            addToDeduplicationHash(an);
                            printAndAppend(an.toString());
                            MapIRI = an.getOntology2();
                            if (mappings.containsKey(MapIRI)) {
                                counter = mappings.get(MapIRI);
                                counter++;
                                mappings.put(MapIRI, counter);
                            } else {
                                mappings.put(MapIRI, 1);
                            }

                            //S//System.out.println("AnnotationAssertationAxiom: " + annotationAssertionAxiom);ystem.out.println("=======================");.



                            //System.out.println("Assertion with out anotation: " + annotationAssertionAxiom.getAnnotationPropertiesInSignature());



                        }



                    }

                    if(countIndividualsFlag) {
                        countIndividuals++;
                    }


                }
                //System.out.println("----------------------------------------------------------------------");
            }

            // INDIVIDUALS

            for (OWLNamedIndividual ind : oA.getIndividualsInSignature()) {


                Set<? extends OWLAxiom> axioms = oA.getAxioms(ind);
//                for (OWLAxiom ax : axioms) {
//                    System.out.println("Axiom " + ax);
//                }
                axioms = oA.getAnnotationAssertionAxioms(ind.getIRI());


                for (OWLAxiom ax : axioms) {
                    aux = ax.toString();
                    if (aux.indexOf(MATCH[x]) > -1) {

                        //printAndAppend("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX-->"+ax);
                        an = Util.getAnnotationAssertationEntity(ax, ++countMatch);
                        addToDeduplicationHash(an);
                        printAndAppend(an.toString());
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

                if (countIndividualsFlag) {
                    countIndividuals++;
                }

            }


            // FINISH INDIVIDUAIS




            if (mappings.size() > 0) {

                HashMap<String, Integer> mmp = SerializationUtils.clone(mappings);
                maps.put(MATCH[x], mmp);

            }
            mappings.clear();
            countIndividualsFlag=false;

        }


    }


    @Override
    public void saveFile() {


        if (an != null && an.getOntology1() != null) {

            printAndAppend("----------------------------------------------------------");
            printAndAppend("Ontology Examined: " + an.getOntology1());
            printAndAppend("----------------------------------------------------------");

            printAndAppend("Total Individuals: " + countIndividuals);
            printAndAppend("----------------------------------------------------------");


            for (Map.Entry<String, HashMap<String, Integer>> entry : maps.entrySet()) {
                String key = entry.getKey();
                HashMap<String, Integer> value = entry.getValue();

                printAndAppend("Matches to: " + key);
                printAndAppend("----------------------------------------------------------");

                for (Map.Entry<String, Integer> entry2 : value.entrySet()) {
                    String key2 = entry2.getKey();
                    Integer value2 = entry2.getValue();

                    printAndAppend("SubTotal: " + key2 + " --> " + value2);
                    printAndAppend("----------------------------------------------------------");

                }


            }

            printAndAppend("Total Matches       : "+totalAnnotationAssertationEntities);
            printAndAppend("Total Unique Matches: "+deduplicationHash.size());



        } else {
            printAndAppend("No matchs founded!");
        }



        writeFile();

    }


    @Override
    public void buildJson() {

        MappingEntity me;

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();

        printAndAppend("Begin generation of JSON file");

        int total = deduplicationHash.size();
        int count = 1;
        int current = 0;
        int last = 0;

        for (AnnotationAssertationEntity an : deduplicationHash.values()) {

            count++;
            current = (count*100/total);
            if(current%10==0){
                if(current != last) {
                    printAndAppend("-->" + (count * 100 / total) + "% done");
                    last = current;
                }
            }
            me = new MappingEntity();
            me.setId(an.getId());
            me.setCreator("http://data.agroportal.lirmm.fr/users/elcioabrahao");
            me.setSourceContactInfo("elcio.abrahao@lirmm.fr");
            me.setSource(an.getOntology1());
            me.setSourceName(an.getOntology1());
            me.setComment("Generated with the Ontology Mapping Harvest Tool - v.1.0 - Agroportal Project - LIRMM - FR");

            HashMap<String, String> classes = new HashMap<>();

            classes.put(an.getOntologyConcept1(), currentOntologyName);
            classes.put(an.getAssertion(), an.getOntologyConcept2());

            me.setClasses(classes);

            mappingEntities.add(me);

        }

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        writeJsonFile(gson.toJson(mappingEntities));
        printAndAppend("Finished generation of JSON file");

    }

    @Override
    public void parse(String command, ArrayList<String> files) {
        this.command = command;
        this.files = files;
        for (String file:files) {
            loadOntology(file);
            findMatches();
            saveFile();
            if(command.indexOf("j")>-1){
                buildJson();
            }

        }
    }



}
