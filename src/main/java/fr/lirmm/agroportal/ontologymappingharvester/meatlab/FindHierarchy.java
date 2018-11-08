package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

import fr.lirmm.agroportal.ontologymappingharvester.services.LogService;
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
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class FindHierarchy extends LogService {

    OWLOntology oA;
    File fileIN;
    OWLOntologyID ontologyID;
    OWLOntologyManager man;
    int deepOfMainClass = 0;
    Stack<String> st;
    DefaultPrefixManager pm;

    /**
     * Load ontology from file
     * @return
     */
    public void loadOntology() {

        setupLogProperties("pm","FOODON_MEATLAB", "/home/abrahao/data/meatylab/phase2/");
        pm = new DefaultPrefixManager(null, null, "http://purl.obolibrary.org/obo/");

        man = OWLManager.createOWLOntologyManager();

        stdoutLogger.info("concept;depth;parents");

        fileIN = new File("/home/abrahao/data/meatylab/phase2/FOODON.xrdf");


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


            //stdoutLogger.info("ONTOLOGY:" + oA.toString());

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
        System.out.println("Consistent: " + consistent);
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
            System.out.println("The following classes are unsatisfiable: ");
            for (OWLClass cls : unsatisfiable) {
                System.out.println(" " + cls);
            }
        } else {
            System.out.println("There are no unsatisfiable classes");
        }


//        for (OWLNamedIndividual i : oA.getIndividualsInSignature()) {
//            for (OWLObjectProperty p : oA.getObjectPropertiesInSignature()) {
//                NodeSet<OWLNamedIndividual> individualValues = reasoner.getObjectPropertyValues(i, p);
//                //System.out.println("Vazio: "+individualValues.isEmpty());
//                if(!individualValues.isEmpty()){
//                    Set<OWLNamedIndividual> values = individualValues.getFlattened();
//                    System.out.println("The property values for "+p+" for individual "+i+" are: ");
//                    for (OWLNamedIndividual ind : values) {
//                        System.out.println(" " + ind);
//                    }
//
//                }
//            }
//        }
        // Finally, let's print out the class hierarchy.

        Node<OWLClass> topNode = reasoner.getTopClassNode();
        print(topNode, reasoner, 0, false);


    }

    private void print(@Nonnull Node<OWLClass> parent, @Nonnull OWLReasoner reasoner, int depth, boolean parentFinded) {
        // We don't want to print out the bottom node (containing owl:Nothing
        // and unsatisfiable classes) because this would appear as a leaf node
        // everywhere
        if (parent.isBottomNode()) {

            return;
        }
        // Print an indent to denote parent-child relationships
        //printIndent(depth);
        // Now print the node (containing the child classe

        //System.out.println("##-->"+parent.getRepresentativeElement());

        if(parentFinded == false && parent.getRepresentativeElement().toString().indexOf("FOODON_00002403")>0){
            parentFinded = true;
            deepOfMainClass = depth;
            //stdoutLogger.info (" FINDED --> "+parent.getRepresentativeElement()+ " ");
            st = new Stack<String>();
        }

        if(parentFinded) {

            printNode(parent, depth);

        }


        for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
            assert child != null;
             // Recurse to do the children. Note that we don't have to worry
             // about cycles as there are non in the inferred class hierarchy
             // graph - a cycle gets collapsed into a single node since each
             // class in the cycle is equivalent.
            String shortForm = pm.getShortForm(parent.getRepresentativeElement()).replace(":","");;
            if(st != null){
                st.push(shortForm);
            }

            print(child, reasoner, depth + 1, parentFinded);
            if(st !=null && !st.empty()){
                st.pop();
            }

        }

        if(depth <= deepOfMainClass){
            parentFinded=false;
        }

    }

    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
    }

    private void printNode(@Nonnull Node<OWLClass> node, int depth) {
        // The default prefix used here is only an example.
        // For real ontologies, choose a meaningful prefix - the best
        // choice depends on the actual ontology.

        //System.out.println(node.getRepresentativeElement().getSignature().iterator().next().getDataPropertiesInSignature());

        // Print out a node as a list of class names in curly brackets
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext();) {
            OWLClass cls = it.next();

            // User a prefix manager to provide a slightly nicer shorter name
            String shortForm = pm.getShortForm(cls).replace(":","");
            stdoutLogger.info(""+shortForm + ";"+depth+";"+ st.toString());
            //assertNotNull(shortForm);

        }
    }


    public void shouldCreateAndReadAnnotations() throws Exception {




        for (OWLClass cls : oA.getClassesInSignature()) {
            // Get the annotations on the class that use the label property

            System.out.println("------------------------------------------------------------------------");
            System.out.println("Classes: "+cls.toString());
            System.out.println("------------------------------------------");

            for (OWLOntology o : oA.getImportsClosure()) {

                //println("AnnotationAssetationAxioms: "+o.getAnnotationAssertionAxioms(cls.getIRI()));

                //annotationObjects(o.getAnnotationAssertionAxioms(cls.getIRI()),label)

                for (OWLAnnotationAssertionAxiom annotationAssertionAxiom : o.getAnnotationAssertionAxioms(cls.getIRI())) {
                    //println("Entrou no if do assetation");

                    System.out.println("=======================");
                    System.out.println("AnnotationAssertationAxiom: "+annotationAssertionAxiom);
                    System.out.println("Subject: "+annotationAssertionAxiom.getSubject());
                    System.out.println("Property: "+annotationAssertionAxiom.getProperty());

                    System.out.println("Assertion with out anotation: "+annotationAssertionAxiom.getAnnotationPropertiesInSignature());


                    if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                        //println("Entrou no if do annotation get value");
                        OWLLiteral val = (OWLLiteral) annotationAssertionAxiom.getValue();
                        //if (val.hasLang("en")) {
                        System.out.println("PropertyValue: " +
                                val.getLiteral());
                        //}
                    }

                }
            }
            System.out.println("----------------------------------------------------------------------");
        }
    }


    public static void main(String[] args){

        FindHierarchy f = new FindHierarchy();
        f.loadOntology();
        System.out.println(f.oA.toString());
        try {
            f.shouldUseReasoner();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
