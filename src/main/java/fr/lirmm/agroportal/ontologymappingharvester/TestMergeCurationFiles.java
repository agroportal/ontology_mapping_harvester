package fr.lirmm.agroportal.ontologymappingharvester;

import fr.lirmm.agroportal.ontologymappingharvester.services.MergeCurationFIlesService;

public class TestMergeCurationFiles {

    public static void main(String[] args){

        MergeCurationFIlesService mcs = new MergeCurationFIlesService();
        mcs.execute();

    }
}
