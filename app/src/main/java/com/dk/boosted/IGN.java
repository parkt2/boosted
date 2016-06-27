package com.dk.boosted;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class IGN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ign);
//        Drawable searchIcon = ContextCompat.getDrawable(this, R.drawable.search);
//        Bitmap bitmap = ((BitmapDrawable) searchIcon).getBitmap();
//        Drawable newSearchIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
//        EditText editName = (EditText) findViewById(R.id.edit_name);
//        editName.setCompoundDrawablesWithIntrinsicBounds(newSearchIcon, 0, 0, 0);
    }
}
