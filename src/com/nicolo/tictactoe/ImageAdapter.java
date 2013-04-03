package com.nicolo.tictactoe;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton imageButton;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	imageButton = new ImageButton(mContext);
        	imageButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        	imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        	imageButton.setAdjustViewBounds(true);
        	imageButton.setMaxHeight(100);
        	imageButton.setMaxWidth(100);
            //imageView.setPadding(8, 8, 8, 8);
        } else {
        	imageButton = (ImageButton) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        imageButton.setImageResource(R.drawable.blank);
        return imageButton;
    }

   
    
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.o
    };
}