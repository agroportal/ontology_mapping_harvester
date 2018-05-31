package fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Identifier implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pattern")
    @Expose
    private String pattern;
    @SerializedName("definition")
    @Expose
    private String definition;
    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("prefixed")
    @Expose
    private Integer prefixed;
    @SerializedName("synonyms")
    @Expose
    private List<String> synonyms = null;
    private final static long serialVersionUID = -2176558304465355588L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPrefixed() {
        return prefixed;
    }

    public void setPrefixed(Integer prefixed) {
        this.prefixed = prefixed;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

}