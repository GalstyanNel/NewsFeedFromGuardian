package com.example.nelly.newsfeedapp.view.activities.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nelly.newsfeedapp.R;
import com.example.nelly.newsfeedapp.callbacks.SendItemPositionListener;
import com.example.nelly.newsfeedapp.models.Article;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsView> {
    private Context context;
    private List<Article> listOfArticles;
    private SendItemPositionListener sendItemPositionListener;

    public NewsAdapter(Context context, List<Article> listOfArticle) {
        this.context = context;
        this.listOfArticles = listOfArticle;
        this.sendItemPositionListener = (SendItemPositionListener) context;
    }

    @NonNull
    @Override
    public NewsView onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
        NewsView newsView = new NewsView(layoutView);
        return newsView;
    }

    @Override
    public void onBindViewHolder(NewsView holder, int position) {
        holder.title.setText(listOfArticles.get(position).getWebTitle());
        holder.category.setText(listOfArticles.get(position).getSectionName());
        if (listOfArticles != null
                && !listOfArticles.isEmpty()
                && listOfArticles.get(position).getFields() != null) {
            Glide
                    .with(context.getApplicationContext())
                    .load(listOfArticles.get(position).getFields().getThumbnail())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.loading)
                            .fitCenter())
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return listOfArticles.size();

    }

    class NewsView extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView category, title;

        public NewsView(final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.articleImage);
            category = itemView.findViewById(R.id.articleCategory);
            title = itemView.findViewById(R.id.articleTitle);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                sendItemPositionListener.sendItemPosition(position, imageView, category, title);

            });

        }
    }

}

