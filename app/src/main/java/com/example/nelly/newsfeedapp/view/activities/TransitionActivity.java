package com.example.nelly.newsfeedapp.view.activities;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nelly.newsfeedapp.R;
import com.example.nelly.newsfeedapp.models.Article;
import com.example.nelly.newsfeedapp.utils.DBHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import static com.example.nelly.newsfeedapp.utils.Constants.ARTICLE;


public class TransitionActivity extends AppCompatActivity {
    private ImageView transitionImageVIew;
    private TextView transitionCategory, transitionTitle;
    private MaterialFavoriteButton favouriteButton;
    private DBHelper helper;
    private Article article;
    private boolean isExistInFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        this.setFinishOnTouchOutside(true);

        transitionImageVIew = findViewById(R.id.transitionImage);
        transitionCategory = findViewById(R.id.transitionCategory);
        transitionTitle = findViewById(R.id.transitionTitle);
        helper = new DBHelper(this);
        favouriteButton = findViewById(R.id.favouriteButton);

        if (getIntent().getExtras() != null) {

            article = (Article) getIntent().getSerializableExtra(ARTICLE);

            if (article != null && article.getFields() != null) {
                Glide.with(this.getApplicationContext())
                        .load(article.getFields().getThumbnail())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.loading)
                                .fitCenter())
                        .into(transitionImageVIew);
            }

            transitionTitle.setText(article.getWebTitle());
            transitionCategory.setText(article.getSectionName());
        }

        LiveData<Article> favoriteArticleDB = helper.getArticleById(article.getId());
        favoriteArticleDB.observe(this, article -> {
            isExistInFavorite = article != null;
            favouriteButton.setFavorite(isExistInFavorite);
        });

        favouriteButton.setOnClickListener(view -> {
            if (!isExistInFavorite) {
                article.setFavourite(true);
                helper.insertArticle(article);
                Toast.makeText(TransitionActivity.this, R.string.articel_pinned, Toast.LENGTH_SHORT).show();

            } else {
                helper.deleteFavouriteArticle(article.getId());
                Toast.makeText(TransitionActivity.this, R.string.article_deleted, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
