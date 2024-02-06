package com.example.schedulenow;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class ExamsAdapter extends
        RecyclerView.Adapter<ExamsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
        public TextView timeTextView;
        public TextView locationTextView;
        public Button deleteExamButton;
        public Button editExamButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.exam_title);
            dateTextView = (TextView) itemView.findViewById(R.id.exam_date);
            timeTextView = (TextView) itemView.findViewById(R.id.exam_time);
            locationTextView = (TextView) itemView.findViewById(R.id.exam_location);
            // set up listener for delete exam
            deleteExamButton = (Button) itemView.findViewById(R.id.deleteExamButton);
            deleteExamButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeExam(position);
                }
            });
            // set up listener for edit exam
            editExamButton = (Button) itemView.findViewById(R.id.editExamButton);
            editExamButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Exam exam = exams.get(position);
                    onEditExamCallback.accept(exam);
                }
            });

        }
    }

    // member variable for the exams
    private List<Exam> exams;
    private Context context;
    private Consumer<Void> onExamRemovedCallback; // Callback to be invoked on exam removal
    private Consumer<Exam> onEditExamCallback; // Callback to be invoked on exam edit
    private static final String PREFS_NAME = "ExamPrefs";
    private static final String KEY_EXAMS_LIST = "examsList";

    public ExamsAdapter(Context context, List<Exam> exams, Consumer<Void> onExamRemovedCallback, Consumer<Exam> onEditExamCallback) {
        this.context = context;
        this.exams = exams;
        this.onExamRemovedCallback = onExamRemovedCallback;
        this.onEditExamCallback = onEditExamCallback;
    }

    @NonNull
    @Override
    public ExamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View examView = inflater.inflate(R.layout.item_exam, parent, false);

        // return a new instance of the holder
        return new ExamsAdapter.ViewHolder(examView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamsAdapter.ViewHolder holder, int position) {
        Exam exam = exams.get(position);
        if (exam != null) {
            TextView textView1 = holder.titleTextView;
            textView1.setText(exam.getTitle());
            TextView textView2 = holder.dateTextView;
            textView2.setText(exam.getDate());
            TextView textView3 = holder.timeTextView;
            textView3.setText(exam.getTime());
            TextView textView4 = holder.locationTextView;
            textView4.setText(exam.getLocation());
        }
    }
    private void removeExam(int position) {
        if (position < exams.size() && position >= 0) {
            exams.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, exams.size());
            onExamRemovedCallback.accept(null); // Notify the fragment
        }
    }

    // return total count of exam items in the list
    @Override
    public int getItemCount() {
        return exams.size();
    }


}