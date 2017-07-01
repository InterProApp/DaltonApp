package com.example.androidtouchedpixel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtouchedpixel.helpers.ColoresHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;

    private EditText et1;
    private static int RESULT_LOAD_IMAGE = 1;
    TextView touchedXY, invertedXY, imgSize, colorRGB;
    ImageView imgSource2;
    String cadena3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        touchedXY = (TextView) findViewById(R.id.xy);
        invertedXY = (TextView) findViewById(R.id.invertedxy);
        imgSize = (TextView) findViewById(R.id.size);
        colorRGB = (TextView) findViewById(R.id.colorrgb);
        imgSource2 = (ImageView) findViewById(R.id.source2);
        et1 = (EditText) findViewById(R.id.text);
        imgSource2.setOnTouchListener(imgSourceOnTouchListener);

    }

    public OnTouchListener imgSourceOnTouchListener
            = new OnTouchListener() {


        @Override
        public boolean onTouch(View view, MotionEvent event) {

            float eventX = event.getX();
            float eventY = event.getY();
            float[] eventXY = new float[]{eventX, eventY};

            Matrix invertMatrix = new Matrix();
            ((ImageView) view).getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);
            int x = (int) eventXY[0];
            int y = (int) eventXY[1];

            Drawable imgDrawable = ((ImageView) view).getDrawable();
            Bitmap bitmap = ((BitmapDrawable) imgDrawable).getBitmap();


            //Limit x, y range within bitmap
            if (x < 0) {
                x = 0;
            } else if (x > bitmap.getWidth() - 1) {
                x = bitmap.getWidth() - 1;
            }

            if (y < 0) {
                y = 0;
            } else if (y > bitmap.getHeight() - 1) {
                y = bitmap.getHeight() - 1;
            }

            colorRGB.setText("NombreColor: " + ColoresHelper.getInstance(getResources()).getNombreDelColorAproximado(bitmap.getPixel(x, y)));
            return true;
        }
    };


    public void Abre(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE) {
                //Get ImageURi and load with help of picasso
                //Uri selectedImageURI = data.getData();
                Picasso.with(MainActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
                        .into((ImageView) findViewById(R.id.source2));
            } else if (requestCode == CAMERA_REQUEST) {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File f = new File(getApplicationContext().getCacheDir(), imageFileName);

                try {
                    f.createNewFile();
                } catch (IOException e) {
//                    throw e;
                }

                //Convert bitmap to byte array
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Picasso.with(MainActivity.this).load(f).noPlaceholder().centerCrop().fit()
                        .into((ImageView) findViewById(R.id.source2));
            }
        }
    }

    public void tomarFoto(View v) throws IOException {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST);
    }
}
