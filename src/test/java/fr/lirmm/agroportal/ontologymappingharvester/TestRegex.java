package fr.lirmm.agroportal.ontologymappingharvester;

public class TestRegex {

    private static String[] INVALIDCHARACTERS;

    public static void main(String[] args){

        INVALIDCHARACTERS = new String[]{"2",":","2","-","2","_","3"," ","1","[pthr]\\d{5}"};


        String value = "GO:0000090 NO MATCH mitotic anaphase";


                System.out.println(isValidMap(value));



    }


    private  static boolean isValidMap(String value){


        //TODO ATTENTION
        value = value.toLowerCase();

        int length = value.length();
        int count = value.replaceAll(" ","").length();

        if(value.indexOf("http")!=0 && value.indexOf("smtp")!=0 && value.indexOf("ftp")!=0) {

            // this verify if the string begin with: abcdf:SOMETHING  It will not match if it is an AAAA://SOMETHING
            if(!value.matches("^([a-zA-Z].*)(:(?!//)).*")){

                for (int i = 0; i < INVALIDCHARACTERS.length - 1; i = i + 2) {

                    if (value.matches("(.*" + INVALIDCHARACTERS[i + 1] + ".*){" + INVALIDCHARACTERS[i] + ",}")) {
                        //externalLogger.warn("INVALID CONCEPT FOUNDED - Ontology: " + currentOntologyName + " Concept: " + value);
                        return false;
                    }

                    if(length-count>=3){
                        return false;
                    }
                }
            }else{

                if(value.indexOf(" ")>-1){
                    for (int i = 0; i < INVALIDCHARACTERS.length - 1; i = i + 2) {

                        if (value.matches("(.*" + INVALIDCHARACTERS[i + 1] + ".*){" + INVALIDCHARACTERS[i] + ",}")) {
                            //externalLogger.warn("INVALID CONCEPT FOUNDED - Ontology: " + currentOntologyName + " Concept: " + value);
                            return false;
                        }

                        if(length-count>=3){
                            return false;
                        }
                    }
                }

            }

        }
        return true;
    }

    private static boolean isValidPatern(String value){
        if(value.matches("^([a-zA-Z0-9])")){
            return true;
        }else{
            return false;
        }
    }
}
