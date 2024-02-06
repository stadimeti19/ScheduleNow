package com.example.schedulenow;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulenow.databinding.FragmentClassBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends Fragment {

    private FragmentClassBinding binding;
    private ArrayList<Class> classes;
    private ClassesAdapter adapter;

    // you need a way to store and retrieve data, even if the app is closed and reopened
    // to do this, you can store the data as a JSON string in SharedPreferences
    private static final String PREFS_NAME = "ClassPrefs";
    private static final String KEY_CLASSES_LIST = "classesList";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState
    ) {
        binding = FragmentClassBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize classes and adapter member variables
        classes = new ArrayList<>();
        adapter = new ClassesAdapter(requireContext(), classes, unused -> saveClassPreferences(), this::showEditClassDialog);

        // Find the correct RecyclerView layout
        RecyclerView recyclerView = root.findViewById(R.id.rvClasses);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadClassesPreferences();

        adapter.notifyDataSetChanged();

        // Find the floating action button from the xml file
        com.google.android.material.floatingactionbutton.FloatingActionButton floatingButton =
                root.findViewById(R.id.floatingAddClasses);

        floatingButton.setOnClickListener(view -> showDialogAddClass());

        return root;
    }

    // Populate dialog box for user to add classes
    @SuppressLint("NotifyDataSetChanged")
    private void showDialogAddClass() {
        // Create AlertDialog to pop up
        AlertDialog.Builder classBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_class_dialog, null);
        classBuilder.setView(dialogView);
        AlertDialog dialog = classBuilder.create();

        // Find the views from the dialog class layout
        EditText editClassName = dialogView.findViewById(R.id.editClassName);
        EditText editClassTime = dialogView.findViewById(R.id.editClassTime);
        EditText editClassInstructor = dialogView.findViewById(R.id.editClassInstructor);
        Button addClassButton = dialogView.findViewById(R.id.addClassButton);
        Button cancelClassButton = dialogView.findViewById(R.id.cancelClassButton);

        addClassButton.setOnClickListener(view -> {
            String className = editClassName.getText().toString();
            String classTime = editClassTime.getText().toString();
            String classInstructor = editClassInstructor.getText().toString();

            // check if the fields are not empty before creating new class
            if (!className.isEmpty() && !classInstructor.isEmpty() && !classTime.isEmpty()) {
                classes.add(new Class(className, classTime, classInstructor));
                saveClassPreferences();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // set up click listener for cancel button to dismiss dialog
        cancelClassButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showEditClassDialog(Class classToEdit) {
        // Create AlertDialog to pop up
        AlertDialog.Builder classBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_class_dialog, null);
        classBuilder.setView(dialogView);
        AlertDialog dialog = classBuilder.create();

        // Find the views from the dialog class layout
        EditText editClassName = dialogView.findViewById(R.id.editClassName);
        EditText editClassTime = dialogView.findViewById(R.id.editClassTime);
        EditText editClassInstructor = dialogView.findViewById(R.id.editClassInstructor);
        Button saveClassButton = dialogView.findViewById(R.id.saveClassButton);
        Button cancelClassButton = dialogView.findViewById(R.id.cancelClassButton);

        // Set the initial values
        editClassName.setText(classToEdit.getName());
        editClassTime.setText(classToEdit.getTime());
        editClassInstructor.setText(classToEdit.getInstructor());

        saveClassButton.setOnClickListener(view -> {
            // Retrieve edited values
            String updatedName = editClassName.getText().toString();
            String updatedTime = editClassTime.getText().toString();
            String updatedInstructor = editClassInstructor.getText().toString();

            // Update the assignment in the list
            classToEdit.setcName(updatedName);
            classToEdit.setcTime(updatedTime);
            classToEdit.setcInstructor(updatedInstructor);

            // Save the updated assignments list
            saveClassPreferences();

            // Notify the adapter of the change
            adapter.notifyDataSetChanged();

            // Dismiss the dialog
            dialog.dismiss();
        });

        // set up click listener for cancel button to dismiss dialog
        cancelClassButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void loadClassesPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String classesJSON = preferences.getString(KEY_CLASSES_LIST, null);

        if (classesJSON != null) {
            Type listType = new TypeToken<List<Class>>() {}.getType();
            List<Class> loadedClasses = new Gson().fromJson(classesJSON, listType);

            classes.clear();
            classes.addAll(loadedClasses);
        }
    }

    private void saveClassPreferences(){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String classesJSON = new Gson().toJson(classes);

        editor.putString(KEY_CLASSES_LIST, classesJSON);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}