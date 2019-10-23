package com.example.nelly.newsfeedapp.callbacks;


import android.widget.ImageView;
import android.widget.TextView;

public interface SendItemPositionListener{
    void sendItemPosition (int position, ImageView imageView, TextView category, TextView title);
}
