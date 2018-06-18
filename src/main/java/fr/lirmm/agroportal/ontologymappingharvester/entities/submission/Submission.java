package fr.lirmm.agroportal.ontologymappingharvester.entities.submission;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Submission {

	@SerializedName("id")
	@Expose
	private String id;
	@SerializedName("submissionId")
	@Expose
	private Integer submissionId;
	@SerializedName("metrics")
	@Expose
	private String metrics;
	@SerializedName("ontology")
	@Expose
	private Ontology ontology;
	@SerializedName("submissionStatus")
	@Expose
	private List<String> submissionStatus = null;
	@SerializedName("classType")
	@Expose
	private Object classType;
	@SerializedName("hasOntologyLanguage")
	@Expose
	private String hasOntologyLanguage;
	@SerializedName("prefLabelProperty")
	@Expose
	private String prefLabelProperty;
	@SerializedName("definitionProperty")
	@Expose
	private String definitionProperty;
	@SerializedName("synonymProperty")
	@Expose
	private String synonymProperty;
	@SerializedName("authorProperty")
	@Expose
	private String authorProperty;
	@SerializedName("hierarchyProperty")
	@Expose
	private Object hierarchyProperty;
	@SerializedName("obsoleteProperty")
	@Expose
	private Object obsoleteProperty;
	@SerializedName("obsoleteParent")
	@Expose
	private Object obsoleteParent;
	@SerializedName("homepage")
	@Expose
	private String homepage;
	@SerializedName("publication")
	@Expose
	private String publication;
	@SerializedName("URI")
	@Expose
	private String uRI="";
	@SerializedName("naturalLanguage")
	@Expose
	private List<String> naturalLanguage = null;
	@SerializedName("documentation")
	@Expose
	private String documentation;
	@SerializedName("version")
	@Expose
	private Object version;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("contact")
	@Expose
	private List<Contact> contact = null;
	@SerializedName("creationDate")
	@Expose
	private String creationDate;
	@SerializedName("released")
	@Expose
	private String released;
	@SerializedName("numberOfClasses")
	@Expose
	private Integer numberOfClasses;
	@SerializedName("numberOfIndividuals")
	@Expose
	private Integer numberOfIndividuals;
	@SerializedName("numberOfProperties")
	@Expose
	private Integer numberOfProperties;
	@SerializedName("maxDepth")
	@Expose
	private Integer maxDepth;
	@SerializedName("maxChildCount")
	@Expose
	private Integer maxChildCount;
	@SerializedName("averageChildCount")
	@Expose
	private Integer averageChildCount;
	@SerializedName("classesWithOneChild")
	@Expose
	private Integer classesWithOneChild;
	@SerializedName("classesWithMoreThan25Children")
	@Expose
	private Integer classesWithMoreThan25Children;
	@SerializedName("classesWithNoDefinition")
	@Expose
	private Integer classesWithNoDefinition;
	@SerializedName("modificationDate")
	@Expose
	private Object modificationDate;
	@SerializedName("entities")
	@Expose
	private Object entities;
	@SerializedName("numberOfAxioms")
	@Expose
	private Object numberOfAxioms;
	@SerializedName("keyClasses")
	@Expose
	private Object keyClasses;
	@SerializedName("keywords")
	@Expose
	private String keywords;
	@SerializedName("knownUsage")
	@Expose
	private Object knownUsage;
	@SerializedName("notes")
	@Expose
	private String notes;
	@SerializedName("conformsToKnowledgeRepresentationParadigm")
	@Expose
	private Object conformsToKnowledgeRepresentationParadigm;
	@SerializedName("hasContributor")
	@Expose
	private Object hasContributor;
	@SerializedName("hasCreator")
	@Expose
	private Object hasCreator;
	@SerializedName("designedForOntologyTask")
	@Expose
	private List<Object> designedForOntologyTask = null;
	@SerializedName("wasGeneratedBy")
	@Expose
	private Object wasGeneratedBy;
	@SerializedName("wasInvalidatedBy")
	@Expose
	private Object wasInvalidatedBy;
	@SerializedName("curatedBy")
	@Expose
	private Object curatedBy;
	@SerializedName("endorsedBy")
	@Expose
	private List<Object> endorsedBy = null;
	@SerializedName("fundedBy")
	@Expose
	private Object fundedBy;
	@SerializedName("translator")
	@Expose
	private Object translator;
	@SerializedName("hasDomain")
	@Expose
	private String hasDomain;
	@SerializedName("hasFormalityLevel")
	@Expose
	private String hasFormalityLevel;
	@SerializedName("hasLicense")
	@Expose
	private String hasLicense;
	@SerializedName("hasOntologySyntax")
	@Expose
	private String hasOntologySyntax;
	@SerializedName("isOfType")
	@Expose
	private String isOfType;
	@SerializedName("usedOntologyEngineeringMethodology")
	@Expose
	private Object usedOntologyEngineeringMethodology;
	@SerializedName("usedOntologyEngineeringTool")
	@Expose
	private String usedOntologyEngineeringTool;
	@SerializedName("useImports")
	@Expose
	private List<String> useImports = null;
	@SerializedName("hasPriorVersion")
	@Expose
	private Object hasPriorVersion;
	@SerializedName("isBackwardCompatibleWith")
	@Expose
	private List<Object> isBackwardCompatibleWith = null;
	@SerializedName("isIncompatibleWith")
	@Expose
	private List<Object> isIncompatibleWith = null;
	@SerializedName("deprecated")
	@Expose
	private Boolean deprecated;
	@SerializedName("versionIRI")
	@Expose
	private Object versionIRI;
	@SerializedName("ontologyRelatedTo")
	@Expose
	private List<Object> ontologyRelatedTo = null;
	@SerializedName("comesFromTheSameDomain")
	@Expose
	private List<Object> comesFromTheSameDomain = null;
	@SerializedName("similarTo")
	@Expose
	private List<Object> similarTo = null;
	@SerializedName("isAlignedTo")
	@Expose
	private List<Object> isAlignedTo = null;
	@SerializedName("explanationEvolution")
	@Expose
	private Object explanationEvolution;
	@SerializedName("generalizes")
	@Expose
	private Object generalizes;
	@SerializedName("hasDisparateModelling")
	@Expose
	private Object hasDisparateModelling;
	@SerializedName("hiddenLabel")
	@Expose
	private Object hiddenLabel;
	@SerializedName("coverage")
	@Expose
	private Object coverage;
	@SerializedName("publisher")
	@Expose
	private Object publisher;
	@SerializedName("identifier")
	@Expose
	private String identifier;
	@SerializedName("source")
	@Expose
	private Object source;
	@SerializedName("abstract")
	@Expose
	private String _abstract;
	@SerializedName("alternative")
	@Expose
	private Object alternative;
	@SerializedName("hasPart")
	@Expose
	private Object hasPart;
	@SerializedName("isFormatOf")
	@Expose
	private Object isFormatOf;
	@SerializedName("hasFormat")
	@Expose
	private Object hasFormat;
	@SerializedName("audience")
	@Expose
	private Object audience;
	@SerializedName("valid")
	@Expose
	private Object valid;
	@SerializedName("accrualMethod")
	@Expose
	private Object accrualMethod;
	@SerializedName("accrualPeriodicity")
	@Expose
	private Object accrualPeriodicity;
	@SerializedName("accrualPolicy")
	@Expose
	private Object accrualPolicy;
	@SerializedName("endpoint")
	@Expose
	private String endpoint;
	@SerializedName("dataDump")
	@Expose
	private String dataDump;
	@SerializedName("csvDump")
	@Expose
	private String csvDump;
	@SerializedName("openSearchDescription")
	@Expose
	private String openSearchDescription;
	@SerializedName("uriLookupEndpoint")
	@Expose
	private String uriLookupEndpoint;
	@SerializedName("uriRegexPattern")
	@Expose
	private Object uriRegexPattern;
	@SerializedName("depiction")
	@Expose
	private String depiction;
	@SerializedName("logo")
	@Expose
	private Object logo;
	@SerializedName("competencyQuestion")
	@Expose
	private Object competencyQuestion;
	@SerializedName("usedBy")
	@Expose
	private List<Object> usedBy = null;
	@SerializedName("metadataVoc")
	@Expose
	private List<Object> metadataVoc = null;
	@SerializedName("hasDisjunctionsWith")
	@Expose
	private Object hasDisjunctionsWith;
	@SerializedName("toDoList")
	@Expose
	private Object toDoList;
	@SerializedName("example")
	@Expose
	private Object example;
	@SerializedName("preferredNamespaceUri")
	@Expose
	private Object preferredNamespaceUri;
	@SerializedName("preferredNamespacePrefix")
	@Expose
	private Object preferredNamespacePrefix;
	@SerializedName("morePermissions")
	@Expose
	private Object morePermissions;
	@SerializedName("useGuidelines")
	@Expose
	private Object useGuidelines;
	@SerializedName("curatedOn")
	@Expose
	private Object curatedOn;
	@SerializedName("repository")
	@Expose
	private String repository;
	@SerializedName("bugDatabase")
	@Expose
	private String bugDatabase;
	@SerializedName("mailingList")
	@Expose
	private Object mailingList;
	@SerializedName("exampleIdentifier")
	@Expose
	private String exampleIdentifier;
	@SerializedName("award")
	@Expose
	private Object award;
	@SerializedName("copyrightHolder")
	@Expose
	private Object copyrightHolder;
	@SerializedName("associatedMedia")
	@Expose
	private Object associatedMedia;
	@SerializedName("workTranslation")
	@Expose
	private Object workTranslation;
	@SerializedName("translationOfWork")
	@Expose
	private Object translationOfWork;
	@SerializedName("includedInDataCatalog")
	@Expose
	private List<String> includedInDataCatalog = null;
	@SerializedName("uploadFilePath")
	@Expose
	private String uploadFilePath;
	@SerializedName("diffFilePath")
	@Expose
	private Object diffFilePath;
	@SerializedName("masterFileName")
	@Expose
	private Object masterFileName;
	@SerializedName("missingImports")
	@Expose
	private List<Object> missingImports = null;
	@SerializedName("pullLocation")
	@Expose
	private String pullLocation;
	@SerializedName("@id")
	@Expose
	private String id3;
	@SerializedName("@type")
	@Expose
	private String type;
	@SerializedName("links")
	@Expose
	private Links_ links;
	@SerializedName("@context")
	@Expose
	private Context___ context;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(Integer submissionId) {
		this.submissionId = submissionId;
	}

	public String getMetrics() {
		return metrics;
	}

	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}

	public Ontology getOntology() {
		return ontology;
	}

	public void setOntology(Ontology ontology) {
		this.ontology = ontology;
	}

	public List<String> getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(List<String> submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public Object getClassType() {
		return classType;
	}

	public void setClassType(Object classType) {
		this.classType = classType;
	}

	public String getHasOntologyLanguage() {
		return hasOntologyLanguage;
	}

	public void setHasOntologyLanguage(String hasOntologyLanguage) {
		this.hasOntologyLanguage = hasOntologyLanguage;
	}

	public String getPrefLabelProperty() {
		return prefLabelProperty;
	}

	public void setPrefLabelProperty(String prefLabelProperty) {
		this.prefLabelProperty = prefLabelProperty;
	}

	public String getDefinitionProperty() {
		return definitionProperty;
	}

	public void setDefinitionProperty(String definitionProperty) {
		this.definitionProperty = definitionProperty;
	}

	public String getSynonymProperty() {
		return synonymProperty;
	}

	public void setSynonymProperty(String synonymProperty) {
		this.synonymProperty = synonymProperty;
	}

	public String getAuthorProperty() {
		return authorProperty;
	}

	public void setAuthorProperty(String authorProperty) {
		this.authorProperty = authorProperty;
	}

	public Object getHierarchyProperty() {
		return hierarchyProperty;
	}

	public void setHierarchyProperty(Object hierarchyProperty) {
		this.hierarchyProperty = hierarchyProperty;
	}

	public Object getObsoleteProperty() {
		return obsoleteProperty;
	}

	public void setObsoleteProperty(Object obsoleteProperty) {
		this.obsoleteProperty = obsoleteProperty;
	}

	public Object getObsoleteParent() {
		return obsoleteParent;
	}

	public void setObsoleteParent(Object obsoleteParent) {
		this.obsoleteParent = obsoleteParent;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getPublication() {
		return publication;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public String getURI() {
		return uRI;
	}

	public void setURI(String uRI) {
		this.uRI = uRI;
	}

	public List<String> getNaturalLanguage() {
		return naturalLanguage;
	}

	public void setNaturalLanguage(List<String> naturalLanguage) {
		this.naturalLanguage = naturalLanguage;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public Object getVersion() {
		return version;
	}

	public void setVersion(Object version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Contact> getContact() {
		return contact;
	}

	public void setContact(List<Contact> contact) {
		this.contact = contact;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public Integer getNumberOfClasses() {
		return numberOfClasses;
	}

	public void setNumberOfClasses(Integer numberOfClasses) {
		this.numberOfClasses = numberOfClasses;
	}

	public Integer getNumberOfIndividuals() {
		return numberOfIndividuals;
	}

	public void setNumberOfIndividuals(Integer numberOfIndividuals) {
		this.numberOfIndividuals = numberOfIndividuals;
	}

	public Integer getNumberOfProperties() {
		return numberOfProperties;
	}

	public void setNumberOfProperties(Integer numberOfProperties) {
		this.numberOfProperties = numberOfProperties;
	}

	public Integer getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(Integer maxDepth) {
		this.maxDepth = maxDepth;
	}

	public Integer getMaxChildCount() {
		return maxChildCount;
	}

	public void setMaxChildCount(Integer maxChildCount) {
		this.maxChildCount = maxChildCount;
	}

	public Integer getAverageChildCount() {
		return averageChildCount;
	}

	public void setAverageChildCount(Integer averageChildCount) {
		this.averageChildCount = averageChildCount;
	}

	public Integer getClassesWithOneChild() {
		return classesWithOneChild;
	}

	public void setClassesWithOneChild(Integer classesWithOneChild) {
		this.classesWithOneChild = classesWithOneChild;
	}

	public Integer getClassesWithMoreThan25Children() {
		return classesWithMoreThan25Children;
	}

	public void setClassesWithMoreThan25Children(Integer classesWithMoreThan25Children) {
		this.classesWithMoreThan25Children = classesWithMoreThan25Children;
	}

	public Integer getClassesWithNoDefinition() {
		return classesWithNoDefinition;
	}

	public void setClassesWithNoDefinition(Integer classesWithNoDefinition) {
		this.classesWithNoDefinition = classesWithNoDefinition;
	}

	public Object getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Object modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Object getEntities() {
		return entities;
	}

	public void setEntities(Object entities) {
		this.entities = entities;
	}

	public Object getNumberOfAxioms() {
		return numberOfAxioms;
	}

	public void setNumberOfAxioms(Object numberOfAxioms) {
		this.numberOfAxioms = numberOfAxioms;
	}

	public Object getKeyClasses() {
		return keyClasses;
	}

	public void setKeyClasses(Object keyClasses) {
		this.keyClasses = keyClasses;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Object getKnownUsage() {
		return knownUsage;
	}

	public void setKnownUsage(Object knownUsage) {
		this.knownUsage = knownUsage;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Object getConformsToKnowledgeRepresentationParadigm() {
		return conformsToKnowledgeRepresentationParadigm;
	}

	public void setConformsToKnowledgeRepresentationParadigm(Object conformsToKnowledgeRepresentationParadigm) {
		this.conformsToKnowledgeRepresentationParadigm = conformsToKnowledgeRepresentationParadigm;
	}

	public Object getHasContributor() {
		return hasContributor;
	}

	public void setHasContributor(Object hasContributor) {
		this.hasContributor = hasContributor;
	}

	public Object getHasCreator() {
		return hasCreator;
	}

	public void setHasCreator(Object hasCreator) {
		this.hasCreator = hasCreator;
	}

	public List<Object> getDesignedForOntologyTask() {
		return designedForOntologyTask;
	}

	public void setDesignedForOntologyTask(List<Object> designedForOntologyTask) {
		this.designedForOntologyTask = designedForOntologyTask;
	}

	public Object getWasGeneratedBy() {
		return wasGeneratedBy;
	}

	public void setWasGeneratedBy(Object wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
	}

	public Object getWasInvalidatedBy() {
		return wasInvalidatedBy;
	}

	public void setWasInvalidatedBy(Object wasInvalidatedBy) {
		this.wasInvalidatedBy = wasInvalidatedBy;
	}

	public Object getCuratedBy() {
		return curatedBy;
	}

	public void setCuratedBy(Object curatedBy) {
		this.curatedBy = curatedBy;
	}

	public List<Object> getEndorsedBy() {
		return endorsedBy;
	}

	public void setEndorsedBy(List<Object> endorsedBy) {
		this.endorsedBy = endorsedBy;
	}

	public Object getFundedBy() {
		return fundedBy;
	}

	public void setFundedBy(Object fundedBy) {
		this.fundedBy = fundedBy;
	}

	public Object getTranslator() {
		return translator;
	}

	public void setTranslator(Object translator) {
		this.translator = translator;
	}

	public String getHasDomain() {
		return hasDomain;
	}

	public void setHasDomain(String hasDomain) {
		this.hasDomain = hasDomain;
	}

	public String getHasFormalityLevel() {
		return hasFormalityLevel;
	}

	public void setHasFormalityLevel(String hasFormalityLevel) {
		this.hasFormalityLevel = hasFormalityLevel;
	}

	public String getHasLicense() {
		return hasLicense;
	}

	public void setHasLicense(String hasLicense) {
		this.hasLicense = hasLicense;
	}

	public String getHasOntologySyntax() {
		return hasOntologySyntax;
	}

	public void setHasOntologySyntax(String hasOntologySyntax) {
		this.hasOntologySyntax = hasOntologySyntax;
	}

	public String getIsOfType() {
		return isOfType;
	}

	public void setIsOfType(String isOfType) {
		this.isOfType = isOfType;
	}

	public Object getUsedOntologyEngineeringMethodology() {
		return usedOntologyEngineeringMethodology;
	}

	public void setUsedOntologyEngineeringMethodology(Object usedOntologyEngineeringMethodology) {
		this.usedOntologyEngineeringMethodology = usedOntologyEngineeringMethodology;
	}

	public String getUsedOntologyEngineeringTool() {
		return usedOntologyEngineeringTool;
	}

	public void setUsedOntologyEngineeringTool(String usedOntologyEngineeringTool) {
		this.usedOntologyEngineeringTool = usedOntologyEngineeringTool;
	}

	public List<String> getUseImports() {
		return useImports;
	}

	public void setUseImports(List<String> useImports) {
		this.useImports = useImports;
	}

	public Object getHasPriorVersion() {
		return hasPriorVersion;
	}

	public void setHasPriorVersion(Object hasPriorVersion) {
		this.hasPriorVersion = hasPriorVersion;
	}

	public List<Object> getIsBackwardCompatibleWith() {
		return isBackwardCompatibleWith;
	}

	public void setIsBackwardCompatibleWith(List<Object> isBackwardCompatibleWith) {
		this.isBackwardCompatibleWith = isBackwardCompatibleWith;
	}

	public List<Object> getIsIncompatibleWith() {
		return isIncompatibleWith;
	}

	public void setIsIncompatibleWith(List<Object> isIncompatibleWith) {
		this.isIncompatibleWith = isIncompatibleWith;
	}

	public Boolean getDeprecated() {
		return deprecated;
	}

	public void setDeprecated(Boolean deprecated) {
		this.deprecated = deprecated;
	}

	public Object getVersionIRI() {
		return versionIRI;
	}

	public void setVersionIRI(Object versionIRI) {
		this.versionIRI = versionIRI;
	}

	public List<Object> getOntologyRelatedTo() {
		return ontologyRelatedTo;
	}

	public void setOntologyRelatedTo(List<Object> ontologyRelatedTo) {
		this.ontologyRelatedTo = ontologyRelatedTo;
	}

	public List<Object> getComesFromTheSameDomain() {
		return comesFromTheSameDomain;
	}

	public void setComesFromTheSameDomain(List<Object> comesFromTheSameDomain) {
		this.comesFromTheSameDomain = comesFromTheSameDomain;
	}

	public List<Object> getSimilarTo() {
		return similarTo;
	}

	public void setSimilarTo(List<Object> similarTo) {
		this.similarTo = similarTo;
	}

	public List<Object> getIsAlignedTo() {
		return isAlignedTo;
	}

	public void setIsAlignedTo(List<Object> isAlignedTo) {
		this.isAlignedTo = isAlignedTo;
	}

	public Object getExplanationEvolution() {
		return explanationEvolution;
	}

	public void setExplanationEvolution(Object explanationEvolution) {
		this.explanationEvolution = explanationEvolution;
	}

	public Object getGeneralizes() {
		return generalizes;
	}

	public void setGeneralizes(Object generalizes) {
		this.generalizes = generalizes;
	}

	public Object getHasDisparateModelling() {
		return hasDisparateModelling;
	}

	public void setHasDisparateModelling(Object hasDisparateModelling) {
		this.hasDisparateModelling = hasDisparateModelling;
	}

	public Object getHiddenLabel() {
		return hiddenLabel;
	}

	public void setHiddenLabel(Object hiddenLabel) {
		this.hiddenLabel = hiddenLabel;
	}

	public Object getCoverage() {
		return coverage;
	}

	public void setCoverage(Object coverage) {
		this.coverage = coverage;
	}

	public Object getPublisher() {
		return publisher;
	}

	public void setPublisher(Object publisher) {
		this.publisher = publisher;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getAbstract() {
		return _abstract;
	}

	public void setAbstract(String _abstract) {
		this._abstract = _abstract;
	}

	public Object getAlternative() {
		return alternative;
	}

	public void setAlternative(Object alternative) {
		this.alternative = alternative;
	}

	public Object getHasPart() {
		return hasPart;
	}

	public void setHasPart(Object hasPart) {
		this.hasPart = hasPart;
	}

	public Object getIsFormatOf() {
		return isFormatOf;
	}

	public void setIsFormatOf(Object isFormatOf) {
		this.isFormatOf = isFormatOf;
	}

	public Object getHasFormat() {
		return hasFormat;
	}

	public void setHasFormat(Object hasFormat) {
		this.hasFormat = hasFormat;
	}

	public Object getAudience() {
		return audience;
	}

	public void setAudience(Object audience) {
		this.audience = audience;
	}

	public Object getValid() {
		return valid;
	}

	public void setValid(Object valid) {
		this.valid = valid;
	}

	public Object getAccrualMethod() {
		return accrualMethod;
	}

	public void setAccrualMethod(Object accrualMethod) {
		this.accrualMethod = accrualMethod;
	}

	public Object getAccrualPeriodicity() {
		return accrualPeriodicity;
	}

	public void setAccrualPeriodicity(Object accrualPeriodicity) {
		this.accrualPeriodicity = accrualPeriodicity;
	}

	public Object getAccrualPolicy() {
		return accrualPolicy;
	}

	public void setAccrualPolicy(Object accrualPolicy) {
		this.accrualPolicy = accrualPolicy;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getDataDump() {
		return dataDump;
	}

	public void setDataDump(String dataDump) {
		this.dataDump = dataDump;
	}

	public String getCsvDump() {
		return csvDump;
	}

	public void setCsvDump(String csvDump) {
		this.csvDump = csvDump;
	}

	public String getOpenSearchDescription() {
		return openSearchDescription;
	}

	public void setOpenSearchDescription(String openSearchDescription) {
		this.openSearchDescription = openSearchDescription;
	}

	public String getUriLookupEndpoint() {
		return uriLookupEndpoint;
	}

	public void setUriLookupEndpoint(String uriLookupEndpoint) {
		this.uriLookupEndpoint = uriLookupEndpoint;
	}

	public Object getUriRegexPattern() {
		return uriRegexPattern;
	}

	public void setUriRegexPattern(Object uriRegexPattern) {
		this.uriRegexPattern = uriRegexPattern;
	}

	public String getDepiction() {
		return depiction;
	}

	public void setDepiction(String depiction) {
		this.depiction = depiction;
	}

	public Object getLogo() {
		return logo;
	}

	public void setLogo(Object logo) {
		this.logo = logo;
	}

	public Object getCompetencyQuestion() {
		return competencyQuestion;
	}

	public void setCompetencyQuestion(Object competencyQuestion) {
		this.competencyQuestion = competencyQuestion;
	}

	public List<Object> getUsedBy() {
		return usedBy;
	}

	public void setUsedBy(List<Object> usedBy) {
		this.usedBy = usedBy;
	}

	public List<Object> getMetadataVoc() {
		return metadataVoc;
	}

	public void setMetadataVoc(List<Object> metadataVoc) {
		this.metadataVoc = metadataVoc;
	}

	public Object getHasDisjunctionsWith() {
		return hasDisjunctionsWith;
	}

	public void setHasDisjunctionsWith(Object hasDisjunctionsWith) {
		this.hasDisjunctionsWith = hasDisjunctionsWith;
	}

	public Object getToDoList() {
		return toDoList;
	}

	public void setToDoList(Object toDoList) {
		this.toDoList = toDoList;
	}

	public Object getExample() {
		return example;
	}

	public void setExample(Object example) {
		this.example = example;
	}

	public Object getPreferredNamespaceUri() {
		return preferredNamespaceUri;
	}

	public void setPreferredNamespaceUri(Object preferredNamespaceUri) {
		this.preferredNamespaceUri = preferredNamespaceUri;
	}

	public Object getPreferredNamespacePrefix() {
		return preferredNamespacePrefix;
	}

	public void setPreferredNamespacePrefix(Object preferredNamespacePrefix) {
		this.preferredNamespacePrefix = preferredNamespacePrefix;
	}

	public Object getMorePermissions() {
		return morePermissions;
	}

	public void setMorePermissions(Object morePermissions) {
		this.morePermissions = morePermissions;
	}

	public Object getUseGuidelines() {
		return useGuidelines;
	}

	public void setUseGuidelines(Object useGuidelines) {
		this.useGuidelines = useGuidelines;
	}

	public Object getCuratedOn() {
		return curatedOn;
	}

	public void setCuratedOn(Object curatedOn) {
		this.curatedOn = curatedOn;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getBugDatabase() {
		return bugDatabase;
	}

	public void setBugDatabase(String bugDatabase) {
		this.bugDatabase = bugDatabase;
	}

	public Object getMailingList() {
		return mailingList;
	}

	public void setMailingList(Object mailingList) {
		this.mailingList = mailingList;
	}

	public String getExampleIdentifier() {
		return exampleIdentifier;
	}

	public void setExampleIdentifier(String exampleIdentifier) {
		this.exampleIdentifier = exampleIdentifier;
	}

	public Object getAward() {
		return award;
	}

	public void setAward(Object award) {
		this.award = award;
	}

	public Object getCopyrightHolder() {
		return copyrightHolder;
	}

	public void setCopyrightHolder(Object copyrightHolder) {
		this.copyrightHolder = copyrightHolder;
	}

	public Object getAssociatedMedia() {
		return associatedMedia;
	}

	public void setAssociatedMedia(Object associatedMedia) {
		this.associatedMedia = associatedMedia;
	}

	public Object getWorkTranslation() {
		return workTranslation;
	}

	public void setWorkTranslation(Object workTranslation) {
		this.workTranslation = workTranslation;
	}

	public Object getTranslationOfWork() {
		return translationOfWork;
	}

	public void setTranslationOfWork(Object translationOfWork) {
		this.translationOfWork = translationOfWork;
	}

	public List<String> getIncludedInDataCatalog() {
		return includedInDataCatalog;
	}

	public void setIncludedInDataCatalog(List<String> includedInDataCatalog) {
		this.includedInDataCatalog = includedInDataCatalog;
	}

	public String getUploadFilePath() {
		return uploadFilePath;
	}

	public void setUploadFilePath(String uploadFilePath) {
		this.uploadFilePath = uploadFilePath;
	}

	public Object getDiffFilePath() {
		return diffFilePath;
	}

	public void setDiffFilePath(Object diffFilePath) {
		this.diffFilePath = diffFilePath;
	}

	public Object getMasterFileName() {
		return masterFileName;
	}

	public void setMasterFileName(Object masterFileName) {
		this.masterFileName = masterFileName;
	}

	public List<Object> getMissingImports() {
		return missingImports;
	}

	public void setMissingImports(List<Object> missingImports) {
		this.missingImports = missingImports;
	}

	public String getPullLocation() {
		return pullLocation;
	}

	public void setPullLocation(String pullLocation) {
		this.pullLocation = pullLocation;
	}

	public String getId3() {
		return id3;
	}

	public void setId3(String id3) {
		this.id3 = id3;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Links_ getLinks() {
		return links;
	}

	public void setLinks(Links_ links) {
		this.links = links;
	}

	public Context___ getContext() {
		return context;
	}

	public void setContext(Context___ context) {
		this.context = context;
	}

}