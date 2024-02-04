package com.example.schedulenow;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        public Button deleteClassButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.class_name);
            timeTextView = (TextView) itemView.findViewById(R.id.class_time);
            instructorTextView = (TextView) itemView.findViewById(R.id.class_instructor);
            deleteClassButton = (Button) itemView.findViewById(R.id.deleteClassButton);

//            deleteClassButton.setOnClickListener(view -> removeClass(getAbsoluteAdapterPosition()));
        }
    }

    // member variable for the classes
    private List<Class> cClasses;
    private Context context;
    private static final String PREFS_NAME = "ClassPrefs";
    private static final String KEY_CLASSES_LIST = "classesList";

    public ClassesAdapter(Context context, List<Class> classes) {
        this.context = context;
        cClasses = classes;
    }

    @NonNull
    @Override
    public ClassesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // inflate the custom layout
        View classView = inflater.inflate(R.layout.item_class, parent, false);

        // return a new instance of the holder
        return new ViewHolder(classView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassesAdapter.ViewHolder holder, int position) {
        Class c = cClasses.get(position);

        TextView textView1 = holder.nameTextView;
        textView1.setText(c.getName());
        TextView textView2 = holder.timeTextView;
        textView2.setText(c.getTime());
        TextView textView3 = holder.instructorTextView;
        textView3.setText(c.getInstructor());
    }


//    private void removeClass(int position) {
//        if (position < cClasses.size() && position >= 0) {
//            cClasses.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, cClasses.size());
//            saveClassPreferences();
//        }
//    }

    // return total count of class items in the list
    @Override
    public int getItemCount() {
        return cClasses.size();
    }

}
