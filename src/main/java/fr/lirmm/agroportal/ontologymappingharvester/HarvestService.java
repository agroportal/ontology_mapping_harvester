package fr.lirmm.agroportal.ontologymappingharvester;

public interface HarvestService {

    public boolean loadOntology();

    public void findMatches();

    public void printResults();

    public void saveFile();

}
