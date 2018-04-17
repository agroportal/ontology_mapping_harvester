package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.ClassQuery;
import fr.lirmm.agroportal.ontologymappingharvester.entities.classquery.Collection;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.ExternalReference;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Contact;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import fr.lirmm.agroportal.ontologymappingharvester.utils.Util;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import java.io.File;
import java.util.*;

public class HarvestAllFormatsService extends BaseService implements HarvestService {


    private String[] MATCH;
    private boolean isIRI;


    /**
     * Constructor to initialize search mappings to search for.
     */
    public HarvestAllFormatsService(){
        super();
        MATCH = new String[]{"http://www.w3.org/2002/07/owl#sameAs","http://www.w3.org/2000/01/rdf-schema#seeAlso","http://www.geneontology.org/formats/oboInOwl#hasDbXref","http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
        isIRI = false;        this.files = files;

    }

    /**
     * Load ontology from local folder
     * @param fileName
     */
    public void loadOntology(String fileName){

        try {

            //System.out.println("FileName: "+fileName);
            currentOntologyName = fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.lastIndexOf(".")).toUpperCase();

            fileIN = new File(fileName);

            oA = man.loadOntologyFromOntologyDocument(fileIN);
        } catch (OWLOntologyCreationException e) {
            errorLogger.error("Error trying to load ontology from file: "+e.getMessage());
        }


        stdoutLogger.info("ONTOLOGY (to.string()): " + oA.toString());

        ontologyID = oA.getOntologyID();

    }

    /**
     * Download ontology from Portal
     * @param acronym
     * @param address
     * @param dir
     * @throws OWLOntologyCreationException
     */
    public void downloadOntology(String acronym, String address, String dir) throws OWLOntologyCreationException{

        //System.out.println("Acro   : "+acronym);
        //System.out.println("Address: "+address);
        //System.out.println("Dir    : "+dir);

        fileIN = new File(dir + File.separator+ acronym.toUpperCase()+".xrdf");

        //System.out.println("FileIn    : "+fileIN);
        // Agroportal DEMO KEY

        String apiKey = ManageProperties.loadPropertyValue("apikey");

        IRI iri = IRI.create(address+"?apikey="+apiKey+"&download_format=rdf");

        try {
            oA = man.loadOntology(iri);

        }catch(UnloadableImportException e1){
            errorLogger.error("Error trying to import ontology: "+ e1.getMessage());
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
        String auxProperty="";


        for (OWLClass c : oA.getClassesInSignature()) {
            countClasses++;
        }
        stdoutLogger.info("Total of Classes: " + countClasses);

        stdoutLogger.info("Begin search for matchs on class anntations...");

        for (int x = 0; x < MATCH.length; x++) {

            stdoutLogger.info("======================= Searching matchs for: " + MATCH[x] + "=======================");

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

                            stdoutLogger.info("Ontology: "+ oA.getOntologyID());
                            stdoutLogger.info("Subject: " + annotationAssertionAxiom.getSubject());
                            stdoutLogger.info("Property: " + annotationAssertionAxiom.getProperty());

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


                            an = getAnnotationAssertationEntity(currentOntologyName,oA.getOntologyID().toString(),annotationAssertionAxiom.getSubject().toString(),annotationAssertionAxiom.getProperty().toString(),auxProperty,isIRI,++countMatch);
                            addToDeduplicationHash(an);

                            MapIRI = an.getOntology2();
                            if (mappings.containsKey(MapIRI)) {
                                counter = mappings.get(MapIRI);
                                counter++;
                                mappings.put(MapIRI, counter);
                                totalMappings.put(MapIRI, counter);
                            } else {
                                mappings.put(MapIRI, 1);
                                totalMappings.put(MapIRI, 1);
                            }
                            if (totalMappings.containsKey(MapIRI)) {
                                counter = totalMappings.get(MapIRI);
                                counter++;
                                totalMappings.put(MapIRI, counter);
//                                if(MapIRI==null || MapIRI.equalsIgnoreCase("null")){
//                                    System.out.println("Fase One");
//                                    System.out.println("Current    : "+currentOntologyName);
//                                    System.out.println("Aux        : "+aux);
//                                    System.out.println("Assertation: "+an.getAssertion());
//                                    System.out.println("Ontology1  : "+an.getOntology1());
//                                    System.out.println("Ontology2  : "+an.getOntology2());
//                                    System.out.println("Concept1   : "+an.getOntologyConcept1());
//                                    System.out.println("Concept2   : "+an.getOntologyConcept1());
//                                }
                            } else {
                                totalMappings.put(MapIRI, 1);
//                                if(MapIRI==null || MapIRI.equalsIgnoreCase("null")){
//                                    System.out.println("Fase One");
//                                    System.out.println("Current    : "+currentOntologyName);
//                                    System.out.println("Aux        : "+aux);
//                                    System.out.println("Assertation: "+an.getAssertion());
//                                    System.out.println("Ontology1  : "+an.getOntology1());
//                                    System.out.println("Ontology2  : "+an.getOntology2());
//                                    System.out.println("Concept1   : "+an.getOntologyConcept1());
//                                    System.out.println("Concept2   : "+an.getOntologyConcept1());
//                                }
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

                    //printAndAppend("*** INDIVIDUAL AXIOMS *** of Individual: "+ind.getIRI());
                    //printAndAppend("*** INDIVIDUAL AXIOMS :  "+ax);


                    for (OWLAnnotation owlAnnotation:ax.getAnnotations()) {

                        aux = ax.toString().toLowerCase().replace("\n","").trim();
                        //printAndAppend("-->"+aux+"<--");

                        assertationLookup = false;

                        if (owlAnnotation.toString().indexOf(MATCH[x]) > -1) {

                                //System.out.println("===IND1: "+ind.toString());

                                //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX --->");
                                an = getAnnotationAssertationEntity(owlAnnotation,ind.getIRI(),MATCH[x], ++countMatch);
                                //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX <---");

                                addToDeduplicationHash(an);
//                                if(an.getOntology2()==null){
//                                    printAndAppend("***XXX***");
//                                }

                                MapIRI = an.getOntology2();
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
//                                    if(MapIRI==null || MapIRI.equalsIgnoreCase("null")){
//                                        System.out.println("Fase Two");
//                                        System.out.println("Current    : "+currentOntologyName);
//                                        System.out.println("Aux        : "+aux);
//                                        System.out.println("Assertation: "+an.getAssertion());
//                                        System.out.println("Ontology1  : "+an.getOntology1());
//                                        System.out.println("Ontology2  : "+an.getOntology2());
//                                        System.out.println("Concept1   : "+an.getOntologyConcept1());
//                                        System.out.println("Concept2   : "+an.getOntologyConcept1());
//                                    }
                                } else {
                                    totalMappings.put(MapIRI, 1);
//                                    if(MapIRI==null || MapIRI.equalsIgnoreCase("null")){
//                                        System.out.println("Fase Two");
//                                        System.out.println("Current    : "+currentOntologyName);
//                                        System.out.println("Aux        : "+aux);
//                                        System.out.println("Assertation: "+an.getAssertion());
//                                        System.out.println("Ontology1  : "+an.getOntology1());
//                                        System.out.println("Ontology2  : "+an.getOntology2());
//                                        System.out.println("Concept1   : "+an.getOntologyConcept1());
//                                        System.out.println("Concept2   : "+an.getOntologyConcept1());
//                                    }
                                }

                        }
                    }

                    // special case for SKOS Named Individuals
                    if(assertationLookup){

                        if (ax.getAnnotationPropertiesInSignature().toString().indexOf(MATCH[x]) > -1) {

                            //printAndAppend("Assertation: " + ax.getAnnotationPropertiesInSignature());

                            //System.out.println("===IND2: "+ind.toString());
                            an = getAnnotationAssertationEntity(ax.toString(),ind.getIRI(),MATCH[x], ++countMatch);
                            addToDeduplicationHash(an);

                            MapIRI = an.getOntology2();
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




            if (mappings.size() > 0) {

                HashMap<String, Integer> mmp = SerializationUtils.clone(mappings);
                maps.put(MATCH[x], mmp);

            }
            mappings.clear();
            countIndividualsFlag=false;

        }


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

            for (Map.Entry<String, HashMap<String, Integer>> entry : maps.entrySet()) {
                String key = entry.getKey();
                HashMap<String, Integer> value = entry.getValue();

                stdoutLogger.info("Matches to: " + key);
                stdoutLogger.info("----------------------------------------------------------");

                for (Map.Entry<String, Integer> entry2 : value.entrySet()) {
                    String key2 = entry2.getKey();
                    Integer value2 = entry2.getValue();

                    stdoutLogger.info("Sub Total: " + key2 + " --> " + value2);
                    stdoutLogger.info("----------------------------------------------------------");
                    totalizationLogger.info(Util.getDateTime()+";"+currentOntologyName+";"+currentOntologyId+";"+key2+";"+value2+";");

                }


            }

            stdoutLogger.info("Total Matches for " + currentOntologyName);
            stdoutLogger.info("----------------------------------------------------------");

            for (Map.Entry<String, Integer> entry2 : totalMappings.entrySet()) {
                String key2 = entry2.getKey();
                Integer value2 = entry2.getValue();

                stdoutLogger.info("Total: " + key2 + " --> " + value2);
                stdoutLogger.info("----------------------------------------------------------");

            }

            stdoutLogger.info("Total Matches       : "+totalAnnotationAssertationEntities);
            stdoutLogger.info("Total Unique Matches: "+deduplicationHash.size());



        } else {
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
            current = (count*100/total);
            if(current%10==0){
                if(current != last) {
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
            me.setComment("Generated with the Ontology Mapping Harvest Tool - v.1.0 - Agroportal Project - LIRMM - "+Util.getFormatedDateTime("dd/MM/yyyy HH:mm")+" - FR");
            String[] mappings = new String[1];
            mappings[0] = an.getAssertion();
            me.setRelation(mappings);

            HashMap<String, String> classes = new HashMap<>();

            classes.put(an.getOntologyConcept1(), currentOntologyName);
            classes.put(an.getOntologyConcept2(),(command.indexOf("n")>-1?"ncbo:"+an.getOntology2():"agroportal:"+an.getOntology2()));

            me.setClasses(classes);

            mappingEntities.add(me);

        }

        if(mappingEntities.size()>0) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            writeJsonFile(gson.toJson(mappingEntities),false);
            stdoutLogger.info("Finished generation of temporary JSON file");
        }else{
            stdoutLogger.warn("No matches - JSON file generation skiped!");
        }

    }

    /**
     * Generate Statistics nodes for graph representation
     */
    public void generateStatistics(){

        String aux="";

        addStat("rootnode;"+currentOntologyName+";"+countIndividuals+";"+currentOntologyName);

        for (Map.Entry<String, Integer> entry2 : totalMappings.entrySet()) {
            String key2 = entry2.getKey();
            Integer value2 = entry2.getValue();

            if(key2.indexOf("http")==0){
                //key2 = key2.substring(key2.lastIndexOf(File.separator)+1,key2.length()).toUpperCase();
                key2 = mapExternalLink(key2);
            }


            addStat("node;"+key2+";"+value2+";"+key2);
            addStat("edge;"+currentOntologyName+";"+key2+";"+value2+";"+value2+" matches from "+currentOntologyName+" to "+key2);
        }
        if(sts.toString().length()>0){
            stdoutLogger.info("Statistic file was generated");
            writeStatFile();
        }else{
            stdoutLogger.warn("No matches, Statistic file generation skiped!");
        }


    }


    /**
     * Read ontology files from folder informed on runtime command line
     * @param dirName
     * @return
     */
    private ArrayList<String> getFilesFromFolder(String dirName){

        File dir = new File(dirName);
        String[] extensions = new String[] { "xrdf","owl","rdf" };
        List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
        ArrayList<String> lista = new ArrayList<>();
        for (File file:files) {
            //System.out.println("File: "+file);
            lista.add(file.toString());
        }

        return lista;
    }


    /**
     * Create AnnotationAssertationEntity for classes Variation I
     * @param anot
     * @param iri
     * @param assertion
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(OWLAnnotation anot, IRI iri,String assertion , int id) {


//        System.out.println("---Anot     : "+anot);
//        System.out.println("IRI         : "+iri);
//        System.out.println("Assertation : "+assertion);
//        System.out.println("ID          : "+id);


        String aux = anot.toString().replace("\n","").trim();
        String aux2="";
        String ret="";

        errorLogger.error("ERROR: ORIGIN -->"+aux);

        // System.out.println(aux);
        int indexOf1 = 0;
        int indexOf2 = 0;
        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);
        an.setAssertion(assertion);
        an.setOntology1(currentOntologyName);
        an.setOntologyConcept1(iri.toString());



        indexOf1 = aux.indexOf("\"");
        indexOf2 = aux.lastIndexOf("\"");

        if(indexOf1<indexOf2){

            aux2 = aux.substring(indexOf1+1,indexOf2);
            //an.setAssertion(aux.substring(indexOf1+1,indexOf2-1));
            //System.out.println("ENTROUAQUI-->"+aux.substring(indexOf1+1,indexOf2-1));
            ret = preClean(aux2);
            an.setOntology2(ret);

            if(ret.equals("UNMAPPED_REFERENCE") ){
                if (aux2.indexOf(":") > 1  && aux.indexOf("http")!=0 && aux.indexOf("ftp")!=0 && aux.indexOf("smtp")!=0) {
                    an.setOntology2(aux2.substring(0, aux2.indexOf(":")).toUpperCase());
                } else if (aux2.indexOf("_") > 1) {
                    an.setOntology2(aux2.substring(0, aux2.indexOf("_")).toUpperCase());
                } else if (aux2.indexOf("-") > 1) {
                    an.setOntology2(aux2.substring(0, aux2.indexOf("-")).toUpperCase());
                }else if(aux.indexOf("http")>0 || aux.indexOf("ftp")>0 || aux.indexOf("smtp")>0){
                    an.setOntology2("UNMAPPED_REFERENCE");
                }
            }


            an.setOntologyConcept2(aux2);


            if(an.getOntology2()==null || an.getOntology2().equalsIgnoreCase("null")){
                System.out.println("Current    : "+currentOntologyName);
                System.out.println("Aux        : "+aux);
                System.out.println("Assertation: "+an.getAssertion());
                System.out.println("Ontology1  : "+an.getOntology1());
                System.out.println("Ontology2  : "+an.getOntology2());
                System.out.println("Concept1   : "+an.getOntologyConcept1());
                System.out.println("Concept2   : "+an.getOntologyConcept2());
            }

        }else{
            // in case of a null property value
            an.setOntologyConcept2("UNMAPPED_REFERENCE");
            an.setOntology2("UNMAPPED_REFERENCE");

        }


        return an;
    }


    /**
     * Create AnnotationAssertationEntity for Classes Variation II
     * @param currentOntologyName
     * @param ontologyName
     * @param subject
     * @param property
     * @param propertyValue
     * @param isIRI
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(String currentOntologyName,String ontologyName, String subject, String property, String propertyValue, boolean isIRI, int id ){

        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);

        String name=currentOntologyName;
        String aux ="";
        String ret="";

        an.setOntology1(name);
        an.setOntologyConcept1(subject.replace("\n","").trim());
        an.setOntologyConcept2(propertyValue.replace("\n","").trim());
        an.setAssertion(property.replace("<","").replace(">","").replace("\n","").trim());




        if(isIRI){

            // Enter here if it is not an IRI
            aux = propertyValue.toLowerCase().replace("\n","").trim();
            ret = mapExternalLink(aux);
            an.setOntology2(ret);
            if(ret.equals("UNMAPPED_REFERENCE") ){
                if (aux.indexOf(":") > 1  && aux.indexOf("http")!=0 && aux.indexOf("ftp")!=0 && aux.indexOf("smtp")!=0) {
                    an.setOntology2(aux.substring(0, aux.indexOf(":")).toUpperCase());
                } else if (aux.indexOf("_") > 1) {
                    an.setOntology2(aux.substring(0, aux.indexOf("_")).toUpperCase());
                } else if (aux.indexOf("-") > 1) {
                    an.setOntology2(aux.substring(0, aux.indexOf("-")).toUpperCase());
                }
            }

        }else {

            // Enter here if it is not an IRI
            aux = propertyValue.toLowerCase().replace("\n","").trim();

                ret = mapExternalLink(aux);
                an.setOntology2(ret);
                if(ret.equals("UNMAPPED_REFERENCE")){
                    if (aux.indexOf(":") > 1 && aux.indexOf("http")!=0 && aux.indexOf("ftp")!=0 && aux.indexOf("smtp")!=0 ) {
                        an.setOntology2(aux.substring(0, aux.indexOf(":")).toUpperCase());
                    } else if (aux.indexOf("_") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("_")).toUpperCase());
                    } else if (aux.indexOf("-") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("-")).toUpperCase());
                    }
                }


        }


        return an;

    }


    /**
     * Create AnnotationSsertationEntity from class individuals
     * @param anot
     * @param iri
     * @param assertion
     * @param id
     * @return
     */
    public AnnotationAssertationEntity getAnnotationAssertationEntity(String anot, IRI iri,String assertion , int id) {

        String aux = anot.toString().replace("\n","").trim();
        String aux2="";


        // System.out.println(aux);
        int indexOf1 = 0;
        int indexOf2 = 0;
        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);
        an.setAssertion(assertion);
        an.setOntology1(currentOntologyName);
        an.setOntologyConcept1(iri.toString());




        if(aux.length()>6) {

            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            an.setAssertion(aux.substring(indexOf1, indexOf2+1).replace("<","").replace(">",""));
            aux = aux.substring(indexOf2+1);

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");

            while(indexOf2<=indexOf1){
                aux = aux.substring(indexOf2+1,aux.length());
                System.out.println(aux);
                indexOf1 = aux.indexOf("<");
                indexOf2 = aux.indexOf(">");
            }

            an.setOntologyConcept1(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));

            an.setOntology1(an.getOntologyConcept1().substring(0,an.getOntologyConcept1().lastIndexOf("/")));

            aux = aux.substring(indexOf2+1).replace(" ","").replace("\"","");

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
//
//            if(indexOf2<=indexOf1){
//                System.out.println("Current    : "+currentOntologyName);
//                System.out.println("Aux        : "+aux);
//                System.out.println("Assertation: "+an.getAssertion());
//                System.out.println("Ontology1  : "+an.getOntology1());
//                System.out.println("Ontology2  : "+an.getOntology2());
//                System.out.println("Concept1   : "+an.getOntologyConcept1());
//                System.out.println("Concept2   : "+an.getOntologyConcept1());
//            }


            if(indexOf1>-1 && indexOf2>1) {
                an.setOntologyConcept2(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));


                aux2 = an.getOntologyConcept2();
                if(aux2.length()>0 && aux2.substring(aux2.length()-1,aux2.length()).equalsIgnoreCase("/")){
                    an.setOntologyConcept2(aux2.substring(0,aux2.length()-1));
                }

                an.setOntology2(an.getOntologyConcept2().substring(0, an.getOntologyConcept2().lastIndexOf("/")));
            }else{


                    if (aux.indexOf(":") > 1 && aux.indexOf("http")!=0 && aux.indexOf("ftp")!=0 && aux.indexOf("smtp")!=0 ) {
                        an.setOntology2(aux.substring(0, aux.indexOf(":")).toUpperCase());
                    } else if (aux.indexOf("_") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("_")).toUpperCase());
                    } else if (aux.indexOf("-") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("-")).toUpperCase());
                    } else{
                        an.setOntology2(mapExternalLink(aux));
                    }

                an.setOntologyConcept2(aux);
            }

        }

        return an;
    }


    /**
     * Map external references for LINKS that are not IRI
     * @param searchString
     * @return
     */
    public String mapExternalLink(String searchString){


        ExternalReference value;
        String key;
        String result="";
        for (Map.Entry<String, ExternalReference> entry : externalReferenceHashMap.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if(searchString.indexOf(key)>-1){
                result = value.getLink();
//                if(result==null){
//                    System.out.println("null-->"+key);
//                }

                break;
            }
        }
        if(result.length()>0){
            return result.toLowerCase();
        }else{
            //System.out.println("SearchString: "+searchString);
            if(searchString.indexOf("http")>-1){
                stdoutLogger.error("ERROR: External reference not mapped -->"+searchString);
                stdoutLogger.info("{\"search_string\":\""+searchString.replace("http://www.","").replace("https://www.","").replace("http://","").replace("https://","")+"\",\"link\":\""+searchString+"\",\"iri\":\"\"},");
                externalLogger.info("{\"search_string\":\""+searchString.replace("http://www.","").replace("https://www.","").replace("http://","").replace("https://","")+"\",\"link\":\""+searchString+"\",\"iri\":\"\"},");
            }else{
                stdoutLogger.error("ERROR: NOT A LINK -->"+searchString);
            }

            return "UNMAPPED_REFERENCE";
        }

    }


    private String preClean(String searchString){

        String aux=searchString;
        int index1 = searchString.indexOf(":");
        int index2 = searchString.lastIndexOf(":");
        if(index2>index1){
            aux = searchString.substring(index1+1,searchString.length());
            //System.out.println("*************************************--->>>"+aux);

        }

        return mapExternalLink(aux);

    }


    /**
     * Download list of files
     * @param command
     * @param files
     */
    @Override
    public void parse(String command, ArrayList<String> files) {



        this.command = command;

        this.files = files;


        if(command.indexOf("b")>-1){
            this.files = getFilesFromFolder(files.get(0));
        }

        System.out.println("Total number of files of ontologies founded: "+files.size());

        for (String fileName:this.files) {
            man = OWLManager.createOWLOntologyManager();
            OWLOntology oA = null;
            currentOntologyName = fileName.substring(fileName.lastIndexOf(File.separator)+1,fileName.lastIndexOf(".")).toUpperCase();

            externalLogger.info("******************************************External References for: "+currentOntologyName+" "+Util.getDateTime());

            setupLogProperties(this.command,currentOntologyName, fileName.substring(0,fileName.lastIndexOf(File.separator)));
            System.out.println(Util.getDateTime()+" Processing: "+(++countOntologies)+") "+currentOntologyName);
            Submission submission = agroportalRestService.getLatestSubmission(command,currentOntologyName);
            for(Contact contact: submission.getContact()){
                ontologyContactEmail += contact.getEmail()+",";
            }
            ontologyContactEmail=ontologyContactEmail.substring(0,ontologyContactEmail.length()-1);
            currentOntologyId = submission.getId();
            loadOntology(fileName);
            findMatches();
            saveFile();
            if(command.indexOf("j")>-1){
                buildJson();
            }
            if(command.indexOf("s")>-1){
                generateStatistics();
            }

            mappings = new HashMap<>();
            maps = new HashMap<>();
            totalMappings = new HashMap<>();
            deduplicationHash = new HashMap<>();
            sb = new StringBuffer("");
            sts = new StringBuffer("");
            totalMappings.clear();




        }
        System.out.println(Util.getDateTime()+" Finished processing.");
    }


    /**
     * Dowload ontologies from REST API
     * @param command
     * @param dir
     */
    @Override
    public void parse(String command, String dir) {

        //System.out.println("External references size: "+externalReferenceHashMap.size());


        this.command = command;

        System.out.println("Total of Ontologies founded: "+ontologies.size());

        for (OntologyEntity ontologyEntity: ontologies) {

            man = OWLManager.createOWLOntologyManager();
            OWLOntology oA = null;

            try {
                currentOntologyName = ontologyEntity.getAcronym();
                currentOntologyId = ontologyEntity.getId();
                externalLogger.info("******************************************External References for: "+currentOntologyName+" "+Util.getDateTime());

                System.out.println(Util.getDateTime()+" Processing: "+(++countOntologies)+") "+currentOntologyName);
                for(Contact contact: agroportalRestService.getLatestSubmission(command,currentOntologyName).getContact()){
                    ontologyContactEmail += contact.getEmail()+",";
                }
                ontologyContactEmail=ontologyContactEmail.substring(0,ontologyContactEmail.length()-1);
                setupLogProperties(this.command,currentOntologyName, dir+File.separator);
                downloadOntology(ontologyEntity.getAcronym(), ontologyEntity.getLinks().getDownload(), dir);
                findMatches();
                saveFile();
                if(command.indexOf("j")>-1){
                    buildJson();
                }
                if(command.indexOf("s")>-1){
                    generateStatistics();
                }
            }catch(OWLOntologyCreationException e){
                errorLogger.error("Error trying to download ontology from file"+e.getMessage());
                saveFile();
            }


            mappings = new HashMap<>();
            maps = new HashMap<>();
            totalMappings = new HashMap<>();
            deduplicationHash = new HashMap<>();
            sb = new StringBuffer("");
            sts = new StringBuffer("");
            totalMappings.clear();




        }
        System.out.println(Util.getDateTime()+" Finished processings.");
    }


    /**
     * Download individual ontology from REST API
     * @param command
     * @param dir
     * @param ontology
     */
    @Override
    public void parse(String command, String dir, String ontology) {

        //System.out.println("External references size: "+externalReferenceHashMap.size());


        this.command = command;
        boolean founded = false;

        System.out.println("Total of Ontologies founded: "+ontologies.size());

        for (OntologyEntity ontologyEntity: ontologies) {


            man = OWLManager.createOWLOntologyManager();
            OWLOntology oA = null;

            if(ontologyEntity.getAcronym().equalsIgnoreCase(ontology)) {

                founded = true;

                externalLogger.info("******************************************External References for: "+ontology+" "+Util.getDateTime());


                try {
                    currentOntologyName = ontologyEntity.getAcronym();
                    currentOntologyId = ontologyEntity.getId();
                    System.out.println(Util.getDateTime()+" Processing: "+currentOntologyName);
                    for(Contact contact: agroportalRestService.getLatestSubmission(command,currentOntologyName).getContact()){
                        ontologyContactEmail += contact.getEmail()+",";
                    }
                    ontologyContactEmail=ontologyContactEmail.substring(0,ontologyContactEmail.length()-1);
                    setupLogProperties(this.command,currentOntologyName, dir+File.separator);
                    downloadOntology(ontologyEntity.getAcronym(), ontologyEntity.getLinks().getDownload(), dir);
                    findMatches();

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
                }catch (OWLOntologyCreationException e){
                    errorLogger.error("Error trying to download ontology from file"+e.getMessage());
                    saveFile();
                }

            }

            mappings = new HashMap<>();
            maps = new HashMap<>();
            totalMappings = new HashMap<>();
            deduplicationHash = new HashMap<>();
            sb = new StringBuffer("");
            sts = new StringBuffer("");
            totalMappings.clear();




        }
        if(!founded){
            errorLogger.error("Error: ontology not founded: "+ontology);
            System.out.println("Error: ontology not founded: "+ontology);
        }
        System.out.println(Util.getDateTime()+" Finished processing. ");
    }


}
