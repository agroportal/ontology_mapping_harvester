package fr.lirmm.agroportal.ontologymappingharvester;

public class TestRegex {

    private static String[] INVALIDCHARACTERS;

    public static void main(String[] args){

        INVALIDCHARACTERS = new String[]{"2",":","2","-","2","_","3"," ","1","[pthr]\\d{5}"};


        String value = "pthr51d34";

        if(isValidMap(value)){
            System.out.println("true");
        }else{
            System.out.println("FALSE");
        }

    }


    private static boolean isValidMap(String value){

        for(int i =0;i<INVALIDCHARACTERS.length-1;i=i+2){

            if(value.matches("(.*"+INVALIDCHARACTERS[i+1]+".*){"+INVALIDCHARACTERS[i]+",}")){

                return false;
            }
        }
        return true;
    }
}
