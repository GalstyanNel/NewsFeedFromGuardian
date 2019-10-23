package com.example.nelly.newsfeedapp.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.nelly.newsfeedapp.database.dao.ArticleDao;
import com.example.nelly.newsfeedapp.models.Article;
import com.example.nelly.newsfeedapp.models.Fields;

@Database(entities = {Article.class, Fields.class}, version = 1, exportSchema = false)
@TypeConverters({ArticleDao.Converters.class})
public abstract class NewsDatabase extends RoomDatabase {
    private static NewsDatabase instance;
    private static final String DATABASE_NAME = "newsDatabase";

    public static NewsDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (NewsDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), NewsDatabase.class, NewsDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return instance;
    }

    public abstract ArticleDao articleDao();

}
