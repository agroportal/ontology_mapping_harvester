package fr.lirmm.agroportal.ontologymappingharvester.meatlab;

public class Main {

    public static void main(String[] args){

        Parser parser = new Parser();
        parser.execute("FOODON.json.tmp", "foodon","DATABASE_ANSES");

        Parser parser3 = new Parser();
        parser3.execute("FOODON.json.tmp", "foodon","DATABASE_USDA");



    }
}
