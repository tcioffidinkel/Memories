package com.example.timothy.memories;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy on 12/12/2016.
 */

public class PictureAdapter extends ArrayAdapter<Picture> {

    Context context;
    int layoutResourceId;
    ArrayList<Picture> data= new ArrayList<>();
    SQLiteDatabase db;
    DataBaseHandler handler;
    private String[] allColumns = { handler.KEY_ID,
            handler.KEY_NAME, handler.KEY_LOC, handler.KEY_IMAGE };


    public PictureAdapter(Context context, int layoutResourceId, ArrayList<Picture> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        handler = new DataBaseHandler(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ImageHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ImageHolder();
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.locTitle = (TextView) row.findViewById(R.id.locTitle);
            row.setTag(holder);
        }
        else
        {
            holder = (ImageHolder)row.getTag();
        }
        Picture picture = data.get(position);
        holder.txtTitle.setText(picture.name);
        holder.locTitle.setText(picture.location);

        byte[] outImage=picture.image;
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap theImage = BitmapFactory.decodeStream(imageStream);
        holder.imgIcon.setImageBitmap(theImage);

        return row;

    }
    public Picture getPicture(int id) {
        db = handler.getWritableDatabase();
        Cursor cursor = db.query(handler.TABLE_PICTURE, new String[] { handler.KEY_ID,
                        handler.KEY_NAME, handler.KEY_LOC, handler.KEY_IMAGE }, handler.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Picture picture = new Picture(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getBlob(1));

        return picture;

    }

    public List<Picture> getAllPictures() {
        List<Picture> pictureList = new ArrayList<>();
        db = handler.getWritableDatabase();
        Cursor cursor = db.query(handler.TABLE_PICTURE, allColumns, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Picture picture = new Picture();
                picture.setID(Integer.parseInt(cursor.getString(0)));
                picture.setName(cursor.getString(1));
                picture.setLocation(cursor.getString(2));
                picture.setImage(cursor.getBlob(3));
                pictureList.add(picture);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return pictureList;
    }

    public void addPicture(Picture picture) {

        db = handler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(handler.KEY_NAME, picture.name);
        values.put(handler.KEY_LOC, picture.location);
        values.put(handler.KEY_IMAGE, picture.image);


        db.insert(handler.TABLE_PICTURE, null, values);

    }
    public boolean remove(int id)
    {
        db = handler.getWritableDatabase();
        return db.delete(handler.TABLE_PICTURE, handler.KEY_ID + "=" + id, null) > 0;
    }
    static class ImageHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView locTitle;
    }

}
