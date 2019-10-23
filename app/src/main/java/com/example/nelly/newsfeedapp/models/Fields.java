package com.example.nelly.newsfeedapp.models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "fields")
public class Fields implements Serializable {
    @PrimaryKey (autoGenerate=true)
    @ColumnInfo (name = "id")
     private int id = 0;

    @SerializedName("thumbnail")
    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    public Fields(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
