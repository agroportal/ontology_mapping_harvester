package fr.lirmm.agroportal.ontologymappingharvester.entities.mappingapi;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestMappingEntity {

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
    private Integer prevPage;
    @SerializedName("nextPage")
    @Expose
    private Integer nextPage;
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

    public Integer getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Integer prevPage) {
        this.prevPage = prevPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
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

    @Override
    public String toString() {
        return "RestMappingEntity{" +
                "page=" + page +
                ", pageCount=" + pageCount +
                ", totalCount=" + totalCount +
                ", prevPage=" + prevPage +
                ", nextPage=" + nextPage +
                ", links=" + links +
                ", collection=" + collection +
                '}';
    }
}


