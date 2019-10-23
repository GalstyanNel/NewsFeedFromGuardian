package com.example.nelly.newsfeedapp.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverter;

import com.example.nelly.newsfeedapp.models.Article;
import com.example.nelly.newsfeedapp.models.Fields;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article")
    LiveData<List<Article>> loadAllArticles();

    @Insert
    void insertArticle(Article article);

    @Query("DELETE FROM article WHERE id =:id")
    void delete(String id);

    @Query("SELECT * FROM article WHERE id = :id")
    LiveData<Article> loadArticleById(String id);



    class Converters {
        @TypeConverter
        public static Fields fromString(String value) {
            Type listType = new TypeToken<Fields>() {
            }.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String fromArrayList(Fields list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }
}
