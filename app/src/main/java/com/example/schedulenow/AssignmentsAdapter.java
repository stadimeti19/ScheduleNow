package com.example.schedulenow;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class AssignmentsAdapter extends
        RecyclerView.Adapter<AssignmentsAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dueDateTextView;
        public TextView classTextView;
        public Button deleteAssignmentButton;
        public Button editAssignmentButton;
        public ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.assignment_title);
            dueDateTextView = (TextView) itemView.findViewById(R.id.assignment_dueDate);
            classTextView = (TextView) itemView.findViewById(R.id.assignment_Class);
            deleteAssignmentButton = (Button) itemView.findViewById(R.id.deleteAssignmentButton);
            deleteAssignmentButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeAssignment(position);
                }
            });
            editAssignmentButton = (Button) itemView.findViewById(R.id.editAssignmentButton);
            editAssignmentButton.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Assignment assignment = assignments.get(position);
                    onEditAssignmentCallback.accept(assignment);
                }
            });
            progressBar = itemView.findViewById(R.id.assignment_progressBar);

        }
    }

    // member variable for the assignments
    private List<Assignment> assignments;
    private Context context;
    private Consumer<Void> onAssignmentRemovedCallback; // Callback to be invoked on assignment removal
    private Consumer<Assignment> onEditAssignmentCallback;
    private static final String PREFS_NAME = "AssignmentPrefs";
    private static final String KEY_ASSIGNMENTS_LIST = "assignmentsList";

    public AssignmentsAdapter(Context context, List<Assignment> assignments, Consumer<Void> onAssignmentRemovedCallback, Consumer<Assignment> onEditAssignmentCallback) {
        this.context = context;
        this.assignments = assignments;
        this.onAssignmentRemovedCallback = onAssignmentRemovedCallback;
        this.onEditAssignmentCallback = onEditAssignmentCallback;
    }

    @NonNull
    @Override
    public AssignmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View assignmentView = inflater.inflate(R.layout.item_assignment, parent, false);

        // return a new instance of the holder
        return new AssignmentsAdapter.ViewHolder(assignmentView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentsAdapter.ViewHolder holder, int position) {
        Assignment a = assignments.get(position);
        if (a != null) {
            TextView textView1 = holder.titleTextView;
            textView1.setText(a.getTitle());
            TextView textView2 = holder.dueDateTextView;
            textView2.setText(a.getDueDate());
            TextView textView3 = holder.classTextView;
            textView3.setText(a.getAClass());
            holder.progressBar.setProgress(a.getAssignmentProgress());
        }
    }

    private void removeAssignment(int position) {
        if (position < assignments.size() && position >= 0) {
            assignments.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, assignments.size());
            onAssignmentRemovedCallback.accept(null); // Notify the fragment
        }
    }

    // return total count of assignment items in the list
    @Override
    public int getItemCount() {
        return assignments.size();
    }

}
