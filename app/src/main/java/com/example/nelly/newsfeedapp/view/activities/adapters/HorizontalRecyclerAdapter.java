package com.example.nelly.newsfeedapp.view.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.nelly.newsfeedapp.R;
import com.example.nelly.newsfeedapp.callbacks.SendItemPositionListenerHorizontal;
import com.example.nelly.newsfeedapp.models.Article;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Article> listOfArticles;
    private SendItemPositionListenerHorizontal sendItemPositionListenerHorizontal;

    public HorizontalRecyclerAdapter(Context context, List<Article> listOfArticles) {
        this.context = context;
        this.listOfArticles = listOfArticles;
        this.sendItemPositionListenerHorizontal = (SendItemPositionListenerHorizontal) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(context).inflate(R.layout.horizontal_recycler_view_item, parent, false);

        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (listOfArticles != null
                && !listOfArticles.isEmpty()
                && listOfArticles.get(position).getFields() != null) {
            Glide.with(context)
                    .load(listOfArticles.get(position).getFields().getThumbnail())
                    .into(holder.image);
            holder.circleText.setText(listOfArticles.get(position).getSectionName());
        }

    }

    @Override
    public int getItemCount() {
        return listOfArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView circleText;

        public ViewHolder(final View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.circleImage);
            circleText = itemView.findViewById(R.id.circleText);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                sendItemPositionListenerHorizontal.sendItemPositionHorizontal(position, image, circleText);

            });
        }
    }
}

