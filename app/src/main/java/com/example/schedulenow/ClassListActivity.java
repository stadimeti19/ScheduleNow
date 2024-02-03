package com.example.schedulenow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity {
    ArrayList<Class> classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        RecyclerView rvClasses = (RecyclerView) findViewById(R.id.rvClasses);

        classes = Class.createClassesList(20);

        ClassesAdapter adapter = new ClassesAdapter(classes);

        rvClasses.setAdapter(adapter);

        rvClasses.setLayoutManager(new LinearLayoutManager(this));
    }
}
