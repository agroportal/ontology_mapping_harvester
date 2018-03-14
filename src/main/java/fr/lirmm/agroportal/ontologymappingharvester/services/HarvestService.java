package fr.lirmm.agroportal.ontologymappingharvester.services;

public interface HarvestService {

    public boolean loadOntology();

    public void findMatches();

    public void printResults();

    public void saveFile();

}
