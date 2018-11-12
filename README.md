## Ontology Mapping Harvester Tool

## Introduction

OMHT (Ontology Mapping Harvester Tool) is a script in JAVA language design to extract internal mappings from ontologies hosted on th AGROPORTAL http://agroportal.lirmm.fr. The features from this script are:
- Locate and extract internal mappings;
- Support formats: owl, rdf, skos, obo;
- Generate valis IRIs for mappings;
- Upload mappings using the AGROPORTAL APIs;
- Support to AGROPORTAL API security Keys;
- Generate LOGs from extraction process;

## System Requirements

1. Java 1.8 or newer.
2. 5 MB disk for the script and enougth for the ontologies (depend on the ontology size).
3. At least 16 GB RAM.
4. Access to internet (necessary to access AGROPORTAL APIs and external site resources).

## Instalation

1. Dowload the `omht.jar` file and save it on an empty folder.
2. Change file permitions: `chmod 777 omht.jar [enter]`
3. Enter the folder on command shell and execute: `java -jar omht.jar [enter]`
4. Follow screen instructions to setup **user name** and **API keys** for AGROPORTAL and BIOPORTAL as follows:

`java -jar omht.jar -restagroportaluser john [enter]`

`java -jar omht.jar -restagroportalapikey eert-5543-ty56-5544-jhhh-2fv4 [enter]`

`java -jar omht.jar -restbioportaluser john [enter]`

`java -jar omht.jar -restbiortalapikey eert-5543-ty56-5544-jhhh-2fv4 [enter]`

note: The API keys are provided on AGROPORTAL and BIOPORTAL on the User Profile Link. User name must be exactally the same user name from AGROPORTAL and BIOPORTAL.

5. Test the access executing: `java -jar omht.jar -help [enter]`

## Basic Operation

1. Find mapings on an ontology hosted on AGROPORTAL:

`java -jar omht.jar -jlsuc [ACRONYM] [enter]`

Example: `java -jar omht.jar -jlsuc FOODON [enter]`

After processment the following files will be generated on the folder `omht_output` for each ontology processed:

`FOODON.json`: Mappings on the JSON format ready to be uploaded to AGROPORTAL.

`FOODON.log`: Logging information about the processment.

`FOODON.sts`: Output file to visualisation tool (internal use).

General information are stored on the `omht_config` folder:

`OMHT_execution_history.log`: list of ontologies aready processed.

`OMHT_external_matches_phase_1.cfg`: manual curation file (provided by Agroportal team).

`OMHT_external_matches_phase_1_to_be_curated.xls`: feed back for manual curation (internal use).

`OMHT_external_references.log`: information about where the mappings are located.

`OMHT_harvest_tool_error.log`: errors on execution of the script.

`OMHT_matchs_totalization.xls`: totalization of mappigs by type and ontology.

`OMHT_summary_matchs.xls`: totalization of mappigs founded for the ontology(ies).

2. Post mappings on AGROPORTAL:

`java -jar omht.jar -restagroportal post [SELECTION] [enter]`

Example: `java -jar omht.jar -restagroportal post FOODON [enter]`: to post the internal mappings for FOODON ontology.

Example: `java -jar omht.jar -restagroportal post all [enter]`: to post the internal mappings for all ontologies located on the `omht_output` folder.

note: to be posted on AGROPORTAL the mappings must be inside the `omht_output` folder and the files must be on the `[ACRONYM].json` format like: `FOODON.json`.

