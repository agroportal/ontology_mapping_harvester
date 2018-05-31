package fr.lirmm.agroportal.ontologymappingharvester.entities.identifiers;

import java.util.List;

public class IdentifierEntity {

    private String prefix;
    private String url;
    private String list;

    public IdentifierEntity(String prefix, List<String> synonyms, String url) {
        this.prefix = prefix;
        list = ">>>"+prefix.trim().toLowerCase();
        if(synonyms.size()>0) {
            for(String s: synonyms){
                list += ">>>"+s.trim().toLowerCase();
            }
        }
        this.url = url;
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

    public String findMatch(String value){
        if(list.indexOf(">>>"+value.trim().toLowerCase())>-1){
            return url;
        }else{
            return null;
        }
    }

}
