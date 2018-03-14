package fr.lirmm.agroportal.ontologymappingharvester.services;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HarvestAllFormatsService extends BaseService implements HarvestService {


    File fileA;
    OWLDataFactory df;
    private String[] OWL_MATCH;
    private boolean isIRI;


    public HarvestAllFormatsService(){
        super();
        OWL_MATCH = new String[]{"oboInOwl#hasDbXref","http://www.geneontology.org/formats/oboInOwl#hasDbXref","http://www.w3.org/2004/02/skos/core#exactMatch", "http://www.w3.org/2004/02/skos/core#broadMatch", "http://www.w3.org/2004/02/skos/core#closeMatch", "http://www.w3.org/2004/02/skos/core#narrowMatch", "http://www.w3.org/2004/02/skos/core#relatedMatch"};
        isIRI = false;
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


            //OWLDocumentFormat format = oA.getNonnullFormat();

            //System.out.println("ONTOLOGY FORMAT:" + format.getKey());


            System.out.println("ONTOLOGY:" + oA.toString());

            ontologyID = oA.getOntologyID();

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
        return true;

    }

    @Override
    public void findMatches() {

        boolean countIndividualsFlag = true;
        String auxProperty="";

        sb.append("----------------------------------------------------------\n");

        sb.append("Classes\n");
        for (OWLClass c : oA.getClassesInSignature()) {
            sb.append(c.toString()+"\n");
            countClasses++;
        }
        sb.append("Total Classes: " + countClasses+"\n");
        sb.append("----------------------------------------------------------\n");

        sb.append("Filter - Annotations\n");

        for (int x = 0; x < OWL_MATCH.length; x++) {

            sb.append("======================= Searching for: " + OWL_MATCH[x] + "=======================\n");
            System.out.println("======================= Searching for: " + OWL_MATCH[x] + "=======================");

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

                        if(aux.indexOf(OWL_MATCH[x]) > -1) {

                            System.out.println("Ontology: "+ oA.getOntologyID());
                            System.out.println("Subject: " + annotationAssertionAxiom.getSubject());
                            System.out.println("Property: " + annotationAssertionAxiom.getProperty());

                            if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                                //System.out.println("Entrou no if do annotation get value");
                                OWLLiteral val = (OWLLiteral) annotationAssertionAxiom.getValue();
                                //if (val.hasLang("en")) {
                                System.out.println("PropertyValueLiteral: " + val.getLiteral());
                                auxProperty = val.getLiteral();
                                isIRI = false;
                                //}
                            } else if (annotationAssertionAxiom.getValue() instanceof IRI) {
                                System.out.println("PropertyValueIRI: " + annotationAssertionAxiom.getValue());
                                auxProperty = annotationAssertionAxiom.getValue().toString();
                                isIRI = true;
                                //}
                            }


                            an = Util.getAnnotationAssertationEntity(oA.getOntologyID().toString(),annotationAssertionAxiom.getSubject().toString(),annotationAssertionAxiom.getProperty().toString(),auxProperty,isIRI,countMatch++);
                            sb.append(an.toString()+"\n");
                            MapIRI = an.getOntology2();
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
                    if (aux.indexOf(OWL_MATCH[x]) > -1) {

                        an = Util.getAnnotationAssertationEntity(ax, countMatch++);
                        sb.append(an.toString() + "\n");
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
                maps.put(OWL_MATCH[x], mmp);

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
            System.out.println(" " + cls);
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
            System.out.println(" " + ind);
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
                System.out.println("Vazio: "+individualValues.isEmpty());
                Set<OWLNamedIndividual> values = individualValues.getFlattened();
                System.out.println("The property values for "+p+" for individual "+i+" are: ");
                for (OWLNamedIndividual ind : values) {
                    System.out.println(" " + ind);
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

            System.out.println("------------------------------------------------------------------------");
            System.out.println("Classes: "+cls.toString());
            System.out.println("------------------------------------------");

            for (OWLOntology o : oA.getImportsClosure()) {

                //System.out.println("AnnotationAssetationAxioms: "+o.getAnnotationAssertionAxioms(cls.getIRI()));

                //annotationObjects(o.getAnnotationAssertionAxioms(cls.getIRI()),label)

                for (OWLAnnotationAssertionAxiom annotationAssertionAxiom : o.getAnnotationAssertionAxioms(cls.getIRI())) {
                    //System.out.println("Entrou no if do assetation");

                    System.out.println("=======================");
                    System.out.println("AnnotationAssertationAxiom: "+annotationAssertionAxiom);
                    System.out.println("Subject: "+annotationAssertionAxiom.getSubject());
                    System.out.println("Property: "+annotationAssertionAxiom.getProperty());

                    System.out.println("Assertion with out anotation: "+annotationAssertionAxiom.getAnnotationPropertiesInSignature());


                    if (annotationAssertionAxiom.getValue() instanceof OWLLiteral) {
                        //System.out.println("Entrou no if do annotation get value");
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
}
