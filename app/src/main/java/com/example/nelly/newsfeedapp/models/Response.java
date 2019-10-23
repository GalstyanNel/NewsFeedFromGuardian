package com.example.nelly.newsfeedapp.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("userTier")
    private String userTier;
    @Expose
    @SerializedName("total")
    private Integer total;
    @Expose
    @SerializedName("startIndex")
    private Integer startIndex;
    @Expose
    @SerializedName("pageSize")
    private Integer pageSize;
    @Expose
    @SerializedName("currentPage")
    private Integer currentPage;
    @Expose
    @SerializedName("pages")
    private Integer pages;
    @Expose
    @SerializedName("orderBy")
    private String orderBy;
    @Expose
    @SerializedName("results")
    private ArrayList<Article> results;


    public Response(String status, String userTier, Integer total, Integer startIndex, Integer pageSize, Integer currentPage,
                    Integer pages, String orderBy, ArrayList<Article> results) {
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public ArrayList<Article> getResults() {
        return results;
    }

    public void setResults(ArrayList<Article> results) {
        this.results = results;
    }
}
