package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.CurationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.IdentifierEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Contact;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import fr.lirmm.agroportal.ontologymappingharvester.utils.SortMapByValue;
import fr.lirmm.agroportal.ontologymappingharvester.utils.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.io.File;
import java.util.*;

public class HarvestAllFormatsService extends BaseService implements HarvestService {


    private String[] MATCH;
    private String[] INVALIDCHARACTERS;
    private boolean isIRI;


    /**
     * Constructor to initialize search mappings to search for.
     */
    public HarvestAllFormatsService() {
        super();
        MATCH = new String[]{"owl:sameAs", "http://www.w3.org/2002/07/owl#sameAs", "http://www.w3.org/2000/01/rdf-schema#seeAlso", "http://www.geneontology.org/formats/oboInOwl#hasDbXref", "http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
        INVALIDCHARACTERS = new String[]{"2",":","2","-","2","_","3"," ","1","[pthr]\\d{5}"};
        // "/^[pthr]\\d{5}$/"
        isIRI = false;
        this.files = files;

    }

    /**
     * Load ontology from local folder
     *
     * @param fileName
     */
    public void loadOntology(String fileName) {

        try {

            //System.out.println("FileName: "+fileName);
            currentOntologyName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.lastIndexOf(".")).toUpperCase();

            fileIN = new File(fileName);

            oA = man.loadOntologyFromOntologyDocument(fileIN);
        } catch (OWLOntologyCreationException e) {
            errorLogger.error("Error trying to load ontology from file: " + e.getMessage());
        }


        stdoutLogger.info("ONTOLOGY (to.string()): " + oA.toString());

        ontologyID = oA.getOntologyID();

    }

    /**
     * Download ontology from Portal
     *
     * @param acronym
     * @param address
     * @param dir
     * @throws OWLOntologyCreationException
     */
    public void downloadOntology(String acronym, String address, String dir) throws OWLOntologyCreationException {

        //System.out.println("Acro   : "+acronym);
        //System.out.println("Address: "+address);
        //System.out.println("Dir    : "+dir);

        fileIN = new File(dir + File.separator + acronym.toUpperCase() + ".xrdf");

        //System.out.println("FileIn    : "+fileIN);
        // Agroportal DEMO KEY

        String apiKey = ManageProperties.loadPropertyValue("apikey");

        if (command.indexOf("n") > -1) {
            apiKey = ManageProperties.loadPropertyValue("apikeybio");
        }

        IRI iri = IRI.create(address + "?apikey=" + apiKey + "&download_format=rdf");

        stdoutLogger.warn(address + "?apikey=" + apiKey + "&download_format=rdf");

        try {
            oA = man.loadOntology(iri);

        } catch (UnloadableImportException e1) {
            errorLogger.error("Error trying to import ontology: " + e1.getMessage());
            saveFile();
        }

        stdoutLogger.info("ONTOLOGY (to.string()): " + oA.toString());

        ontologyID = oA.getOntologyID();

    }


    /**
     * Parse ontology searching for matches
     */
    @Override
    public void findMatches() {

        boolean countIndividualsFlag = true;
        String auxProperty = "";


        for (OWLClass c : oA.getClassesInSignature()) {
            countClasses++;
        }
        stdoutLogger.info("Total of Classes: " + countClasses);

        stdoutLogger.info("Begin search for matchs on class anntations...");

        for (int x = 0; x < MATCH.length; x++) {

            stdoutLogger.info("======================= Searching matchs for: " + MATCH[x] + "=======================");

            // Iterte on Classes
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

                        aux = annotationAssertionAxiom.toString();

                        if (aux.indexOf(MATCH[x]) > -1) {

                            stdoutLogger.info("Ontology: " + currentOntologyId);
                            stdoutLogger.info("Subject : " + annotationAssertionAxiom.getSubject());
                            stdoutLogger.info("Property: " + annotationAssertionAxiom.getProperty());
                            stdoutLogger.info("Value   : " + annotationAssertionAxiom.getValue());

                            if(annotationAssertionAxiom.getProperty().toString().equalsIgnoreCase("owl:sameas")){

                                if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                                    //System.out.println("Entrou no if do annotation get value");
                                    OWLLiteral val = (OWLLiteral) annotationAssertionAxiom.getValue();
                                    //if (val.hasLang("en")) {
                                    stdoutLogger.info("PropertyValueLiteral: " + val.getLiteral());
                                    auxProperty = val.getLiteral();
                                    isIRI = false;
                                    //}
                                } else if (annotationAssertionAxiom.getValue() instanceof IRI) {
                                    stdoutLogger.info("PropertyValueIRI: " + annotationAssertionAxiom.getValue());
                                    auxProperty = annotationAssertionAxiom.getValue().toString();
                                    isIRI = true;
                                    //}
                                }

                                //CLII
                                an = getAnnotationAssertationEntity(currentOntologyName, currentOntologyId, annotationAssertionAxiom.getSubject().toString(), annotationAssertionAxiom.getProperty().toString(), auxProperty, isIRI, ++countMatch);

                                // Variation one
                                addToDeduplicationHash(an,1);


                            } else {


                                for (OWLAnnotation aaa : annotationAssertionAxiom.getAnnotations()) {

                                    if (aaa.getProperty().toString().indexOf(MATCH[x]) > -1) {
                                        if (aaa.getValue() instanceof OWLLiteral) {
                                            //System.out.println("Entrou no if do annotation get value");
                                            OWLLiteral val = (OWLLiteral) aaa.getValue();
                                            //if (val.hasLang("en")) {
                                            stdoutLogger.info("PropertyValueLiteral: " + val.getLiteral());
                                            auxProperty = val.getLiteral();
                                            isIRI = false;
                                            //}
                                        } else if (aaa.getValue() instanceof IRI) {
                                            stdoutLogger.info("PropertyValueIRI: " + annotationAssertionAxiom.getValue());
                                            auxProperty = aaa.getValue().toString();
                                            isIRI = true;
                                            //}
                                        }

                                        //CLII
                                        an = getAnnotationAssertationEntity(currentOntologyName, currentOntologyId, annotationAssertionAxiom.getSubject().toString(), aaa.getProperty().toString(), auxProperty, isIRI, ++countMatch);

                                        // variation 2
                                        addToDeduplicationHash(an,2);
                                    }

                                }
                            }
                        }


                    }

                    if (countIndividualsFlag) {
                        countIndividuals++;
                    }


                }
                //System.out.println("----------------------------------------------------------------------");
            }

            // INDIVIDUALS

            stdoutLogger.info("End search of matchs in classes.");
            stdoutLogger.info("Begin search for individuals...");

            boolean assertationLookup = true;

            for (OWLNamedIndividual ind : oA.getIndividualsInSignature()) {


                Set<? extends OWLAxiom> axioms = oA.getAxioms(ind);
//                for (OWLAxiom ax : axioms) {
//                    System.out.println("Axiom " + ax);
//                }
                axioms = oA.getAnnotationAssertionAxioms(ind.getIRI());

                assertationLookup = true;


                for (OWLAxiom ax : axioms) {

                    //System.out.println("*** INDIVIDUAL AXIOMS *** of Individual: "+ind.getIRI());
                    //System.out.println("*** INDIVIDUAL AXIOMS :  "+ax);


                    for (OWLAnnotation owlAnnotation : ax.getAnnotations()) {

                        aux = ax.toString().toLowerCase().replace("\n", "").trim();
                        //printAndAppend("-->"+aux+"<--");

                        assertationLookup = false;

                        if (owlAnnotation.toString().indexOf(MATCH[x]) > -1) {

                            //System.out.println("===IND1: "+ind.toString());

                            //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX --->");

                            // CLI
                            an = getAnnotationAssertationEntity(owlAnnotation, ind.getIRI(), MATCH[x], ++countMatch);
                            //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX <---");

                            // variation 3
                            addToDeduplicationHash(an,3);

                        }
                    }

                    // special case for SKOS Named Individuals
                    if (assertationLookup) {

                        if (ax.getAnnotationPropertiesInSignature().toString().indexOf(MATCH[x]) > -1) {

                            //printAndAppend("Assertation: " + ax.getAnnotationPropertiesInSignature());

                            //System.out.println("===IND2: "+ind.toString());

                            //SKOS
                            an = getAnnotationAssertationEntity(ax.toString(), ind.getIRI(), MATCH[x], ++countMatch);

                            // variation 4
                            addToDeduplicationHash(an,4);


                        }

                    }


                    //printAndAppend("*** END OF INDIVIDUAL AXIOMS ***");

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

            stdoutLogger.info("End search of matchs in individuals.");

            // FINISH INDIVIDUAIS
            countIndividualsFlag = false;

        }


    }


    public void totalizeMappings() {

        //addToDeduplicationHash(an, variation);
        String relation = "";


        for (Map.Entry<String, AnnotationAssertationEntity> entry : deduplicationHash.entrySet()) {
            String key2 = entry.getKey();
            AnnotationAssertationEntity an = entry.getValue();

            MapIRI = an.getOntology2();
            if(MapIRI.trim().length()!=0) {

                relation = an.getAssertion();
                if (mappings.containsKey(MapIRI)) {
                    counter = mappings.get(MapIRI);
                    counter++;
                    mappings.put(MapIRI, counter);
                } else {
                    mappings.put(MapIRI, 1);
                }
                if (totalMappings.containsKey(MapIRI)) {
                    counter = totalMappings.get(MapIRI);
                    counter++;
                    totalMappings.put(MapIRI, counter);
                } else {
                    totalMappings.put(MapIRI, 1);
                }
                if (externalTargetReferenceHashMap.containsKey(MapIRI)) {
                    curationEntity = externalTargetReferenceHashMap.get(MapIRI);
                    curationEntity.addMatch();
                    // add only if it is not already there
                    if (curationEntity.getFoundedIn().indexOf(currentOntologyName) == -1) {
                        curationEntity.addFoundedIn(currentOntologyName);
                    }
                    if (curationEntity.getExampleList().indexOf(an.getOntologyConcept2()) == -1) {
                        curationEntity.addExampleList(an.getOntologyConcept2());
                    }
                    externalTargetReferenceHashMap.put(MapIRI, curationEntity);
                } else {
                    curationEntity = new CurationEntity();
                    curationEntity.addMatch();
                    curationEntity.addExampleList(an.getOntologyConcept2());
                    curationEntity.addFoundedIn(currentOntologyName);
                    curationEntity.setTargetFounded(MapIRI);
                    externalTargetReferenceHashMap.put(MapIRI, curationEntity);
                }
                if (maps.containsKey(relation + ";" + MapIRI)) {
                    counter = maps.get(relation + ";" + MapIRI);
                    counter++;
                    maps.put(relation + ";" + MapIRI, counter);
                } else {
                    maps.put(relation + ";" + MapIRI, 1);
                }
            }else{
                errorLogger.error("INVALID TARGET --> "+an.toString());
            }

        }






        //phase1Logger.trace("REGISTER;"+(++targetRegisterCounter)+";"+MapIRI.replaceAll(";","")+";"+an.toStringFlat());


    }


    /**
     * Generate LOG file information
     */
    @Override
    public void saveFile() {


        if (an != null && an.getOntology1() != null) {

            stdoutLogger.info("----------------------------------------------------------");
            stdoutLogger.info("Ontology Examined: " + currentOntologyName);
            stdoutLogger.info("----------------------------------------------------------");
            stdoutLogger.info("Total Classes    : " + countClasses);
            stdoutLogger.info("Total Individuals: " + countIndividuals);
            stdoutLogger.info("----------------------------------------------------------");

            stdoutLogger.info("Matchs per Type for " + currentOntologyName);
            stdoutLogger.info("----------------------------------------------------------");


            String[] info;

                for (Map.Entry<String, Integer> entry2 : maps.entrySet()) {
                    String key2 = entry2.getKey();
                    Integer value2 = entry2.getValue();
                    //System.out.println("Key2-->"+key2+"<--");
                    info = key2.split(";");
                    stdoutLogger.info("Sub Total Match: " + info[0] + " Ontology: "+info[1]+" --> " + value2);
                    stdoutLogger.info("----------------------------------------------------------");
                    totalizationLogger.info(Util.getDateTime() + ";" + currentOntologyName.replaceAll(";","") + ";" + currentOntologyId.replaceAll(";","") + ";" + info[0] + ";" + info[1] + ";" + value2 + ";");
//                    phase1Logger.info("REGISTER;"+(++targetRegisterCounter)+";"+currentOntologyName.replaceAll(";","") + ";" + currentOntologyId.replaceAll(";","") + ";" + key.replaceAll(";","") + ";" + key2.replaceAll(";","") + ";" + value2 + ";");

                }




            stdoutLogger.info("Total Matches for " + currentOntologyName);
            stdoutLogger.info("----------------------------------------------------------");

            for (Map.Entry<String, Integer> entry2 : totalMappings.entrySet()) {
                String key2 = entry2.getKey();
                Integer value2 = entry2.getValue();

                stdoutLogger.info("Total: " + key2 + " --> " + value2);
                stdoutLogger.info("----------------------------------------------------------");

            }

            stdoutLogger.info("Total Matches       : " + totalAnnotationAssertationEntities);
            stdoutLogger.info("Total Unique Matches: " + deduplicationHash.size());
            summaryLogger.info(currentOntologyName+";"+deduplicationHash.size());


        } else {
            summaryLogger.info(currentOntologyName+";0");
            stdoutLogger.warn("No matchs founded!");
        }


    }


    /**
     * Generate JSON files to export mappings
     */
    @Override
    public void buildJson() {

        MappingEntity me;

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();

        stdoutLogger.info("Begin generation of temporary JSON file");

        int total = deduplicationHash.size();
        int count = 1;
        int current = 0;
        int last = 0;

        stdoutLogger.info("-->0% done");
        for (AnnotationAssertationEntity an : deduplicationHash.values()) {

            count++;
            current = (count * 100 / total);
            if (current % 10 == 0) {
                if (current != last) {
                    stdoutLogger.info("-->" + (count * 100 / total) + "% done");
                    last = current;
                }
            }
            me = new MappingEntity();
            me.setId(an.getId());
            me.setCreator("http://data.agroportal.lirmm.fr/users/elcioabrahao");
            me.setSourceContactInfo(ontologyContactEmail);
            me.setSource(currentOntologyId);
            me.setSourceName(currentOntologyName);
            me.setComment("Generated with the Ontology Mapping Harvest Tool - v.1.0 - Agroportal Project - LIRMM - " + Util.getFormatedDateTime("dd/MM/yyyy HH:mm") + " - FR");
            String[] mappings = new String[1];
            mappings[0] = an.getAssertion();
            me.setRelation(mappings);

            HashMap<String, String> classes = new HashMap<>();

            classes.put(an.getOntologyConcept1(), currentOntologyName);
            classes.put(an.getOntologyConcept2(), (command.indexOf("n") > -1 ? "ncbo:" + an.getOntology2() : "agroportal:" + an.getOntology2()));

            me.setClasses(classes);

            mappingEntities.add(me);

        }

        if (mappingEntities.size() > 0) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            writeJsonFile(gson.toJson(mappingEntities), false);
            stdoutLogger.info("Finished generation of temporary JSON file");
        } else {
            stdoutLogger.warn("No matches - JSON file generation skiped!");
        }

    }

    /**
     * Generate Statistics nodes for graph representation
     */
    public void generateStatistics() {

        String aux = "";

        addStat("rootnode;" + currentOntologyName + ";" + countIndividuals + ";" + currentOntologyName);

        for (Map.Entry<String, Integer> entry2 : totalMappings.entrySet()) {
            String key2 = entry2.getKey().replaceAll("'","").replaceAll(";","");
            Integer value2 = entry2.getValue();

            if (key2.indexOf("http") == 0) {
                //key2 = key2.substring(key2.lastIndexOf(File.separator)+1,key2.length()).toUpperCase();
                key2 = parseLinkReference(key2);
            }


            addStat("node;" + key2 + ";" + value2 + ";" + key2);
            addStat("edge;" + currentOntologyName + ";" + key2 + ";" + value2 + ";" + value2 + " matches from " + currentOntologyName + " to " + key2);
        }
        if (sts.toString().length() > 0) {
            stdoutLogger.info("Statistic file was generated");
            writeStatFile();
        } else {
            stdoutLogger.warn("No matches, Statistic file generation skiped!");
        }


    }


    /**
     * Read ontology files from folder informed on runtime command line
     *
     * @param dirName
     * @return
     */
    private ArrayList<String> getFilesFromFolder(String dirName) {

        File dir = new File(dirName);
        String[] extensions = new String[]{"xrdf", "owl", "rdf"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        ArrayList<String> lista = new ArrayList<>();
        for (File file : files) {
            //System.out.println("File: "+file);
            lista.add(file.toString());
        }

        return lista;
    }


    /**
     * Create AnnotationAssertationEntity for classes Variation I
     *
     * @param anot
     * @param iri
     * @param assertion
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(OWLAnnotation anot, IRI iri, String assertion, int id) {


        String aux = anot.toString().replace("\n", "").trim();
        String aux2 = "";
        String ret = "";
        int count=0;

        int indexOf1 = 0;
        int indexOf2 = 0;
        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);
        an.setAssertion(assertion);
        an.setOntology1(currentOntologyName);
        an.setOntologyConcept1(iri.toString());


        indexOf1 = aux.indexOf("\"");
        indexOf2 = aux.lastIndexOf("\"");

        if (indexOf1 < indexOf2) {

            aux2 = aux.substring(indexOf1 + 1, indexOf2);
            //an.setAssertion(aux.substring(indexOf1+1,indexOf2-1));
            //System.out.println("ENTROUAQUI-->"+aux.substring(indexOf1+1,indexOf2-1));
            aux2 = preClean(aux2);

            count = aux2.length() - aux2.replace(" ","").length();

            an.setOntology2(ret);

            if(isValidMap(aux2)) {

                if (aux2.indexOf("http") == 0 || aux2.indexOf("smtp") == 0 || aux2.indexOf("ftp") == 0) {
                    an.setOntology2(parseLinkReference(aux2));
                    externalLogger.trace("CLI--HTTP--BEFORE: " + aux2);
                    externalLogger.trace("CLI--HTTP--AFTER : " + an.getOntology2());
                } else if (aux2.indexOf(":") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux2.substring(0, aux2.indexOf(":"))));
                    externalLogger.trace("CLI-:-BEFORE: " + aux2);
                    externalLogger.trace("CLI-:-AFTER : " + an.getOntology2());
                } else if (aux2.indexOf("_") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux2.substring(0, aux2.indexOf("_"))));
                    externalLogger.trace("CLI-_-BEFORE: " + aux2);
                    externalLogger.trace("CLI-_-AFTER : " + an.getOntology2());
                } else if (aux2.indexOf("-") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux2.substring(0, aux2.indexOf("-"))));
                    externalLogger.trace("CLI---BEFORE: " + aux2);
                    externalLogger.trace("CLI---AFTER : " + an.getOntology2());
                } else {

                    an.setOntology2(mapExternalLink2(aux2));
                    an.setBaseClassURI(aux2);
                    externalLogger.trace("CLI-ELSE-BEFORE: " + aux2);
                    externalLogger.trace("CLI-ELSE-AFTER : " + an.getOntology2());
                }

            }else{
                externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux2+"<--");
                an.setOntology2("UNKNOW_ONTOLOGY");
            }
            an.setOntologyConcept2(aux2);


        } else {
            // in case of a null property value
            externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux2+"<--");
            an.setOntologyConcept2("UNMAPPED_MAPPING");
            an.setOntology2("UNKNOW_ONTOLOGY");


        }


        return an;
    }


    /**
     * Create AnnotationAssertationEntity for Classes Variation II
     *
     * @param currentOntologyName
     * @param ontologyName
     * @param subject
     * @param property
     * @param propertyValue
     * @param isIRI
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(String currentOntologyName, String ontologyName, String subject, String property, String propertyValue, boolean isIRI, int id) {


        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);

        String name = currentOntologyName;
        String aux = "";
        String ret = "";
        int count=0;

        an.setOntology1(name);
        an.setOntologyConcept1(subject.replace("\n", "").trim());
        an.setOntologyConcept2(propertyValue.replace("\n", "").trim());
        an.setAssertion(property.replace("<", "").replace(">", "").replace("\n", "").trim());

        aux = propertyValue.toLowerCase().replace("\n", "").trim();

        count = aux.length() - aux.replace(" ","").length();

        if (isIRI) {

            // Enter here if it is not an IRI

            if(isValidMap(aux)) {

                if(aux.indexOf("http")==0 || aux.indexOf("smtp")==0 || aux.indexOf("ftp")==0){
                    an.setOntology2(parseLinkReference(aux));
                    externalLogger.trace("CLII-IRI-HTTP-BEFORE: "+aux);
                    externalLogger.trace("CLII-IRI-HTTP-AFTER : "+an.getOntology2());
                } else if (aux.indexOf(":") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf(":"))));
                    externalLogger.trace("CLII-IRI-:--BEFORE: "+aux);
                    externalLogger.trace("CLII-IRI-:--AFTER : "+an.getOntology2());
                } else if (aux.indexOf("_") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("_"))));
                    externalLogger.trace("CLII-IRI-_--BEFORE: "+aux);
                    externalLogger.trace("CLII-IRI-_--AFTER : "+an.getOntology2());
                } else if (aux.indexOf("-") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("-"))));
                    externalLogger.trace("CLII-IRI----BEFORE: "+aux);
                    externalLogger.trace("CLII-IRI----AFTER : "+an.getOntology2());
                } else {
                    an.setOntology2(mapExternalLink2(aux));
                    an.setBaseClassURI(aux);
                    externalLogger.trace("CLII-IRI-ELSE--BEFORE: "+aux);
                    externalLogger.trace("CLII-IRI-ELSE--AFTER : "+an.getOntology2());
                }
            }else{
                externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux+"<--");
                an.setOntology2("UNKNOW_ONTOLOGY");
            }

        } else {

            // Enter here if it is not an IRI

            if(isValidMap(aux)) {

                if(aux.indexOf("http")==0 || aux.indexOf("smtp")==0 || aux.indexOf("ftp")==0){
                    an.setOntology2(parseLinkReference(aux));
                    externalLogger.trace("CLII-NIRI-HTTP--BEFORE: "+aux);
                    externalLogger.trace("CLII-NIRI-HTTP--AFTER : "+an.getOntology2());
                } else if (aux.indexOf(":") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf(":"))));
                    externalLogger.trace("CLII-NIRI-:--BEFORE: "+aux);
                    externalLogger.trace("CLII-NIRI-:--AFTER : "+an.getOntology2());
                } else if (aux.indexOf("_") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("_"))));
                    externalLogger.trace("CLII-NIRI-_--BEFORE: "+aux);
                    externalLogger.trace("CLII-NIRI-_--AFTER : "+an.getOntology2());
                } else if (aux.indexOf("-") > 0 && count <= maxSpaceOcorrencies) {
                    an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("-"))));
                    externalLogger.trace("CLII-NIRI----BEFORE: "+aux);
                    externalLogger.trace("CLII-NIRI----AFTER : "+an.getOntology2());
                } else {
                    an.setOntology2(mapExternalLink2(aux));
                    an.setBaseClassURI(aux);
                    externalLogger.trace("CLII-NIRI-ELSE--BEFORE: "+aux);
                    externalLogger.trace("CLII-NIRI-ELSE--AFTER : "+an.getOntology2());
                }
            }else{
                externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux+"<--");
                an.setOntology2("UNKNOW_ONTOLOGY");
            }


        }


        return an;

    }


    /**
     * Create AnnotationSsertationEntity from class individuals Variation III
     *
     * @param anot
     * @param iri
     * @param assertion
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(String anot, IRI iri, String assertion, int id) {

        // TODO check the cleanup of the literal type ^^xsd:string)
        String aux = anot.toString().replace("\n", "").trim().replace("^^xsd:string)","");
        String aux2 = "";
        String aux3="";
        String aux4="";

        int count=0;


        int indexOf1 = 0;
        int indexOf2 = 0;
        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);
        an.setAssertion(assertion);
        an.setOntology1(currentOntologyName);
        an.setOntologyConcept1(iri.toString());


        if (aux.length() > 6) {

            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            an.setAssertion(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));
            aux = aux.substring(indexOf2 + 1);

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");

            while (indexOf2 <= indexOf1) {
                aux = aux.substring(indexOf2 + 1, aux.length());
                indexOf1 = aux.indexOf("<");
                indexOf2 = aux.indexOf(">");
            }

            an.setOntologyConcept1(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));

            an.setOntology1(an.getOntologyConcept1().substring(0, an.getOntologyConcept1().lastIndexOf("/")));

            aux = aux.substring(indexOf2 + 1).replace(" ", "").replace("\"", "");
            count = aux.length() - aux.replace(" ","").length();
            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");


            if (indexOf1 > -1 && indexOf2 > 1) {

                if(isValidMap(aux)) {
                    externalLogger.trace("IDIII--SKOS--BEFORE ORIGINAL: " + aux);


                    an.setOntologyConcept2(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));
                    externalLogger.trace("IDIII--SKOS--DURING1 CONCEPT: " + an.getOntologyConcept2());

                    aux2 = an.getOntologyConcept2();
                    if (aux2.length() > 0 && aux2.substring(aux2.length() - 1, aux2.length()).equalsIgnoreCase("/")) {
                        an.setOntologyConcept2(aux2.substring(0, aux2.length() - 1));
                        externalLogger.trace("IDIII--SKOS--DURING2 CONCEPT: " + an.getOntologyConcept2());
                    }

                    an.setOntology2(mapExternalLink2(an.getOntologyConcept2().substring(0, an.getOntologyConcept2().lastIndexOf("/"))));
                    externalLogger.trace("IDIII--SKOS--AFTER ONTOLOGY: " + an.getOntology2());
                }else{
                    externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux+"<--");
                    an.setOntology2("UNKNOW_ONTOLOGY");
                }
            } else {

                if(isValidMap(aux)) {
                    if (aux.indexOf("http") == 0 || aux.indexOf("smtp") == 0 || aux.indexOf("ftp") == 0) {
                        an.setOntology2(parseLinkReference(aux));
                        externalLogger.trace("IDIII--HTTP--BEFORE: " + aux);
                        externalLogger.trace("IDIII--HTTP--AFTER : " + an.getOntology2());
                    } else if (aux.indexOf(":") > 0 && count <= maxSpaceOcorrencies) {
                        an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf(":"))));
                        externalLogger.trace("IDIII--:--BEFORE: " + aux);
                        externalLogger.trace("IDIII--:--AFTER : " + an.getOntology2());
                    } else if (aux.indexOf("_") > 0 && count <= maxSpaceOcorrencies) {
                        an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("_"))));
                        externalLogger.trace("IDIII--_--BEFORE: " + aux);
                        externalLogger.trace("IDIII--_--AFTER : " + an.getOntology2());
                    } else if (aux.indexOf("-") > 0 && count <= maxSpaceOcorrencies) {
                        an.setOntology2(mapExternalLink2(aux.substring(0, aux.indexOf("-"))));
                        externalLogger.trace("IDIII-----BEFORE: " + aux);
                        externalLogger.trace("IDIII-----AFTER : " + an.getOntology2());
                    } else {
                        an.setOntology2(mapExternalLink2(aux));
                        an.setBaseClassURI(aux);
                        externalLogger.trace("IDIII--ELSE--BEFORE: " + aux);
                        externalLogger.trace("IDIII--ELSE--AFTER : " + an.getOntology2());
                    }
                }else{
                    externalLogger.error("UNKNOW_ONTOLOGY: -->"+aux+"<--");
                    an.setOntology2("UNKNOW_ONTOLOGY");
                }

                // TODO check if we could take out the referrence for the type of the literal.
                an.setOntologyConcept2(aux);
            }

        }

        return an;
    }



    /**
     * Find root link for reference
     * @param value
     * @return
     */
    public String parseLinkReference(String value){

        String partA="";
        String partB="";

        if(value.indexOf("http://")==0){
            partA="http://";
            partB=value.replace("http://","");
            if(partB.indexOf("/")>-1){
                partB = partB.substring(0, partB.indexOf("/"));
            }

        }else if(value.indexOf("https://")==0){
            partA="https://";
            partB=value.replace("https://","");
            if(partB.indexOf("/")>-1){
                partB = partB.substring(0, partB.indexOf("/"));
            }
        }else if(value.indexOf("ftp://")==0){
            partA="ftp://";
            partB=value.replace("ftp://","");
            if(partB.indexOf("/")>-1){
                partB = partB.substring(0, partB.indexOf("/"));
            }
        }else if(value.indexOf("smtp://")==0){
            partA="smtp://";
            partB=value.replace("smtp://","");
            if(partB.indexOf("/")>-1){
                partB = partB.substring(0, partB.indexOf("/"));
            }
        }

        return partA+partB;
    }


    /**
     * Map external Link that is already curated
     * @param value
     * @return
     */
    public String mapExternalLink2(String value) {

        String process;
        CurationEntity er;

        if(value !=null ){
            process = value.replaceAll("\n","").trim().toLowerCase();
            er = externalTargetReferenceHashMap.get(process);
            if(er != null){
                return er.getCuratedTarget();
            }else{
                    return process;
            }
        }else{
            externalLogger.error("IMPOSSIBLE TO MAP REFERENCE: "+value+" FOR ONTOLOGY: "+currentOntologyName);
            return "UNKNOW_ONTOLOGY";
        }
    }

    /**
     * Get base class URI
     * @param value
     * @return
     */
    public String getBaseClassURI(String value) {

        String process;
        CurationEntity er;

        if(value !=null ){
            process = value.replaceAll("\n","").trim().toLowerCase();
            er = externalTargetReferenceHashMap.get(process);
            if(er != null){
                return er.getBaseClassURI();
            }else{
                return "";
            }
        }else{
            externalLogger.error("IMPOSSIBLE TO FIND BASE CLASS URI: "+value+" FOR ONTOLOGY: "+currentOntologyName);
            return "";
        }
    }

    private boolean isValidMap(String value){

        for(int i =0;i<INVALIDCHARACTERS.length-1;i=i+2){

            if(value.matches("(.*"+INVALIDCHARACTERS[i+1]+".*){"+INVALIDCHARACTERS[i]+",}")){
                externalLogger.warn("INVALID CONCEPT FOUNDED - Ontology: "+currentOntologyName+" Concept: "+value);
                return false;
            }
        }
        return true;
    }


    private String preClean(String searchString) {

        String aux = searchString;
        int index1 = searchString.indexOf(":");
        int index2 = searchString.lastIndexOf(":");
        if (index2 > index1) {
            aux = searchString.substring(index1 + 1, searchString.length());
            //System.out.println("*************************************--->>>"+aux);

        }

        return mapExternalLink2(aux);

    }

    /**
     * Method to generate the Manual Curation File
     */
    public void generatePhase1Targets(){

        loadOBOFoundryOntologies();
        loadIdentifiersOntologies();

        stdoutLogger.info("Local portal verification initiated: "+Util.getDateTime());

        CurationEntity ce = null;
        String key="";
        String keyLocalPortal=null;
        String result=null;
        int status =0;

        for (Map.Entry<String, CurationEntity> entry2 : externalTargetReferenceHashMap.entrySet()) {
            key = entry2.getKey().toUpperCase();
            ce = entry2.getValue();

            // Loockup for external references only if status = 0 (not curated)
            if (ce.getStatus() == 0) {

                if (command.indexOf("n") > -1) {
                    // LOOKUP ON BIOPORTAL THEN ON AGROPORTAL
                    keyLocalPortal = ontologyNameHashMapBio.get(key);
                    if (keyLocalPortal == null) {
                        keyLocalPortal = ontologyNameHashMapAgro.get(key);
                        if (keyLocalPortal != null) {
                            result = "AGROPORTAL:" + key;
                            status = 1;
                        }
                    } else {
                        result = "NCBO:" + key;
                        status = 2;
                    }
                } else {
                    // LOOKUP ON AGROPORTAL THEN ON BIOPORTAL
                    keyLocalPortal = ontologyNameHashMapAgro.get(key);
                    if (keyLocalPortal == null) {
                        keyLocalPortal = ontologyNameHashMapBio.get(key);
                        if (keyLocalPortal != null) {
                            result = "NCBO:" + key;
                            status = 2;
                        }
                    } else {
                        result = "AGROPORTAL:" + key;
                        status = 1;
                    }
                }
                if (result != null) {

                    //System.out.println();
                    ce.setBaseClassURI(keyLocalPortal);
                    ce.setCuratedTarget(result);
                    ce.setCuredtedBy("OMHT");
                    ce.setComments("Founded on Agroportal or Bioportal");
                    ce.setDate(Util.getDateTime());
                    ce.setStatus(status);
                    externalTargetReferenceHashMap.put(key.toLowerCase(), ce);
                    //System.out.println(ce.toString());

                } else {

                    // LOOKUP ON AGROPORTAL BY THE URI
                    key = key.toLowerCase();
                    keyLocalPortal = ontologyNameHashMapAgroInverse.get(key);
                    if (keyLocalPortal != null) {
                        //System.out.println("Ontology: " + currentOntologyName + " Target key: " + key + " TARGET URI: " + keyLocalPortal);
                        ce.setBaseClassURI(key);
                        ce.setCuratedTarget(keyLocalPortal);
                        ce.setCuredtedBy("OMHT");
                        ce.setComments("Founded on Agroportal or Bioportal");
                        ce.setDate(Util.getDateTime());
                        ce.setStatus(1);
                        externalTargetReferenceHashMap.put(key, ce);
                    } else {

                        //LOOKUP ON OBO FOUNDRY
                        key = key.toLowerCase();
                        //System.out.print("Key: "+key);
                        keyLocalPortal = oboOntologies.get(key);
                        //System.out.println(" Value: "+keyLocalPortal);
                        if (keyLocalPortal != null) {
                            ce.setBaseClassURI(keyLocalPortal);
                            ce.setCuratedTarget("ext:" + keyLocalPortal);
                            ce.setCuredtedBy("OMHT");
                            ce.setComments("Founded on OBO Foundry");
                            ce.setDate(Util.getDateTime());
                            ce.setStatus(3);
                            externalTargetReferenceHashMap.put(key, ce);
                        } else {

                            // LOOKUP ON IDENTIFIERS.ORG
                            key = key.toLowerCase();
                            for (IdentifierEntity ie : identifiersList) {
                                keyLocalPortal = ie.findMatch(key);
                                if (keyLocalPortal != null) {
                                    break;
                                }
                            }
                            if (keyLocalPortal != null) {
                                ce.setBaseClassURI(keyLocalPortal);
                                ce.setCuratedTarget("ext:" + keyLocalPortal);
                                ce.setCuredtedBy("OMHT");
                                ce.setComments("Founded on identifiers.org");
                                ce.setDate(Util.getDateTime());
                                ce.setStatus(4);
                                externalTargetReferenceHashMap.put(key, ce);
                            }

                        }
                    }


                }
                result = null;

            }
        }




        //System.out.println("Tamanho do mapa antes do SORT: "+phase1TargetHashMap.size());

        int counter = 1;

        stdoutLogger.info("Generating phase1 Targets: "+Util.getDateTime());
        stdoutLogger.info("Sorting Targets by number of matches: "+Util.getDateTime());
        externalTargetReferenceHashMap = SortMapByValue.sortByValues(externalTargetReferenceHashMap,SortMapByValue.DESC);

        //System.out.println("Tamanho do mapa DEPOIS do SORT: "+phase1TargetHashMap.size());

        stdoutLogger.info("Finished Sorting Targets by number of matches: "+Util.getDateTime());
        phase1Logger.info("NUMBER;TARGET;FOUNDE IN;EXAMPLES;TOTAL COUNT;CURATED TARGET;BASE CLASS URI;CURATED BY;DATE;COMMENTS;STATUS");

        for (Map.Entry<String, CurationEntity> entry2 : externalTargetReferenceHashMap.entrySet()) {
            String key2 = entry2.getKey();
            ce = entry2.getValue();

            phase1Logger.info(""+(counter++)+";"+ce.getTargetFounded()+";"+ce.getFoundedIn()+";"+ce.getExampleList()+";"+ce.getCounter()+";"+ce.getCuratedTarget()+";"+ce.getBaseClassURI()+";"+ce.getCuredtedBy()+";"+ce.getDate()+";"+ce.getComments()+";"+ce.getStatus()+";"+ce.getMappingProperty());


        }
        stdoutLogger.info("Finished generating phase1 Targets: "+Util.getDateTime());

    }


    /**
     * Download list of files
     *
     * @param command
     * @param files
     */
    @Override
    public void parse(String command, ArrayList<String> files) {


        String executionHistory = readExecutionHistory();
        this.command = command;
        this.files = files;

        if (command.indexOf("b") > -1) {
            this.files = getFilesFromFolder(files.get(0));
        }
        totalizationLogger.info(Util.getDateTime() + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        totalizationLogger.info("DATE;SOURCE;SOURCE_ID;RELATION;TARGET;MATCHS_COUNT;");
        summaryLogger.info(Util.getDateTime() + ";-----------------------------");
        summaryLogger.info("Ontology;Total Matches");
        System.out.println("Total number of files of ontologies founded: " + files.size());

        for (String fileName : this.files) {
            man = OWLManager.createOWLOntologyManager();
            OWLOntology oA = null;
            currentOntologyName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.lastIndexOf(".")).toUpperCase();

            // verify execution history to dice if maus be processes or not
            if (executionHistory.indexOf(currentOntologyName.toUpperCase()) == -1) {

                externalLogger.info("******************************************External References for: " + currentOntologyName + " " + Util.getDateTime());

                setupLogProperties(this.command, currentOntologyName, fileName.substring(0, fileName.lastIndexOf(File.separator)));
                System.out.println(Util.getDateTime() + " Processing: " + (++countOntologies) + ") " + currentOntologyName);
                Submission submission = agroportalRestService.getLatestSubmission(command, currentOntologyName);
                if (submission != null) {
                    for (Contact contact : submission.getContact()) {
                        ontologyContactEmail += contact.getEmail() + ",";
                    }
                    ontologyContactEmail = ontologyContactEmail.substring(0, ontologyContactEmail.length() - 1);
                    currentOntologyId = submission.getId();
                } else {
                    ontologyContactEmail = "Ontology not on Agroportal or Bioportal";
                    currentOntologyId = "External Ontology";
                }
                loadOntology(fileName);
                findMatches();
                totalizeMappings();
                saveFile();
                if (command.indexOf("j") > -1) {
                    buildJson();
                }
                if (command.indexOf("s") > -1) {
                    generateStatistics();
                }

                mappings = new HashMap<>();
                maps = new HashMap<>();
                totalMappings = new HashMap<>();
                deduplicationHash = new HashMap<>();
                sb = new StringBuffer("");
                sts = new StringBuffer("");
                totalMappings.clear();
                ManageProperties.setProperty("executionhistory", executionHistory + currentOntologyName.toUpperCase() + ";");
            } else {
                errorLogger.warn("Ontology: " + currentOntologyName.toUpperCase() + " Already processes on previous history, process skiped.");
                stdoutLogger.warn("Ontology: " + currentOntologyName.toUpperCase() + " Already processes on previous history, process skiped.");
            }

        }
        if(externalTargetReferenceHashMap.size()>0){
            generatePhase1Targets();
        }
        System.out.println(Util.getDateTime() + " Finished processing.");
    }


    /**
     * Dowload ontologies from REST API
     *
     * @param command
     * @param dir
     */
    @Override
    public void parse(String command, String dir) {

        //System.out.println("External references size: "+externalReferenceHashMap.size());

        String executionHistory = readExecutionHistory();
        this.command = command;
        totalizationLogger.info(Util.getDateTime() + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        totalizationLogger.info("DATE;SOURCE;SOURCE_ID;RELATION;TARGET;MATCHS_COUNT;");
        summaryLogger.info(Util.getDateTime() + ";-----------------------------");
        summaryLogger.info("Ontology;Total Matches");

        System.out.println("Total of Ontologies founded: " + ontologies.size());


        // run thru ontologies collection
        for (OntologyEntity ontologyEntity : ontologies) {

            // verify execution history to dice if must be processes or not
            if (executionHistory.indexOf(ontologyEntity.getAcronym().toUpperCase()) == -1) {


                // verify if the ontology is summary only on the repository
                if (ontologyEntity.getSummaryOnly() == null || ontologyEntity.getSummaryOnly().toString().equalsIgnoreCase("false")) {

                    man = OWLManager.createOWLOntologyManager();
                    OWLOntology oA = null;

                    try {
                        currentOntologyName = ontologyEntity.getAcronym();
                        currentOntologyId = ontologyEntity.getId();
                        externalLogger.info("******************************************External References for: " + currentOntologyName + " " + Util.getDateTime());

                        System.out.println(Util.getDateTime() + " Processing: " + (++countOntologies) + ") " + currentOntologyName);
                        List<Contact> contacts = agroportalRestService.getLatestSubmission(command, currentOntologyName).getContact();
                        if (contacts != null) {
                            for (Contact contact : contacts) {
                                ontologyContactEmail += contact.getEmail() + ",";
                            }
                            if (ontologyContactEmail.length() > 0) {
                                ontologyContactEmail = ontologyContactEmail.substring(0, ontologyContactEmail.length() - 1);
                            }
                        } else {
                            ontologyContactEmail = "NO CONTACT INFO FOUNDED";
                            errorLogger.error("No contact info founded on ontology submission for " + currentOntologyName);
                        }

                        setupLogProperties(this.command, currentOntologyName, dir + File.separator);
                        downloadOntology(ontologyEntity.getAcronym(), ontologyEntity.getLinks().getDownload(), dir);
                        findMatches();
                        totalizeMappings();
                        saveFile();
                        if (command.indexOf("j") > -1) {
                            buildJson();
                        }
                        if (command.indexOf("s") > -1) {
                            generateStatistics();
                        }
                    } catch (OWLOntologyCreationException e) {
                        errorLogger.error("Error trying to download ontology from file" + e.getMessage());
                        saveFile();
                    }


                    mappings = new HashMap<>();
                    maps = new HashMap<>();
                    totalMappings = new HashMap<>();
                    deduplicationHash = new HashMap<>();
                    sb = new StringBuffer("");
                    sts = new StringBuffer("");
                    totalMappings.clear();

                } else {
                    errorLogger.error("Ontology: " + ontologyEntity.getAcronym() + " is summary only ! Processing skiped !");
                    externalLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " is summary only ! Processing skiped !");
                }
                appendExecutionHistory(ontologyEntity.getAcronym().toUpperCase());
            } else {
                errorLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " Already processes on previous history, process skiped.");
                stdoutLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " Already processes on previous history, process skiped.");
            }

        }
        if(externalTargetReferenceHashMap.size()>0){
            generatePhase1Targets();
        }
        System.out.println(Util.getDateTime() + " Finished processings.");
    }


    /**
     * Download individual ontology from REST API
     *
     * @param command
     * @param dir
     * @param ontology
     */
    @Override
    public void parse(String command, String dir, String ontology) {

        //System.out.println("External references size: "+externalReferenceHashMap.size());

        String executionHistory = readExecutionHistory();
        this.command = command;
        boolean founded = false;
        totalizationLogger.info(Util.getDateTime() + "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        totalizationLogger.info("DATE;SOURCE;SOURCE_ID;RELATION;TARGET;MATCHS_COUNT;");
        System.out.println("Total of Ontologies founded: " + ontologies.size());
        summaryLogger.info(Util.getDateTime() + ";-----------------------------");
        summaryLogger.info("Ontology;Total Matches");

        for (OntologyEntity ontologyEntity : ontologies) {


            man = OWLManager.createOWLOntologyManager();
            OWLOntology oA = null;

            // verify if this the desired ontology
            if (ontologyEntity.getAcronym().equalsIgnoreCase(ontology)) {

                // verify execution history to dice if maus be processes or not
                if (executionHistory.indexOf(ontologyEntity.getAcronym().toUpperCase()) == -1) {


                    if (ontologyEntity.getSummaryOnly() == null || ontologyEntity.getSummaryOnly().toString().equalsIgnoreCase("false")) {


                        founded = true;

                        externalLogger.info("******************************************External References for: " + ontology + " " + Util.getDateTime());


                        try {
                            currentOntologyName = ontologyEntity.getAcronym();
                            currentOntologyId = ontologyEntity.getId();
                            System.out.println(Util.getDateTime() + " Processing: " + currentOntologyName);
                            for (Contact contact : agroportalRestService.getLatestSubmission(command, currentOntologyName).getContact()) {
                                ontologyContactEmail += contact.getEmail() + ",";
                            }
                            ontologyContactEmail = ontologyContactEmail.substring(0, ontologyContactEmail.length() - 1);
                            setupLogProperties(this.command, currentOntologyName, dir + File.separator);
                            downloadOntology(ontologyEntity.getAcronym(), ontologyEntity.getLinks().getDownload(), dir);
                            findMatches();
                            totalizeMappings();

//                try {
//                    shouldUseReasoner();
//                    shouldCreateAndReadAnnotations();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                            saveFile();


                            if (command.indexOf("j") > -1) {
                                buildJson();
                            }
                            if (command.indexOf("s") > -1) {
                                generateStatistics();
                            }
                        } catch (OWLOntologyCreationException e) {
                            errorLogger.error("Error trying to download ontology from file" + e.getMessage());
                            saveFile();
                        }
                    } else {
                        errorLogger.error("Ontology: " + ontologyEntity.getAcronym() + " is summary only ! Processing skiped !");
                        externalLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " is summary only ! Processing skiped !");
                    }

                }else {
                    errorLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " Already processes on previous history, process skiped.");
                    stdoutLogger.warn("Ontology: " + ontologyEntity.getAcronym() + " Already processes on previous history, process skiped.");
                }

                mappings = new HashMap<>();
                maps = new HashMap<>();
                totalMappings = new HashMap<>();
                deduplicationHash = new HashMap<>();
                sb = new StringBuffer("");
                sts = new StringBuffer("");
                totalMappings.clear();

                appendExecutionHistory(ontologyEntity.getAcronym().toUpperCase());
            }


        }
        if(externalTargetReferenceHashMap.size()>0){
            generatePhase1Targets();
        }
        if (!founded) {
            errorLogger.error("Error: ontology not founded: " + ontology);
            System.out.println("Error: ontology not founded: " + ontology);
        }
        System.out.println(Util.getDateTime() + " Finished processing. ");
    }


}
