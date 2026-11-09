
package com.example.cityparkexplorer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText searchBox;
    private ListView parkList;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> parks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = findViewById(R.id.searchBox);
        parkList = findViewById(R.id.parkList);

        loadParks();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                parks
        );

        parkList.setAdapter(adapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadParks() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        getAssets().open("parks.csv")))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(",", -1);

                if (columns.length >= 4) {
                    String parkName = columns[1].trim();
                    String address = columns[2].trim();
                    String city = columns[3].trim();

                    parks.add(parkName + "\n" + address + ", " + city);
                }
            }

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Error loading parks: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}
