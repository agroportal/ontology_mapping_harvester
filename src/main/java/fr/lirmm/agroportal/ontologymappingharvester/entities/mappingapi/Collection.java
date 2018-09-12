package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Collection implements Serializable
{

    @Override
    public String toString() {
        return "Collection{" +
                "id1='" + id1 + '\'' +
                ", source='" + source + '\'' +
                ", classes=" + classes.toString() +
                ", process=" + process +
                '}';
    }

    @SerializedName("id")
    @Expose
    private String id1;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("classes")
    @Expose
    private List<Class> classes = new ArrayList<Class>();
    @SerializedName("process")
    @Expose
    private Process process;


    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
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

}