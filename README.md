## Ontology Mapping Harvester Tool

### Authors 
Elcio Abrahao (@elcioabrahao), with supervision of Clement Jonquet (@jonquet). Contributions from Amir Laadhar (@amirlad)

### Copyright
LIRMM, University of Montpellier

## Introduction

OMHT (Ontology Mapping Harvester Tool) is a script in Java language designed to extract declared mappings from ontologies and reify them into specific objects with metadata and provenance information. OMHT deals with ontologies hosted on the AgroPortal (http://agroportal.lirmm.fr) or the NCBO BioPortal http://bioportal.bioontology.org/) ontology repositories. Mainly, OMHT takes as input a set of ontology acronyms and returns a JSON file for each input ontology that stores extracted mappings along with their metadata. Sometime, the target ontology and term are not explicit (especially with OBO XRefs which do not use URIs) therefore, OMHT relies on a manually curated file to resolve ambiguous targets. 

The features of this script are:
- Locate and extract mappings explicilty declared in ontologies or any kind of semantic resources;
- Process semantic resources in XML/RDF syntax an relies on the ontology repository to deal with different representation languages (AgroPortal handles RDFS, OWL, OBO, UMLS-RRF and SKOS);
- Generate as much as possible valid URIs for mapped terms and target ontolgies;
- Upload mappings to AgroPortal using the Web service APIs;
- Generate mappings in the OntoPortal mapping format and logs from extraction process.

The standard properties used by OMHT to identify declared mappings inside a source file are the following: owl:sameAs, skos:exactMatch, skos:closeMatch, skos:broadMatch, skos:narrowMatch, skos:relatedMatch, oboInOwl#hasDbXref and optionally rdfs:seeAlso.

## System requirements

1. Java 1.8 or higher.
2. 5 MB disk for the script and enougth for the ontologies (depend on the ontology size).
3. At least 16 Gb RAM.
4. Access to internet (necessary to access AgroPortal/BioPortal APIs and external Web applications).

## Instalation

1. Dowload the `omht.jar` file and save it on an empty folder.
2. Change file permitions: `chmod 777 omht.jar [enter]`
3. Enter the folder on command shell and execute: `java -jar omht.jar [enter]`
4. Follow screen instructions to setup **user name** and **API keys** for AgroPortal/BioPortal as follows:

`java -jar omht.jar -restagroportaluser john [enter]`

`java -jar omht.jar -restagroportalapikey eert-5543-ty56-5544-jhhh-2fv4 [enter]`

`java -jar omht.jar -restbioportaluser john [enter]`

`java -jar omht.jar -restbiortalapikey eert-5543-ty56-5544-jhhh-2fv4 [enter]`

Note: API keys are provided by AgroPortal and BioPortal respectively. User name must be exactally the same user name than on AgroPortal/BioPortal. To get an API key see documentation here: https://www.bioontology.org/wiki/BioPortal_Help#Getting_an_API_key

5. Test the access executing: `java -jar omht.jar -help [enter]`

## Basic operation

1. Extract mapings from an ontology hosted on AgroPortal:

`java -jar omht.jar -jlsuc [ACRONYM] [enter]`

Example: `java -jar omht.jar -jlsuc FOODON [enter]`

After processment the following files will be generated on the folder `omht_output` for each ontology processed:

`FOODON.json`: Mappings in the JSON format ready to be uploaded to AgroPortal.

`FOODON.log`: Logging information about the processment.

`FOODON.sts`: Output file to visualisation tool (internal use).

General information are stored on the `omht_config` folder:

`OMHT_execution_history.log`: list of ontologies aready processed.

`OMHT_external_matches_phase_1.cfg`: manual curation file (provided by AgroPortal team).

`OMHT_external_matches_phase_1_to_be_curated.xls`: feedback for manual curation (internal use).

`OMHT_external_references.log`: information about where the mappings are located.

`OMHT_harvest_tool_error.log`: errors on execution of the script.

`OMHT_matchs_totalization.xls`: totalization of mappings by type and ontology.

`OMHT_summary_matchs.xls`: totalization of mappings founded for the ontology(ies).

2. Post mappings on AgroPortal:

`java -jar omht.jar -restagroportal post [SELECTION] [enter]`

Example: `java -jar omht.jar -restagroportal post FOODON [enter]`: to post the declared mappings for FOODON ontology.

Example: `java -jar omht.jar -restagroportal post all [enter]`: to post the declared mappings for all ontologies located on the `omht_output` folder.

Note: to be posted on AgroPortal the mappings must be inside the `omht_output` folder and the files must be on the `[ACRONYM].json` format like: `FOODON.json`.

3. To show help information

`java -jar omht.jar -help [enter]`

**Basic operation:**

1) Show this help information

java -jar omht.jar -help

2) Find declared mappings for one ontology in AgroPortal

java -jar omhtjar -jlsuc [ONTOLOGY_ACRONYM]

Example: 
java -jar omhtjar -jlsuc FOODON

3) Find declared mappings for all ontologies in AgroPortal

java -jar omhtjar -jlsdc

4) Find declared mappings for a list of ontologies in AgroPortal

java -jar omhtjar -jlsuc {[ACRONYM_1], [ACRONYM_2], ...}

Example:
java -jar omhtjar -jlsuc FOODON AGRO AGROVOC PO TO

5) Post all mappings on output folder (*.json) in AgroPortal

java -jar omhtjar -restagroportal post all

6) Delete all mappings ganerated by this script for one ontology

java -jar omhtjar -restagroportal delete [ACRONYM]

Example:
java -jar omhtjar -restagroportal delete FOODON

7) Parameter list:

-j Generate JSON files

-l Generate LOG files

-s Gerate Statistics

-p print LOG on screen

-g Generate javascript for Graph representation of matches

-d Download ontologies from AGROPORTAL

-h Download ontologies from STAGE AGROPORTAL

-u Download an unique ontology (require destination folder and acronym)

-c Clean execution history for the current script execution

8) Setup custom config folder

java -jar omhtjar -configfolder [FOLDER_PATH]

9) Setup custom output folder

java -jar omhtjar -outputfolder [FOLDER_PATH]

10) Setup URL from AGROPORTAL REST Service

java -jar omhtjar -restagroportalurl [URL]

11) Setup URL from BIOPORTAL REST Service

java -jar omhtjar -restbioportalurl [URL]

12) Setup AGROPORTAL REST Service API KEY

java -jar omhtjar -restagroportalapikey [API_KEY]

13) Setup BIOPORTAL REST Service API KEY

java -jar omhtjar -restbioportalapikey [API_KEY]

14) Setup AGROPORTAL user name

java -jar omhtjar -restbioportaluser [API_KEY]

15) Setup BIOPORTAL user name

java -jar omhtjar -restagroportaluser [API_KEY]

## Curation File

OMHT uses a curation file to resolve the targets found in ontologies (mostly to deal with oboInOwl:hasDbXref XRefs heavily used in the OBO world). The last version of this file is included in the distribution of OMHT: https://github.com/agroportal/ontology_mapping_harvester/src/main/resources/OMHT_external_matches_phase_1.cfg

This file is automatically copied to the omht_config folder when the jar file is executed for the fisrt time.

If you have a old version of the script please check the date of the file to assure you got the last version of the curation file in orther to get better mappings.
