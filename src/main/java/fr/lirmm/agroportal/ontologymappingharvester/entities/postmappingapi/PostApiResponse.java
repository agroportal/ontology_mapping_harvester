package fr.lirmm.agroportal.ontologymappingharvester.entities.postmappingapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostApiResponse {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("classes")
    @Expose
    private List<Class> classes = null;
    @SerializedName("process")
    @Expose
    private Process process;
    @SerializedName("@id3")
    @Expose
    private String id3;
    @SerializedName("@type")
    @Expose
    private String type;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}