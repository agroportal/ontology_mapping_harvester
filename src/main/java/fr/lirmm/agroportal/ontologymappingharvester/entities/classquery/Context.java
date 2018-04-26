package fr.lirmm.agroportal.ontologymappingharvester.entities.classquery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Context {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("ontology")
    @Expose
    private String ontology;
    @SerializedName("children")
    @Expose
    private String children;
    @SerializedName("parents")
    @Expose
    private String parents;
    @SerializedName("descendants")
    @Expose
    private String descendants;
    @SerializedName("ancestors")
    @Expose
    private String ancestors;
    @SerializedName("instances")
    @Expose
    private String instances;
    @SerializedName("tree")
    @Expose
    private String tree;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("mappings")
    @Expose
    private String mappings;
    @SerializedName("ui")
    @Expose
    private String ui;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getDescendants() {
        return descendants;
    }

    public void setDescendants(String descendants) {
        this.descendants = descendants;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getInstances() {
        return instances;
    }

    public void setInstances(String instances) {
        this.instances = instances;
    }

    public String getTree() {
        return tree;
    }

    public void setTree(String tree) {
        this.tree = tree;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMappings() {
        return mappings;
    }

    public void setMappings(String mappings) {
        this.mappings = mappings;
    }

    public String getUi() {
        return ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

}