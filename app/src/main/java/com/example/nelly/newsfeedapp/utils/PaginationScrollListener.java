package com.example.nelly.newsfeedapp.utils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private OnLoadMoreListener listener;
    private RecyclerView.LayoutManager manager;

    public PaginationScrollListener(StaggeredGridLayoutManager layoutManager, OnLoadMoreListener listener) {
        this.manager = layoutManager;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemsCount = manager.getChildCount();
        int totalItemsCount = manager.getItemCount();
        int firstVisibleItemPosition = 0;


        if (manager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) manager).findLastVisibleItemPositions(null);
            firstVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        }

        if (!listener.isLoading()) {
            int count = 10;
            if (visibleItemsCount + firstVisibleItemPosition >= totalItemsCount
                    && (firstVisibleItemPosition >= 0)
                    && totalItemsCount >= count) {
                listener.loadMoreItems();
            }
        }
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    public interface OnLoadMoreListener {
        void loadMoreItems();

        Boolean isLoading();
    }
}
