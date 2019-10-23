package com.example.nelly.newsfeedapp.utils;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import com.example.nelly.newsfeedapp.database.NewsDatabase;
import com.example.nelly.newsfeedapp.models.Article;
import java.util.List;

public class DBHelper {

    private final NewsDatabase dataBase;

    public DBHelper(Context context) {
      dataBase =   NewsDatabase.getInstance(context);
    }

    public LiveData<List<Article>> getAllArticles() {
        return dataBase.articleDao().loadAllArticles();

    }

    public LiveData<Article> getArticleById(String id) {
        return dataBase.articleDao().loadArticleById(id);

    }

    public void insertArticle(final Article article) {
        new Thread(() -> dataBase.articleDao().insertArticle(article)).start();
    }

    public void deleteFavouriteArticle(String id) {
        new Thread(()->dataBase.articleDao().delete(id)).start();
    }
}
