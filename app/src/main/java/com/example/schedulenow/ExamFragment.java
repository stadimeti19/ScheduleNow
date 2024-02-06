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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulenow.databinding.FragmentExamBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ExamFragment extends Fragment {
    private FragmentExamBinding binding;
    private ArrayList<Exam> exams;
    private ExamsAdapter adapter;

    // you need a way to store and retrieve data, even if the app is closed and reopened
    // to do this, you can store the data as a JSON string in SharedPreferences
    private static final String PREFS_NAME = "ExamsPrefs";
    private static final String KEY_EXAMS_LIST = "examsList";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState
    ) {
        binding = FragmentExamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize exams and adapter member variables
        exams = new ArrayList<>();
        adapter = new ExamsAdapter(requireContext(), exams, unused -> saveExamPreferences(), this::showEditExamDialog);
        // Find the correct RecyclerView layout
        RecyclerView recyclerView = root.findViewById(R.id.rvExams);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadExamPreferences();

        adapter.notifyDataSetChanged();

        // Find the floating action button from the xml file
        com.google.android.material.floatingactionbutton.FloatingActionButton floatingButton =
                root.findViewById(R.id.floatingAddExams);

        floatingButton.setOnClickListener(view -> showDialogAddExam());

        return root;
    }

    // Populate dialog box for user to add exams
    @SuppressLint("NotifyDataSetChanged")
    private void showDialogAddExam() {
        // Create AlertDialog to pop up
        AlertDialog.Builder examBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.add_exam_dialog, null);
        examBuilder.setView(dialogView);
        AlertDialog dialog = examBuilder.create();

        // Find the views from the dialog class layout
        EditText editExamTitle = dialogView.findViewById(R.id.editExamTitle);
        EditText editExamDate = dialogView.findViewById(R.id.editExamDate);
        EditText editExamTime = dialogView.findViewById(R.id.editExamTime);
        EditText editExamLocation = dialogView.findViewById(R.id.editExamLocation);
        Button addExamButton = dialogView.findViewById(R.id.addExamButton);
        Button cancelExamButton = dialogView.findViewById(R.id.cancelExamButton);

        addExamButton.setOnClickListener(view -> {
            String examTitle = editExamTitle.getText().toString();
            String examDate = editExamDate.getText().toString();
            String examTime = editExamTime.getText().toString();
            String examLocation = editExamLocation.getText().toString();

            // check if the fields are not empty before creating new exam
            if (!examTitle.isEmpty() && !examDate.isEmpty() && !examTime.isEmpty() && !examLocation.isEmpty()) {
                exams.add(new Exam(examTitle, examDate, examTime, examLocation));
                saveExamPreferences();
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        // set up click listener for cancel button to dismiss dialog
        cancelExamButton.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showEditExamDialog(Exam examToEdit) {
        // Create AlertDialog to pop up
        AlertDialog.Builder examBuilder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.edit_exam_dialog, null);
        examBuilder.setView(dialogView);
        AlertDialog dialog = examBuilder.create();

        // Find the views from the dialog class layout
        EditText editExamTitle = dialogView.findViewById(R.id.editExamTitle);
        EditText editExamDate = dialogView.findViewById(R.id.editExamDate);
        EditText editExamTime = dialogView.findViewById(R.id.editExamTime);
        EditText editExamLocation = dialogView.findViewById(R.id.editExamLocation);
        Button saveExamButton = dialogView.findViewById(R.id.saveExamButton);
        Button cancelExamButton = dialogView.findViewById(R.id.cancelExamButton);

        // Set the initial values
        editExamTitle.setText(examToEdit.getTitle());
        editExamDate.setText(examToEdit.getDate());
        editExamTime.setText(examToEdit.getTime());
        editExamLocation.setText(examToEdit.getLocation());

        saveExamButton.setOnClickListener(view -> {
            // Retrieve edited values
            String updatedTitle = editExamTitle.getText().toString();
            String updatedDate = editExamDate.getText().toString();
            String updatedTime = editExamTime.getText().toString();
            String updatedLocation = editExamLocation.getText().toString();

            // Update the exam in the list
            examToEdit.setTitle(updatedTitle);
            examToEdit.setDate(updatedDate);
            examToEdit.setTime(updatedTime);
            examToEdit.setLocation(updatedLocation);

            // Save the updated exams list
            saveExamPreferences();

            // Notify the adapter of the change
            adapter.notifyDataSetChanged();

            // Dismiss the dialog
            dialog.dismiss();
        });

        // set up click listener for cancel button to dismiss dialog
        cancelExamButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void loadExamPreferences() {
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String examsJSON = preferences.getString(KEY_EXAMS_LIST, null);

        if (examsJSON != null) {
            Type listType = new TypeToken<List<Exam>>() {}.getType();
            List<Exam> loadedExams = new Gson().fromJson(examsJSON, listType);

            exams.clear();
            exams.addAll(loadedExams);
        }
    }

    private void saveExamPreferences(){
        SharedPreferences preferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String examsJSON = new Gson().toJson(exams);

        editor.putString(KEY_EXAMS_LIST, examsJSON);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

