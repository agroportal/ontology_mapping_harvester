package fr.lirmm.agroportal.ontologymappingharvester;

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

            aux = aux.substring(indexOf2+1);

            //System.out.println(aux);
            indexOf1 = aux.indexOf("<");
            indexOf2 = aux.indexOf(">");
            an.setOntologyConcept2(aux.substring(indexOf1, indexOf2+1).replace("<","").replace(">",""));
            an.setOntology2(an.getOntologyConcept2().substring(0,an.getOntologyConcept2().lastIndexOf("/")));


        }

        return an;
    }


}
