package com.apps.avichay.myeasyapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.avichay.myeasyapplication.R;
import com.squareup.picasso.Picasso;

public class ListAdapter extends CursorAdapter {
    private AdapterView.OnItemClickListener onItemClickListener;
    public  ListAdapter(Context context, Cursor cursor){super(context,cursor,0);

    }    public View newView(Context context, Cursor cursor, ViewGroup parent){

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);

        //attachroot : can overwrite the place of the view in the listview
    }
    public void bindView(View view, Context context,Cursor cursor){

        TextView tvName = view.findViewById(R.id.textView);
        TextView tvName2 =  view.findViewById(R.id.textView2);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView lLength = view.findViewById(R.id.locationLength);
        int name = cursor.getColumnIndex("address");
        int info  = cursor.getColumnIndex("location");
        int uri = cursor.getColumnIndex("uri");
        int locationLength = cursor.getColumnIndex("length");
        String url = cursor.getString(uri);
        String Info = cursor.getString(info);
        String Name = cursor.getString(name);
        String LocationLength = cursor.getString(locationLength);
        Picasso.get()
                .load(url)
                .into(imageView);
        //converting int from the ColumnIndex & getting the string out of it :)
        tvName.setText(Info);
        tvName2.setText(Name);
        lLength.setText(LocationLength);
        //setting the name & info to TextView

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

