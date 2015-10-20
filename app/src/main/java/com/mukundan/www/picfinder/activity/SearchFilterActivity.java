package com.mukundan.www.picfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mukundan.www.picfinder.R;

import java.util.Arrays;

public class SearchFilterActivity extends ActionBarActivity {

    Spinner etImageSize;
    Spinner etColorFilter;
    Spinner etImageType;
    EditText etSiteFilter;
    Button btnSave;
    String[] imageSizes;
    String[] colorFilters;
    String[] imageTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        imageSizes = getResources().getStringArray(R.array.image_size);
        colorFilters = getResources().getStringArray(R.array.color_filter);
        imageTypes = getResources().getStringArray(R.array.image_type);
        setupViews();
        Intent i = getIntent();
        etImageSize.setSelection(Arrays.asList(imageSizes).indexOf(i.getStringExtra("image_size")));
        etColorFilter.setSelection(Arrays.asList(imageSizes).indexOf(i.getStringExtra("color_filter")));
        etImageType.setSelection(Arrays.asList(imageSizes).indexOf(i.getStringExtra("image_type")));
    }

    private void setupViews() {
        this.etColorFilter = (Spinner) findViewById(R.id.etColorFilter);
        ArrayAdapter<CharSequence> aColorFilter = ArrayAdapter.createFromResource(this,
                R.array.color_filter, android.R.layout.simple_spinner_item);
        aColorFilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etColorFilter.setAdapter(aColorFilter);

        this.etImageSize = (Spinner) findViewById(R.id.etImageSize);
        ArrayAdapter<CharSequence> aImageSize = ArrayAdapter.createFromResource(this,
                R.array.image_size, android.R.layout.simple_spinner_item);
        aImageSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etColorFilter.setAdapter(aImageSize);

        this.etImageType = (Spinner) findViewById(R.id.etImageType);
        ArrayAdapter<CharSequence> aImageType = ArrayAdapter.createFromResource(this,
                R.array.image_type, android.R.layout.simple_spinner_item);
        aImageType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etColorFilter.setAdapter(aImageType);

        this.etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

        this.btnSave = (Button) findViewById(R.id.btnSave);
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("image_size", "all"); //etImageSize.getSelectedItem().toString());
                data.putExtra("image_type", "all"); //etImageType.getSelectedItem().toString());
                data.putExtra("color_filter", etColorFilter.getSelectedItem().toString());
                data.putExtra("site_filter", etSiteFilter.getText().toString());
                data.putExtra("code", 200); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish();
            }
        });
    }

}
