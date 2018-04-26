package fr.lirmm.agroportal.ontologymappingharvester.entities.classquery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClassQuery {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("totalCount")
    @Expose
    private Integer totalCount;
    @SerializedName("prevPage")
    @Expose
    private Object prevPage;
    @SerializedName("nextPage")
    @Expose
    private Object nextPage;
    @SerializedName("links")
    @Expose
    private Links links;
    @SerializedName("collection")
    @Expose
    private List<Collection> collection = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Object prevPage) {
        this.prevPage = prevPage;
    }

    public Object getNextPage() {
        return nextPage;
    }

    public void setNextPage(Object nextPage) {
        this.nextPage = nextPage;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public List<Collection> getCollection() {
        return collection;
    }

    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

}