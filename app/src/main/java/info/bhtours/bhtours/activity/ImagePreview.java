package info.bhtours.bhtours.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
/*import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;*/
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import info.bhtours.bhtours.R;
import info.bhtours.bhtours.model.ImageItem;
import info.bhtours.bhtours.util.TouchImageView;
import info.bhtours.bhtours.util.DataLoader;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImagePreview extends AppCompatActivity {

    private ArrayList<String> imageItems = new ArrayList<>();
    private ArrayList<String> imageTitles = new ArrayList<>();
    private ArrayList<ImageItem> imagesCache = new ArrayList<>();
    private int currentIndex = 0;
    DataLoader dataLoader;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Bitmap icon = imagesCache.get(currentIndex).getThumbImage();
                if (icon != null) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpg");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg"));
                    startActivity(Intent.createChooser(share, "Share Image"));
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        String title = getIntent().getStringExtra("locationName");
        setTitle(title);
        String imageUrl = getIntent().getStringExtra("imageUrl");

        imageItems = (ArrayList<String>) getIntent().getSerializableExtra("imageList");
        imageTitles = (ArrayList<String>) getIntent().getSerializableExtra("imageTitles");
        currentIndex = getIntent().getIntExtra("imageListSelectedIndex", 0);

        /*for (int i = 0; i < imageItems.size(); i++) {
            imagesCache.add(new ImageItem("",null,"",""));
        }*/

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.progressDialogMsg));
        dialog.show();
        LoadImage(imageUrl);
        //dataLoader = new DataLoader(this.getApplicationContext(), this);
        //dataLoader.getImageFromUrl(imageUrl);

        /*final GestureDetector gdt = new GestureDetector(new MySwipe());
        final TouchImageView imageView = (TouchImageView) findViewById(R.id.image);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gdt.onTouchEvent(event)) {
                    return false;
                }
                return true;
            }
        });*/
    }

    public boolean onSwipe(TouchImageView.Direction direction) {
        switch (direction) {
            case left:
                if (imageItems.size() - 1 > currentIndex) {
                    currentIndex++;
                    dialog.show();
                    LoadImage(imageItems.get(currentIndex));
                }
                break;
            case right:
                if (currentIndex != 0) {
                    currentIndex--;
                    dialog.show();
                    LoadImage(imageItems.get(currentIndex));
                }
                break;
        }
        return false;
    }

    private void LoadImage(String imageurl)
    {
        TouchImageView imageView = (TouchImageView) findViewById(R.id.image);
        imageView.setTarget(new CommonImageTarget(imageView));
        Picasso.with(this.getApplicationContext()).load(imageurl).into(imageView.getTarget());

        TextView titleTextView = (TextView) findViewById(R.id.title);
        setTitle(imageTitles.get(currentIndex));
        String imgRes = getResources().getString(R.string.imge_preview_image_number);
        titleTextView.setText(String.format(imgRes, currentIndex + 1, imageItems.size()));
        dialog.dismiss();
    }

    public class CommonImageTarget implements Target {
        private final TouchImageView imageView;

        public CommonImageTarget(final TouchImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            this.imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("@@@@@@@", "Failed! Bitmap could not downloaded.");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }
}
