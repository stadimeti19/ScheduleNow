package com.example.schedulenow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClassesAdapter extends
    RecyclerView.Adapter<ClassesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView timeTextView;
        public TextView instructorTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.class_name);
            timeTextView = (TextView) itemView.findViewById(R.id.class_time);
            instructorTextView = (TextView) itemView.findViewById(R.id.class_instructor);

        }
    }

    // member variable for the classes
    private List<Class> cClasses;

    public ClassesAdapter(List<Class> classes) {
        cClasses = classes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View classView = inflater.inflate(R.layout.item_class, parent, false);

        // return a new instance of the holder
        return new ViewHolder(classView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class c = cClasses.get(position);

        TextView textView1 = holder.nameTextView;
        textView1.setText(c.getName());
        TextView textView2 = holder.timeTextView;
        textView2.setText(c.getTime());
        TextView textView3 = holder.instructorTextView;
        textView3.setText(c.getInstructor());

    }

    // return total count of class items in the list
    @Override
    public int getItemCount() {
        return cClasses.size();
    }

}
