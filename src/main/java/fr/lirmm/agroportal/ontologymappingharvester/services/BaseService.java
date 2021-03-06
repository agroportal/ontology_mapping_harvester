package fr.lirmm.agroportal.ontologymappingharvester.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.lirmm.agroportal.ontologymappingharvester.entities.reference.CurationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.TargetReference;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.Identifier;
import fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers.IdentifierEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.mappings.MappingEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry.OBOOntologies;
import fr.lirmm.agroportal.ontologymappingharvester.entities.obofoundry.Ontology;
import fr.lirmm.agroportal.ontologymappingharvester.entities.ontology.OntologyEntity;
import fr.lirmm.agroportal.ontologymappingharvester.entities.submission.Submission;
import fr.lirmm.agroportal.ontologymappingharvester.network.AgroportalRestService;
import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
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
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BaseService extends LogService {


    OWLOntologyManager man;
    OWLOntology oA;

    int countClasses;
    int countIndividuals;
    int countMatch;
    int countTotalMatch;
    int totalAnnotationAssertationEntities;
    int countOntologies;
    int targetRegisterCounter;
    String aux;
    AnnotationAssertationEntity an;
    HashMap<String,AnnotationAssertationEntity> deduplicationHash;
    HashMap<String,Integer> maps;
    HashMap<String,Integer> mappings;
    HashMap<String,Integer> totalMappings;
    HashMap<String, CurationEntity> externalTargetReferenceHashMap;
    HashMap<String, CurationEntity> externalTargetReferenceHashMapOut;
    HashMap<String,String> ontologyNameHashMapAgro;
    HashMap<String,String> ontologyNameHashMapAgroInverse;
    HashMap<String,String> ontologyNameHashMapBio;
    HashMap<String,String> ontologyNameHashMapBioInverse;
    HashMap<String, String> oboOntologies;
    List<OntologyEntity> ontologies;
    AgroportalRestService agroportalRestService;
    CurationEntity curationEntity;
    List<IdentifierEntity> identifiersList;

    int counter;
    String MapIRI;
    File fileIN;
    File fileOUT;
    StringBuffer sb;
    StringBuffer sts;
    OWLOntologyID ontologyID;
    String command;
    ArrayList<String> files;
    String currentOntologyName;
    StringBuffer unmap;
    String currentOntologyId;

    String ontologyContactEmail;
    public boolean printToConsole;


    /**
     * Constructor: initialize vars
     */
    public BaseService(){

        printToConsole=false;
        man = OWLManager.createOWLOntologyManager();
        OWLOntology oA = null;
        totalAnnotationAssertationEntities=0;
        countClasses=0;
        countIndividuals=0;
        countMatch=0;
        countTotalMatch=0;
        countOntologies=0;
        aux="";
        an = null;
        mappings = new HashMap<>();
        maps = new HashMap<>();
        totalMappings = new HashMap<>();
        deduplicationHash = new HashMap<>();
        ontologyNameHashMapAgro = new HashMap<>();
        ontologyNameHashMapBio = new HashMap<>();
        ontologyNameHashMapAgroInverse = new HashMap<>();
        ontologyNameHashMapBioInverse = new HashMap<>();
        oboOntologies = new HashMap<>();
        counter = 0;
        MapIRI="";
        sb = new StringBuffer("");
        sts = new StringBuffer("");
        unmap = new StringBuffer("");
        command="";
        files = new ArrayList<>();
        currentOntologyName="";
        externalTargetReferenceHashMap = new HashMap<>();
        externalTargetReferenceHashMapOut = new HashMap<>();
        ontologyContactEmail = "";
        agroportalRestService = new AgroportalRestService();
        currentOntologyId="";
        targetRegisterCounter=0;
        identifiersList = new ArrayList<>();
    }


    /**
     * Load ontology from file
     * @return
     */
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
//            println(oA);
//            oA.logicalAxioms().forEach(System.out::println);


            //OWLDocumentFormat format = oA.getNonnullFormat();

            //println("ONTOLOGY FORMAT:" + format.getKey());


            stdoutLogger.info("ONTOLOGY:" + oA.toString());

            ontologyID = oA.getOntologyID();

//            if (ontologyID != null) {
//                println("ID :" + ontologyID.toString());
//                //Optional<IRI> ontologyIRI = ontologyID.getOntologyIRI();
//                if (ontologyIRI.isPresent()) {
//                    println("ONTOLOGYIRI :" + ontologyIRI.toString());
//                } else {
//                    println("Sem IRI");
//                }
//            } else {
//                println("Sem ID");
//            }
        }else{
            println("No file selected !!!");
        }
        return true;

    }


    /**
     * Write JSON files for Mappings
     * @param jsonString
     */
    public void writeJsonFile(String jsonString, boolean definitive){


        if(command.indexOf("j")>-1) {

            String path = fileIN.getAbsolutePath();
            String extension=".json";
            Path p = Paths.get(path);
            String fileName = p.getFileName().toString().substring(0,p.getFileName().toString().indexOf("."));
            String directory = p.getParent().toString();

            File fileToDelete = new File(directory + File.separator + fileName + extension);

            if(definitive){
                extension=".json";
            }

            File f = new File(directory + File.separator + fileName + extension);
            //println("file   ->"+f);
            stdoutLogger.info("Writing JSON file: " + f.getAbsolutePath());

            try {
                FileUtils.writeStringToFile(f, jsonString, "UTF-8");
                if(definitive){
                    fileToDelete.delete();
                }
            } catch (IOException e) {
                errorLogger.error("Error trying to write JSON file: " + e.getMessage());
            }
        }

    }

    public void writeStatFile(){


        if(command.indexOf("s")>-1) {

            String path = fileIN.getAbsolutePath();

            Path p = Paths.get(path);
            String fileName = p.getFileName().toString();
            String directory = p.getParent().toString();

            File f = new File(directory + File.separator + (fileName.replace(".rdf", "").replace(".xrdf", "")) + ".sts");
            stdoutLogger.info("Writing Statistics file: " + f.getAbsolutePath());

            try {
                FileUtils.writeStringToFile(f, sts.toString(), "UTF-8");
            } catch (IOException e) {
                errorLogger.error("Error trying to write STATISTICS file: " + e.getMessage());
            }
        }

    }


    public void addStat(String text){
            sts.append(text+"\n");
    }


    public void addToDeduplicationHash(AnnotationAssertationEntity an, int variation){


        AnnotationAssertationEntity aaa = deduplicationHash.get(an.getOntology1()+an.getOntologyConcept1()+an.getAssertion()+an.getOntologyConcept2());
        if(aaa!=null){
            externalLogger.info("Duplicated: "+aaa.getOntology2()+" - "+aaa.getId()+" - "+aaa.getOntologyConcept1()+" "+aaa.getAssertion()+" "+aaa.getOntologyConcept2()+" with: "+an.getId()+" - "+an.getOntologyConcept1()+" "+an.getAssertion()+" "+an.getOntologyConcept2());
        }

        deduplicationHash.put(an.getOntology1()+an.getOntologyConcept1()+an.getAssertion()+an.getOntologyConcept2(),an);
        //stdoutLogger.trace("variation "+variation+" "+an.getOntologyConcept1()+an.getAssertion()+an.getOntologyConcept2());
        if(an.getId()>totalAnnotationAssertationEntities){
            totalAnnotationAssertationEntities=an.getId();
        }
    }


    public void shouldUseReasoner() throws Exception {
        // Create our ontology manager in the usual way.
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();


        //df = manager.getOWLDataFactory();
        // We need to create an instance of OWLReasoner. An OWLReasoner provides
        // the basic query functionality that we need, for example the ability
        // obtain the subclasses of a class etc. To do this we use a reasoner
        // factory. Create a reasoner factory. In this case, we will use HermiT,
        // but we could also use FaCT++ (http://code.google.com/p/factplusplus/)
        // or Pellet(http://clarkparsia.com/pellet) Note that (as of 03 Feb
        // 2010) FaCT++ and Pellet OWL API 3.0.0 compatible libraries are
        // expected to be available in the near future). For now, we'll use
        // HermiT HermiT can be downloaded from http://hermit-reasoner.com Make
        // sure you get the HermiT library and add it to your class path. You
        // can then instantiate the HermiT reasoner factory: Comment out the
        // first line below and uncomment the second line below to instantiate
        // the HermiT reasoner factory. You'll also need to import the
        // org.semanticweb.HermiT.Reasoner package.
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        // OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
        // We'll now create an instance of an OWLReasoner (the implementation
        // being provided by HermiT as we're using the HermiT reasoner factory).
        // The are two categories of reasoner, Buffering and NonBuffering. In
        // our case, we'll create the buffering reasoner, which is the default
        // kind of reasoner. We'll also attach a progress monitor to the
        // reasoner. To do this we set up a configuration that knows about a
        // progress monitor. Create a console progress monitor. This will print
        // the reasoner progress out to the console.
        // ConsoleProgressMonitor progressMonitor = new
        // ConsoleProgressMonitor();
        // Specify the progress monitor via a configuration. We could also
        // specify other setup parameters in the configuration, and different
        // reasoners may accept their own defined parameters this way.
        // OWLReasonerConfiguration config = new SimpleConfiguration(
        // progressMonitor);
        // Create a reasoner that will reason over our ontology and its imports
        // closure. Pass in the configuration.
        // OWLReasoner reasoner = reasonerFactory.createReasoner(ont, config);
        OWLReasoner reasoner = reasonerFactory.createReasoner(oA);
        // Ask the reasoner to do all the necessary work now
        reasoner.precomputeInferences();
        // We can determine if the ontology is actually consistent (in this
        // case, it should be).
        boolean consistent = reasoner.isConsistent();
        stdoutLogger.info("Consistent: " + consistent);
        // We can easily get a list of unsatisfiable classes. (A class is
        // unsatisfiable if it can't possibly have any instances). Note that the
        // getUnsatisfiableClasses method is really just a convenience method
        // for obtaining the classes that are equivalent to owl:Nothing.
        Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
        // This node contains owl:Nothing and all the classes that are
        // equivalent to owl:Nothing - i.e. the unsatisfiable classes. We just
        // want to print out the unsatisfiable classes excluding owl:Nothing,
        // and we can used a convenience method on the node to get these
        Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
        if (!unsatisfiable.isEmpty()) {
            stdoutLogger.info("The following classes are unsatisfiable: ");
            for (OWLClass cls : unsatisfiable) {
                stdoutLogger.info(" " + cls);
            }
        } else {
            stdoutLogger.info("There are no unsatisfiable classes");
        }
        // Now we want to query the reasoner for all descendants of Marsupial.
        // Vegetarians are defined in the ontology to be animals that don't eat
        // animals or parts of animals.
        OWLDataFactory fac = manager.getOWLDataFactory();
        // Get a reference to the vegetarian class so that we can as the
        // reasoner about it. The full IRI of this class happens to be:
        // <http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#Marsupials>
        OWLClass marsupials = fac.getOWLClass(IRI.create(
                "http://protege.stanford.edu/plugins/owl/owl-library/koala.owl#Marsupials"));
        // Now use the reasoner to obtain the subclasses of Marsupials. We can
        // ask for the direct subclasses or all of the (proper)
        // subclasses. In this case we just want the direct ones
        // (which we specify by the "true" flag).
        NodeSet<OWLClass> subClses = reasoner.getSubClasses(marsupials, true);
        // The reasoner returns a NodeSet, which represents a set of Nodes. Each
        // node in the set represents a subclass of Marsupial. A node of
        // classes contains classes, where each class in the node is equivalent.
        // For example, if we asked for the subclasses of some class A and got
        // back a NodeSet containing two nodes {B, C} and {D}, then A would have
        // two proper subclasses. One of these subclasses would be equivalent to
        // the class D, and the other would be the class that is equivalent to
        // class B and class C. In this case, we don't particularly care about
        // the equivalences, so we will flatten this set of sets and print the
        // result
        Set<OWLClass> clses = subClses.getFlattened();
        for (OWLClass cls : clses) {
            stdoutLogger.info(" " + cls);
        }
        // We can easily
        // retrieve the instances of a class. In this example we'll obtain the
        // instances of the class Marsupials.
        NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(marsupials, false);
        // The reasoner returns a NodeSet again. This time the NodeSet contains
        // individuals. Again, we just want the individuals, so get a flattened
        // set.
        Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        for (OWLNamedIndividual ind : individuals) {
            stdoutLogger.info(" " + ind);
        }
        // Again, it's worth noting that not all of the individuals that are
        // returned were explicitly stated to be marsupials. Finally, we can ask
        // for the property values (property assertions in OWL speak) for a
        // given
        // individual and property.
        // Let's get all properties for all individuals
        for (OWLNamedIndividual i : oA.getIndividualsInSignature()) {
            for (OWLObjectProperty p : oA.getObjectPropertiesInSignature()) {
                NodeSet<OWLNamedIndividual> individualValues = reasoner.getObjectPropertyValues(i, p);
                stdoutLogger.info("Vazio: "+individualValues.isEmpty());
                Set<OWLNamedIndividual> values = individualValues.getFlattened();
                stdoutLogger.info("The property values for "+p+" for individual "+i+" are: ");
                for (OWLNamedIndividual ind : values) {
                    stdoutLogger.info(" " + ind);
                }
            }
        }
        // Finally, let's print out the class hierarchy.
        Node<OWLClass> topNode = reasoner.getTopClassNode();
        print(topNode, reasoner, 0);


    }

    private static void print(@Nonnull Node<OWLClass> parent, @Nonnull OWLReasoner reasoner, int depth) {
        // We don't want to print out the bottom node (containing owl:Nothing
        // and unsatisfiable classes) because this would appear as a leaf node
        // everywhere
        if (parent.isBottomNode()) {
            return;
        }
        // Print an indent to denote parent-child relationships
        printIndent(depth);
        // Now print the node (containing the child classes)
        printNode(parent);
        for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
            assert child != null;
            // Recurse to do the children. Note that we don't have to worry
            // about cycles as there are non in the inferred class hierarchy
            // graph - a cycle gets collapsed into a single node since each
            // class in the cycle is equivalent.
            print(child, reasoner, depth + 1);
        }
    }

    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print(" ");
        }
    }

    private static void printNode(@Nonnull Node<OWLClass> node) {
        // The default prefix used here is only an example.
        // For real ontologies, choose a meaningful prefix - the best
        // choice depends on the actual ontology.
        DefaultPrefixManager pm = new DefaultPrefixManager(null, null, "http://owl.man.ac.uk/2005/07/sssw/people#");
        // Print out a node as a list of class names in curly brackets
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext();) {
            OWLClass cls = it.next();
            // User a prefix manager to provide a slightly nicer shorter name
            String shortForm = pm.getShortForm(cls);

            System.out.println("Short Name: "+shortForm);
            //assertNotNull(shortForm);
        }
    }


    public void shouldCreateAndReadAnnotations() throws Exception {




        for (OWLClass cls : oA.getClassesInSignature()) {
            // Get the annotations on the class that use the label property

            stdoutLogger.info("------------------------------------------------------------------------");
            stdoutLogger.info("Classes: "+cls.toString());
            stdoutLogger.info("------------------------------------------");

            for (OWLOntology o : oA.getImportsClosure()) {

                //println("AnnotationAssetationAxioms: "+o.getAnnotationAssertionAxioms(cls.getIRI()));

                //annotationObjects(o.getAnnotationAssertionAxioms(cls.getIRI()),label)

                for (OWLAnnotationAssertionAxiom annotationAssertionAxiom : o.getAnnotationAssertionAxioms(cls.getIRI())) {
                    //println("Entrou no if do assetation");

                    stdoutLogger.info("=======================");
                    stdoutLogger.info("AnnotationAssertationAxiom: "+annotationAssertionAxiom);
                    stdoutLogger.info("Subject: "+annotationAssertionAxiom.getSubject());
                    stdoutLogger.info("Property: "+annotationAssertionAxiom.getProperty());

                    stdoutLogger.info("Assertion with out anotation: "+annotationAssertionAxiom.getAnnotationPropertiesInSignature());


                    if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                        //println("Entrou no if do annotation get value");
                        OWLLiteral val = (OWLLiteral) annotationAssertionAxiom.getValue();
                        //if (val.hasLang("en")) {
                        stdoutLogger.info("PropertyValue: " +
                                val.getLiteral());
                        //}
                    }

                }
            }
            stdoutLogger.info("----------------------------------------------------------------------");
        }
    }



    /**
     * Load external references JSON file
     */
    public void loadExternalTargetReferences(){

        int status=0;
        String dir = ManageProperties.loadPropertyValue("externalproperties");

        //println(dir);
        //println(dir+File.separator+"OMHT_external_matches_phase_1.cfg");

        try(BufferedReader br = new BufferedReader(new FileReader(dir+File.separator+"OMHT_external_matches_phase_1.cfg"))) {

            //println("Entrou qui....");

            String line = br.readLine();
            String[] content = new String[12];
            CurationEntity er;
            // exclude hearder
            line = br.readLine();
            String property = "";

            while (line != null) {
                content = line.split(";");
                // this due the lack of value on the last column
                if(content.length<13){
                    property = "";
                }else{
                    property = content[12];
                }
                //println("-->"+line+"<--");
                er = new CurationEntity(content[0],content[1],content[2],content[3],0,content[5],content[6],content[7],content[8],content[9],content[10],Integer.parseInt(content[11]),property);

                //println("CURATED_TARGETS: "+content[0]+" - "+content[1]+" - "+content[2]+" - "+content[3]+" - "+"0"+" - "+content[5]+" - "+content[6]+" - "+content[7]+" - "+content[8]+" - "+content[9]+" - "+content[10]+" - "+content[11]+" - "+property);

                if(er.getStatus()>0){
                    er.setCounter(Integer.parseInt(content[4]));
                }
                // TODO ATTENTION TO THIS LOWERCASE
                externalTargetReferenceHashMap.put(content[1].toLowerCase(),er);
                line = br.readLine();
            }

        } catch (IOException e) {
            errorLogger.error("Error trying to load external target references txt file located in: "+dir+" message:"+e.getMessage());
        }
        println("External target curated references loaded: "+externalTargetReferenceHashMap.size()+" valid targets");
    }





    public void loadAndProcessOntologiesMetadata(String command){

        Submission submission = null;

        println("Load and process ontology metadata...");

        List<OntologyEntity> ontologiesAgro =  agroportalRestService.getOntologyAnnotation(command.indexOf("h")>-1?"h":"x");

        if(ontologiesAgro==null || ontologiesAgro.size()==0){
            errorLogger.error("Error: could not load ontologies metadata from Agroportal - Please verify API Key" );
            stdoutLogger.error("Error: could not load ontologies metadata from Agroportal - Please verify API Key");
            println("Error: could not load ontologies metadata from Agroportal - Please verify API Key");
            System.exit(0);
        }

        for(OntologyEntity oe: ontologiesAgro){
            submission = agroportalRestService.getLatestSubmission(command.indexOf("h")>-1?"h":"x",oe.getAcronym());

            //System.out.println(oe.getAcronym()+" ########################################################");
            //System.out.println(oe.getAcronym()+" --> "+submission.getURI());


            if(submission.getURI()!=null && submission.getURI().length()>1){
                if((submission.getURI().length()-1)==submission.getURI().lastIndexOf("/")){
                    submission.setURI(submission.getURI().substring(0,submission.getURI().length()-1));
                }
            }

            // get the number of classes to put on the visualization javscript latter
            if(submission.getNumberOfClasses()!=null && submission.getNumberOfClasses()>0){
                oe.setNumberOfClasses(submission.getNumberOfClasses());
            }else{
                // In case there is no information about the number of classes on the submission
                oe.setNumberOfClasses(1);
            }

            //TODO take out the prefix
            //ontologyNameHashMapAgroInverse.put(submission.getURI(),"AGROPORTAL:"+oe.getAcronym());

            ontologyNameHashMapAgroInverse.put(submission.getURI(),oe.getAcronym());

            ontologyNameHashMapAgro.put(oe.getAcronym(),submission.getURI());
            externalLogger.info("Get IRI for "+oe.getAcronym()+" --> "+submission.getURI());
        }


        List<OntologyEntity> ontologiesBio =  agroportalRestService.getOntologyAnnotation("n");

        if(ontologiesBio==null || ontologiesBio.size()==0){
            errorLogger.error("Error: could not load ontologies metadata from Bioportal - Please verify API Key - Current key: "+ManageProperties.loadPropertyValue("restagroportalapikey") );
            stdoutLogger.error("Error: could not load ontologies metadata from Bioportal - Please verify API Key");
            println("Error: could not load ontologies metadata from Bioportal - Please verify API Key");
            System.exit(0);
        }

        for(OntologyEntity oe: ontologiesBio){
            ontologyNameHashMapBioInverse.put(oe.getId(),"ncbo:"+oe.getAcronym());
            ontologyNameHashMapBio.put(oe.getAcronym(),oe.getId());
            externalLogger.info("Get IRI for "+oe.getAcronym()+" --> "+oe.getId());
        }


        if(command.indexOf("n")>-1 || command.indexOf("f")>-1){
            ontologies = ontologiesBio;
        }else{
            ontologies = ontologiesAgro;
        }

    }


    public MappingEntity[] loadTempJSONFile(File file){

        ArrayList<MappingEntity> mappingEntities = new ArrayList<>();
        MappingEntity[] mappingE = null;


        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String everything = sb.toString();
            Gson gson = new GsonBuilder().create();
            mappingE = gson.fromJson(everything, MappingEntity[].class);

            println("Tamanho: "+mappingE.length);


        } catch (IOException e) {
            errorLogger.error("Error trying to load external references JSON file located in: "+file.toString()+" - "+e.getMessage());
        }

        return mappingE;
    }


    public void appendExecutionHistory(String ontology){

        String folder = ManageProperties.loadPropertyValue("externalproperties");
        try
        {
            String filename= "OMHT_execution_history.log";
            FileWriter fw = new FileWriter(folder+File.separator+filename,true); //the true will append the new data
            fw.write(ontology+"\n");//appends the string to the file
            fw.close();
        }
        catch(IOException ioe)
        {
            errorLogger.error("Error trying to write on execution history: " + ioe.getMessage());
        }

    }

    public String readExecutionHistory(){

        String folder = ManageProperties.loadPropertyValue("externalproperties");
        String filename= "OMHT_execution_history.log";
        String history="";

        try(BufferedReader br = new BufferedReader(new FileReader(folder+File.separator+filename))) {

            String line = br.readLine();

            while (line != null) {
                history += line+";";
                line = br.readLine();
            }
            String everything = sb.toString();
        } catch (FileNotFoundException e) {
            errorLogger.error("Execution history file not founded. Creating empity file.");
            appendExecutionHistory("");
        } catch (IOException e) {
            errorLogger.error("Error trying to read execution history file -->"+folder+File.separator+filename+" message: "+e.getMessage());
        }
        return history;
    }

    /**
     * If 'c' is present on COMMAND the history will be cleaned and all ontologies will be processed on the next run.
     */
    public void deleteExecutionHistory(){
        String folder = ManageProperties.loadPropertyValue("externalproperties");
        String filename= "OMHT_execution_history.log";
        try {
            Files.deleteIfExists(Paths.get(folder + File.separator + filename));
        } catch (IOException e) {
            errorLogger.error("Error trying to delete execution history: " + e.getMessage());
        }
        appendExecutionHistory("");
    }

    public void deleteLogFiles(){
        String folder = ManageProperties.loadPropertyValue("externalproperties");
        String[] filenames= {"OMHT_matchs_totalization.xls","OMHT_summary_matchs.xls","OMHT_execution_history.log","OMHT_external_matches_phase_1_to_be_curated.xls","OMHT_external_references.log","OMHT_harvest_tool_error.log"};
        try {
            for(int i=0;i<filenames.length;i++){
                Files.deleteIfExists(Paths.get(folder + File.separator + filenames[i]));
            }
        } catch (IOException e) {
            errorLogger.error("Error trying to delete files: " + e.getMessage());
        }
        appendExecutionHistory("");
    }


    public HashMap<String,TargetReference> readTargetReferenceHashMap(){


        HashMap<String, TargetReference> hashMap = new HashMap<>();
        String[] aux = new String[6];
        String folder = ManageProperties.loadPropertyValue("externalproperties");
        String filename= "OMHT_external_matches_phase_2.cfg";
        TargetReference tr;

        try(BufferedReader br = new BufferedReader(new FileReader(folder+File.separator+filename))) {

            String line = br.readLine();

            while (line != null) {
                aux = line.split(";");
                tr = new TargetReference(aux[0],aux[1],aux[2],aux[3],aux[4],aux[5]);
                hashMap.put(aux[0],tr);
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            errorLogger.error("Target reference file not founded.");
        } catch (IOException e) {
            errorLogger.error("Error trying to read target reference file -->"+folder+File.separator+filename+" message: "+e.getMessage());
        }
        return hashMap;
    }


    /**
     * Load Ontologies from OBO Foundry
     * Used on Pre Manual Curation process
     */
    public void loadOBOFoundryOntologies(){

        OBOOntologies out=null;


        try {
            URL url = new URL(ManageProperties.loadPropertyValue("obofoundryontologies"));
            Scanner s = new Scanner(url.openStream());

            StringBuffer sb = new StringBuffer();

            while(s.hasNext()){
                sb.append(s.nextLine().trim());
            }


            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();


            out = gson.fromJson(sb.toString(),OBOOntologies.class);



            // read from your scanner
        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
            ex.printStackTrace(); // for now, simply output it.
        }

        for(Ontology ont: out.getOntologies()){
            if(!ont.isObsolete()){
                //println("-->"+ont.getId()+" "+ont.getOntologyPurl());
                oboOntologies.put(ont.getId(),ont.getOntologyPurl());
            }
        }



    }

    /**
     * Load Ontologies from Identifiers.org
     * Used on the Manual Pre Curation Process
     */
    public void loadIdentifiersOntologies(){

        AgroportalRestService ars = new AgroportalRestService();

        String prefix = "";
        List<String> synonmyms = null;
        String url = "";

        for(Identifier i: ars.getIdentifiers()){
            prefix = i.getPrefix();
            url = i.getUrl();
            synonmyms = i.getSynonyms();
            if(synonmyms==null){
                synonmyms =  new ArrayList<>();
            }
            identifiersList.add(new IdentifierEntity(prefix,synonmyms,url));
        }

    }
    
    
    public void println(String value){
        if(printToConsole){
            System.out.println(value);
        }
    }
    
    public void setPrintToConsole(boolean value){
        this.printToConsole=value;
    }


}
