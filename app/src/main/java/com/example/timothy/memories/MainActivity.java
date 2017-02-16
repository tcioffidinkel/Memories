package com.example.timothy.memories;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Images.Thumbnails.IMAGE_ID;

/**
 * Created by Timothy on 12/12/2016.
 */
public class MainActivity extends Activity{
    FloatingActionButton addImage;
    ArrayList<Picture> imageArray = new ArrayList<Picture>();
    PictureAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    String msg = "Memories";
    ListView dataList;
    byte[] imageName;
    int imageId;
    Bitmap theImage;
    DataBaseHandler db;
    AlertDialog b;
    String m_Text;
    String m_location;
    TextView text;
    List<Picture> pictures;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = (ListView) findViewById(R.id.list);
        db = new DataBaseHandler(this);
        imageAdapter = new PictureAdapter(this, R.layout.screen_list,
                imageArray);
        pictures = imageAdapter.getAllPictures();
        for (Picture cn : pictures ) {
            imageArray.add(cn);

        }



        dataList.setAdapter(imageAdapter);

        final String[] option = new String[] {"Take from Camera"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (which == 0) {
                    callCamera();
                }


            }
        });
        final AlertDialog dialog = builder.create();

        addImage = (FloatingActionButton) findViewById(R.id.fab);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
                getLocationInput();
                getCaptionInput();
                imageAdapter.notifyDataSetChanged();


            }
        });

        dataList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPictureContent(imageArray, position, parent);
                imageAdapter.notifyDataSetChanged();
            }
        });

        imageAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = data.getExtras();

        Bitmap yourImage = extras.getParcelable("data");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        Log.d("Adding image", "Adding");
        imageAdapter.addPicture(new Picture(m_Text, m_location, imageInByte));
        imageAdapter.notifyDataSetChanged();
        Intent i = new Intent(MainActivity.this,
                MainActivity.class);
        startActivity(i);
        finish();

    }
    public void getCaptionInput()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Caption");
        dialogBuilder.setMessage("Enter Picture Caption Below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                m_Text = edt.getText().toString();
                imageAdapter.notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        b = dialogBuilder.create();
        imageAdapter.notifyDataSetChanged();
        b.show();
    }
    public void getLocationInput()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("Location");
        dialogBuilder.setMessage("Enter Location Information");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                m_location = edt.getText().toString();
                imageAdapter.notifyDataSetChanged();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        b = dialogBuilder.create();
        imageAdapter.notifyDataSetChanged();
        b.show();
    }
    public void getPictureContent(final ArrayList<Picture> imageArry, final int position, final AdapterView<?> parent)
    {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.screen_list, null);


        final ImageView edt = (ImageView) dialogView.findViewById(R.id.imgIcon);
        final TextView text = (TextView)  dialogView.findViewById(R.id.txtTitle);
        final TextView loc = (TextView) dialogView.findViewById(R.id.locTitle) ;
        edt.setImageBitmap(BitmapFactory.decodeByteArray(imageArry.get(position).getImage(), 0, imageArry.get(position).getImage().length));
        text.setText(imageArry.get(position).getName());
        loc.setText(imageArry.get(position).getLocation());

        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Image");
        dialogBuilder.setPositiveButton("Edit Image information", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getLocationInput();
                imageArry.get(position).setLocation(m_location);
                getCaptionInput();
                imageArry.get(position).setName(m_Text);
                text.setText(imageArry.get(position).getName());
                loc.setText(imageArry.get(position).getLocation());
                imageAdapter.notifyDataSetChanged();



            }
        });
        dialogBuilder.setNegativeButton("Delete Image", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                    imageAdapter.remove(imageArry.get(position).getID());
                    imageAdapter.remove(position);
                    imageAdapter.clear();
                    imageAdapter.notifyDataSetChanged();

            }
        });
        b = dialogBuilder.create();
        imageAdapter.notifyDataSetChanged();
        b.show();
    }

    public void callCamera()
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 200);
    }
    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (b != null) {
            b.dismiss();
            b = null;
        }
        Log.d(msg, "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (b != null) {
            b.dismiss();
            b = null;
        }

    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (b != null) {
            b.dismiss();
            b = null;
        }
        Log.d(msg, "The onDestroy() event");
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        super.onSaveInstanceState(savedInstanceState);
        Log.d(msg, "The onSaveInstanceState() event");
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(msg, "The onRestoreInstanceState() event: " + savedInstanceState.getInt("Count"));
    }



}
