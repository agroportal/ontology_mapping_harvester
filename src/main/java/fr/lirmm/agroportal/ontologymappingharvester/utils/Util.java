package fr.lirmm.agroportal.ontologymappingharvester.utils;

import fr.lirmm.agroportal.ontologymappingharvester.entities.AnnotationAssertationEntity;
import org.semanticweb.owlapi.model.OWLAxiom;

public class Util {

    public static AnnotationAssertationEntity getAnnotationAssertationEntity(OWLAxiom ax, int id) {

        String aux = ax.toString();
        String aux2 = "";

        // System.out.println(aux);
        int indexOf1 = 0;
        int indexOf2 = 0;
        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);

        if(aux.length()>6) {

            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            an.setAssertion(aux.substring(indexOf1, indexOf2+1).replace("<","").replace(">",""));
            aux = aux.substring(indexOf2+1);

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            an.setOntologyConcept1(aux.substring(indexOf1, indexOf2+1).replace("<","").replace(">",""));
            an.setOntology1(an.getOntologyConcept1().substring(0,an.getOntologyConcept1().lastIndexOf("/")));

            aux = aux.substring(indexOf2+1).replace(" ","").replace("\"","");

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            if(indexOf1>-1 && indexOf2>1) {
                an.setOntologyConcept2(aux.substring(indexOf1, indexOf2 + 1).replace("<", "").replace(">", ""));
                an.setOntology2(an.getOntologyConcept2().substring(0, an.getOntologyConcept2().lastIndexOf("/")));
            }else{

                if(aux.indexOf("http")==0 || aux.indexOf("https")==0){

                    // Enter here if it founds a literal that begins with HTTP or HTTPS
                    an.setOntology2(aux.substring(0,aux.lastIndexOf("/")));

                }else {
                    if (aux.indexOf(":") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf(":")).toUpperCase());
                    } else if (aux.indexOf("_") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("_")).toUpperCase());
                    } else if (aux.indexOf("-") > 1) {
                        an.setOntology2(aux.substring(0, aux.indexOf("-")).toUpperCase());
                    } else {
                        an.setOntology2(aux.toUpperCase());
                    }
                }
                an.setOntologyConcept2(aux);
            }

        }

        return an;
    }


    public static AnnotationAssertationEntity getAnnotationAssertationEntity(String ontologyName, String subject, String property, String propertyValue, boolean isIRI, int id ){

        AnnotationAssertationEntity an = new AnnotationAssertationEntity();
        an.setId(id);
        String name="";
        String aux ="";

        if(ontologyName.toLowerCase().indexOf("anonymous")>-1){
            try {
                name = ontologyName.substring(ontologyName.lastIndexOf("/"), ontologyName.lastIndexOf("_"));
            }catch(Exception e){
                name = "Current Ontology";
            }
        }
        an.setOntology1(name);
        an.setOntologyConcept1(subject);
        an.setOntologyConcept2(propertyValue);
        an.setAssertion(property.replace("<","").replace(">",""));

        if(isIRI){
            //System.out.println("#################################################################### "+propertyValue.substring(0,propertyValue.lastIndexOf("_")));

            // enter here if it is an IRI
            an.setOntology2(propertyValue.substring(0,propertyValue.lastIndexOf("_")));
        }else {

            // Enter here if it is not an IRI
            aux = propertyValue.toLowerCase();
            if(aux.indexOf("http")==0 || aux.indexOf("https")==0){

                // Enter here if it founds a literal that begins with HTTP or HTTPS
                an.setOntology2(propertyValue.substring(0,propertyValue.lastIndexOf("/")));

            }else {

                // Enter here if it founds a literal with a reference to an external ID
                if (propertyValue.indexOf(":") > 1) {
                    an.setOntology2(propertyValue.substring(0, propertyValue.indexOf(":")).toUpperCase());
                } else if (propertyValue.indexOf("_") > 1) {
                    an.setOntology2(propertyValue.substring(0, propertyValue.indexOf("_")).toUpperCase());
                } else if (propertyValue.indexOf("-") > 1) {
                    an.setOntology2(propertyValue.substring(0, propertyValue.indexOf("-")).toUpperCase());
                } else {
                    an.setOntology2(propertyValue.toUpperCase());
                }
            }

        }


        return an;

    }

}
