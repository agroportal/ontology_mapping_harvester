package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Main {

    public static void main(String[] args){

        Parser parser = new Parser();
        parser.execute("FOODON.json", "foodon","CIQUAL_2017_EuroFIR_2018_04_18_LanguaL");

    }
}
