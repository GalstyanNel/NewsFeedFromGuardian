package com.example.nelly.newsfeedapp.view.activities;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nelly.newsfeedapp.R;
import com.example.nelly.newsfeedapp.callbacks.SendItemPositionListener;
import com.example.nelly.newsfeedapp.callbacks.SendItemPositionListenerHorizontal;
import com.example.nelly.newsfeedapp.models.Article;
import com.example.nelly.newsfeedapp.utils.PaginationScrollListener;
import com.example.nelly.newsfeedapp.view.activities.adapters.HorizontalRecyclerAdapter;
import com.example.nelly.newsfeedapp.view.activities.adapters.NewsAdapter;
import com.example.nelly.newsfeedapp.view.activities.presenter.ArticlePresenter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.nelly.newsfeedapp.utils.Constants.ARTICLE;
import static com.example.nelly.newsfeedapp.utils.Constants.CATEGORY_TRANSITION;
import static com.example.nelly.newsfeedapp.utils.Constants.IMAGE_TRANSITION;
import static com.example.nelly.newsfeedapp.utils.Constants.PAGE_START;
import static com.example.nelly.newsfeedapp.utils.Constants.TITLE_TRANSITION;

public class MainActivity extends AppCompatActivity implements ArticleContractView, SendItemPositionListener, SendItemPositionListenerHorizontal {

    private ArticlePresenter presenter;
    private ProgressDialog progressDialog;

    private NewsAdapter newsAdapter;
    private HorizontalRecyclerAdapter horizontalRecyclerAdapter;

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView horizontalRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initPresenter();

        initHorizontalRecyclerView();

        initArticleAdapter();
        progressDialog = new ProgressDialog(this);
        showProgress();

        initSwipeContainer();

        presenter.loadArticles(PAGE_START, false);
    }


    private void initArticleAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        newsAdapter = new NewsAdapter(this, presenter.listOfArticles);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(newsAdapter);
        recyclerView.scrollToPosition(1);

        recyclerView.setHasFixedSize(true);

        PaginationScrollListener listener = new PaginationScrollListener(manager, this.presenter);
        recyclerView.addOnScrollListener(listener);
    }

    private void initPresenter() {
        presenter = new ArticlePresenter(this);
        getLifecycle().addObserver(presenter);
        presenter.attachView(this);
    }

    private void initSwipeContainer() {
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(() -> {
            showProgress();
            Toast.makeText(MainActivity.this, R.string.news_refreshed, Toast.LENGTH_SHORT).show();
            swipeContainer.setRefreshing(false);
            presenter.loadArticles(PAGE_START, false);
        });
    }

    public void initHorizontalRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        horizontalRecycler = findViewById(R.id.horizontal_recyclerView);
        horizontalRecyclerAdapter = new HorizontalRecyclerAdapter(this, presenter.listOfFavouriteArticle);
        horizontalRecycler.setAdapter(horizontalRecyclerAdapter);
        horizontalRecycler.setLayoutManager(manager);
    }

    @Override
    public void updateAdapters() {
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressDialog.setMessage(getString(R.string.fetching_news));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void updateFavouritAdapter() {
        horizontalRecyclerAdapter.notifyDataSetChanged();
        if (presenter.listOfFavouriteArticle.isEmpty() && horizontalRecycler.getVisibility() == View.VISIBLE) {
            horizontalRecycler.setVisibility(View.GONE);
        } else if (!presenter.listOfFavouriteArticle.isEmpty() && horizontalRecycler.getVisibility() == View.GONE) {
            horizontalRecycler.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendItemPosition(int position, ImageView imageView, TextView category, TextView title) {
        if (position != RecyclerView.NO_POSITION) {
            Pair[] pairs = new Pair[3];
            pairs[0] = new Pair<View, String>(imageView, IMAGE_TRANSITION);
            pairs[1] = new Pair<View, String>(category, CATEGORY_TRANSITION);
            pairs[2] = new Pair<View, String>(title, TITLE_TRANSITION);
            goToTransactionActivity(position, pairs, presenter.listOfArticles);

        }
    }

    @Override
    public void sendItemPositionHorizontal(int position, CircleImageView image, TextView category) {
        if (position != RecyclerView.NO_POSITION) {
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View, String>(image, IMAGE_TRANSITION);
            pairs[1] = new Pair<View, String>(category, CATEGORY_TRANSITION);
            goToTransactionActivity(position, pairs, presenter.listOfFavouriteArticle);
        }
    }

    private void goToTransactionActivity(int position, Pair[] pairs, List<Article> listOfFavouriteArticle) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, pairs);
        Article article = listOfFavouriteArticle.get(position);
        Intent intent = new Intent(this, TransitionActivity.class);
        if (article != null) {
            intent.putExtra(ARTICLE, article);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, options.toBundle());
    }
}
