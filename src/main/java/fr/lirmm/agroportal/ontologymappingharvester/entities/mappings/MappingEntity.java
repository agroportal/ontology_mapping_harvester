package fr.lirmm.agroportal.ontologymappingharvester.entities.mappings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity to represent ontology JSON MAPPING
 */
public class MappingEntity implements Serializable {


    private int id;

    @Expose
    @SerializedName("creator")
    private String creator;
    @Expose
    @SerializedName("source_contact_info")
    private String sourceContactInfo;
    @Expose
    @SerializedName("relation")
    private String[] relation;
    @Expose
    @SerializedName("source")
    private String source;
    @Expose
    @SerializedName("source_name")
    private String sourceName;
    @Expose
    @SerializedName("comment")
    private String comment;
    @Expose
    @SerializedName("classes")
    private HashMap<String,String> classes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSourceContactInfo() {
        return sourceContactInfo;
    }

    public void setSourceContactInfo(String sourceContactInfo) {
        this.sourceContactInfo = sourceContactInfo;
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public HashMap<String, String> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, String> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return "MappingEntity{" +
                "id=" + id +
                ", creator='" + creator + '\'' +
                ", sourceContactInfo='" + sourceContactInfo + '\'' +
                ", relation=" + Arrays.toString(relation) +
                ", source='" + source + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", comment='" + comment + '\'' +
                ", classes=" + classes +
                '}';
    }

    public String getClassesFormated(){
        String begin="";
        String end="";

        String key="";
        String value="";

        for (Map.Entry<String, String> entry : classes.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            if(value.equalsIgnoreCase(sourceName)){
                begin = key+";";
            }else{
                end = key + ";";
            }
        }

        return begin + end;
    }

}
