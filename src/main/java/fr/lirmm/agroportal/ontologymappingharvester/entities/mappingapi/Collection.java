package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Collection {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("classes")
    @Expose
    private List<Class> classes = null;
    @SerializedName("process")
    @Expose
    private Process process;
    @SerializedName("@id")
    @Expose
    private String id3;
    @SerializedName("@type")
    @Expose
    private String type3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("source", source).append("classes", classes).append("process", process).append("id3", id3).append("type3", type3).toString();
    }

}
