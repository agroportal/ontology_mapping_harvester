package fr.lirmm.agroportal.ontologymappingharvester.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("submissions")
    @Expose
    private String submissions;
    @SerializedName("properties")
    @Expose
    private String properties;
    @SerializedName("classes")
    @Expose
    private String classes;
    @SerializedName("single_class")
    @Expose
    private String singleClass;
    @SerializedName("roots")
    @Expose
    private String roots;
    @SerializedName("instances")
    @Expose
    private String instances;
    @SerializedName("metrics")
    @Expose
    private String metrics;
    @SerializedName("reviews")
    @Expose
    private String reviews;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("groups")
    @Expose
    private String groups;
    @SerializedName("categories")
    @Expose
    private String categories;
    @SerializedName("latest_submission")
    @Expose
    private String latestSubmission;
    @SerializedName("projects")
    @Expose
    private String projects;
    @SerializedName("download")
    @Expose
    private String download;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("analytics")
    @Expose
    private String analytics;
    @SerializedName("ui")
    @Expose
    private String ui;
    @SerializedName("@context")
    @Expose
    private Context context;

    public String getSubmissions() {
        return submissions;
    }

    public void setSubmissions(String submissions) {
        this.submissions = submissions;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getSingleClass() {
        return singleClass;
    }

    public void setSingleClass(String singleClass) {
        this.singleClass = singleClass;
    }

    public String getRoots() {
        return roots;
    }

    public void setRoots(String roots) {
        this.roots = roots;
    }

    public String getInstances() {
        return instances;
    }

    public void setInstances(String instances) {
        this.instances = instances;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getLatestSubmission() {
        return latestSubmission;
    }

    public void setLatestSubmission(String latestSubmission) {
        this.latestSubmission = latestSubmission;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getAnalytics() {
        return analytics;
    }

    public void setAnalytics(String analytics) {
        this.analytics = analytics;
    }

    public String getUi() {
        return ui;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}