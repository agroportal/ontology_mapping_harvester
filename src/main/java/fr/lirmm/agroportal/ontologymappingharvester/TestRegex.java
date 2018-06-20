package fr.lirmm.agroportal.ontologymappingharvester;

public class TestRegex {

    private static String[] INVALIDCHARACTERS;

    public static void main(String[] args){

        INVALIDCHARACTERS = new String[]{"2",":","2","-","2","_","3"," ","1","[pthr]\\d{5}"};


        String value = "fia:Http://";

            System.out.println(value.matches("^([a-zA-Z].*)(:(?!//)).*"));


    }


    private static boolean isValidMap(String value){

        for(int i =0;i<INVALIDCHARACTERS.length-1;i=i+2){

            if(value.matches("(.*"+INVALIDCHARACTERS[i+1]+".*){"+INVALIDCHARACTERS[i]+",}")){

                return false;
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
