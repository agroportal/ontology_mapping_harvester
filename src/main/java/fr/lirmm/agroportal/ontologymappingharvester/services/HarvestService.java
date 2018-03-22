package fr.lirmm.agroportal.ontologymappingharvester.services;

import java.util.ArrayList;

public interface HarvestService {

    public boolean loadOntology();

    public void findMatches();

    public void saveFile();

    public void parse(String command, ArrayList<String> fileName);

    public void parse(String command, String dir);

    public void buildJson();

}
