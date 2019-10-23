package com.example.nelly.newsfeedapp.view.activities.presenter;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Handler;
import com.example.nelly.newsfeedapp.models.Article;
import com.example.nelly.newsfeedapp.models.NewsResponse;
import com.example.nelly.newsfeedapp.request.APIClient;
import com.example.nelly.newsfeedapp.request.RetrofitBuilder;
import com.example.nelly.newsfeedapp.utils.ConnectionHelper;
import com.example.nelly.newsfeedapp.utils.DBHelper;
import com.example.nelly.newsfeedapp.view.activities.ArticleContractView;
import com.example.nelly.newsfeedapp.utils.PaginationScrollListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.Callback;

import static com.example.nelly.newsfeedapp.utils.Constants.HEADER_ACCEPT;
import static com.example.nelly.newsfeedapp.utils.Constants.HEADER_KEY;
import static com.example.nelly.newsfeedapp.utils.Constants.PAGE_START;
import static com.example.nelly.newsfeedapp.utils.Constants.SHOW_FIELDS;
import static com.example.nelly.newsfeedapp.utils.Constants.UPDATE_SECOND;

public class ArticlePresenter implements PaginationScrollListener.OnLoadMoreListener, LifecycleObserver, Runnable {
    private final Handler handler;
    private final DBHelper helper;
    private Cache cache;
    private int currentPage = PAGE_START;
    private Boolean isLoading = true;
    public List<Article> listOfArticles;
    public List<Article> listOfFavouriteArticle;
    private ArticleContractView view;
    private Context context;

    public ArticlePresenter(Context context) {
        this.listOfArticles = new ArrayList<>();
        helper = new DBHelper(context);
        this.listOfFavouriteArticle = new ArrayList<>();
        this.context = context;
        setCache(context);
        handler = new Handler();
        getAllFavoriteLiveData();
    }

    private void setCache(Context context) {
        Long cacheSize = (long) (10 * 1024 * 1024);
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        cache = new Cache(httpCacheDirectory, cacheSize);
    }

    public void attachView(ArticleContractView view) {
        this.view = view;
    }

    public void loadArticles(final int currentPage, boolean updateOnlyNewArticle) {


        APIClient client = RetrofitBuilder.getRetrofitInstance(RetrofitBuilder.getOkHttp(context, cache)).create(APIClient.class);
        Call<NewsResponse> call = client.getAllNews(HEADER_KEY, HEADER_ACCEPT, SHOW_FIELDS, currentPage);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (updateOnlyNewArticle)
                        findNewArticle(response.body().getResponse().getResults());
                    else {
                        listOfArticles.addAll(response.body().getResponse().getResults());
                        view.updateAdapters();
                    }
                    isLoading = false;

                    view.hideProgress();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                view.hideProgress();
            }
        });
    }

    private void findNewArticle(List<Article> articles) {
        new Thread(() -> {
            for (int i = 0; i < articles.size(); i++) {
                Article newArticle = articles.get(i);
                Article oldArticle = listOfArticles.get(i);
                if (!newArticle.getId().equals(oldArticle.getId()))
                    listOfArticles.add(i, newArticle);
            }
            if(!((Activity)context).isDestroyed()){
                ((Activity)context).runOnUiThread(()->{
                    view.updateAdapters();
                });
            }
        }).start();

    }

    @Override
    public void loadMoreItems() {
        isLoading = true;
        currentPage++;
        loadArticles(currentPage, false);
    }

    @Override
    public Boolean isLoading() {
        return isLoading;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        updateArticlesEvery30Seconds();
    }
    private void getAllFavoriteLiveData() {
        LiveData<List<Article>> allArticleLiveData = helper.getAllArticles();
        allArticleLiveData.observe((LifecycleOwner) context, articles -> {
            listOfFavouriteArticle.clear();
            listOfFavouriteArticle.addAll(articles);
            view.updateFavouritAdapter();
        });
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        handler.removeCallbacks(this);
    }
    @Override
    public void run() {
        if(ConnectionHelper.hasNetwork(context)){
            loadArticles(PAGE_START,true);
            updateArticlesEvery30Seconds();
        }
    }

    private void updateArticlesEvery30Seconds() {
        handler.postDelayed(this, UPDATE_SECOND);// check for new items in the feed and add them to the list every 30 seconds.
    }
}
