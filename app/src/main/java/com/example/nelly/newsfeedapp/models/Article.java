package com.example.nelly.newsfeedapp.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "article")
public class Article implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;
    @SerializedName("sectionId")
    @ColumnInfo(name = "sectionId")
    private String sectionId;
    @SerializedName("sectionName")
    @ColumnInfo(name = "sectionName")
    private String sectionName;
    @SerializedName("webTitle")
    @ColumnInfo(name = "webTitle")
    private String webTitle;
    @SerializedName("fields")
    @ColumnInfo(name = "fields")
    private Fields fields;
    @SerializedName("isFavourite")
    @ColumnInfo(name = "isFavourite")
    private Boolean isFavourite;


    public Article(String webTitle, String sectionName, Fields fields) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.fields = fields;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}
