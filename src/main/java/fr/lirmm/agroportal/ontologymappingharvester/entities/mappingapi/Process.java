package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Process {

    @SerializedName("id3")
    @Expose
    private String id3;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("relation")
    @Expose
    private List<String> relation = null;
    @SerializedName("source_contact_info")
    @Expose
    private Object sourceContactInfo;
    @SerializedName("source_name")
    @Expose
    private String sourceName;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getRelation() {
        return relation;
    }

    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    public Object getSourceContactInfo() {
        return sourceContactInfo;
    }

    public void setSourceContactInfo(Object sourceContactInfo) {
        this.sourceContactInfo = sourceContactInfo;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id3='" + id3 + '\'' +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", source='" + source + '\'' +
                ", relation=" + relation +
                ", sourceContactInfo=" + sourceContactInfo +
                ", sourceName='" + sourceName + '\'' +
                ", comment='" + comment + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}