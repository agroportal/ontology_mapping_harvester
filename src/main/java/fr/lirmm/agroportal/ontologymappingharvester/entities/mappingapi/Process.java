package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Process {

    @SerializedName("id")
    @Expose
    private String id2;
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
    private String sourceContactInfo;
    @SerializedName("source_name")
    @Expose
    private String sourceName;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("date")
    @Expose
    private String date;

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
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

    public String getSourceContactInfo() {
        return sourceContactInfo;
    }

    public void setSourceContactInfo(String sourceContactInfo) {
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
        return new ToStringBuilder(this).append("id2", id2).append("name", name).append("creator", creator).append("source", source).append("relation", relation).append("sourceContactInfo", sourceContactInfo).append("sourceName", sourceName).append("comment", comment).append("date", date).toString();
    }

}