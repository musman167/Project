package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_class extends RecyclerView.Adapter<Adapter_class.ViewHolder> {

    ArrayList<getter_setter_class> mlist;
    Context context;

    private String key ;
    private String task;
    private String description;
    private DatabaseReference reference;

    private String onlineUserID;
    private final FirebaseAuth mAuth ;
    private FirebaseUser mUser;



    public Adapter_class(ArrayList<getter_setter_class> mlist, Context context) {
        this.mlist = mlist;
        this.context = context;
        mAuth= FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Adapter_class.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_class.ViewHolder holder, int position) {
    getter_setter_class getter_setter_class_obj = mlist.get(position);
    holder.mDate.setText(getter_setter_class_obj.getDate());
    holder.mTask.setText(getter_setter_class_obj.getTask());
    holder.mDescription.setText(getter_setter_class_obj.getDescription());


    holder.linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            key= String.valueOf(getItemId(position));
            task=getter_setter_class_obj.getTask();
            description=getter_setter_class_obj.getDescription();
          updateData();
        }
    });
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDate, mTask, mDescription,mID;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mDate=itemView.findViewById(R.id.text_date_ret);
            mTask=itemView.findViewById(R.id.text_task_ret);
            mDescription=itemView.findViewById(R.id.text_description_ret);
            linearLayout=itemView.findViewById(R.id.LL2);
        }
    }

    private void updateData(){


        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_data, null);
        myDialog.setView(view);
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();
        reference=FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);
        final AlertDialog dialog = myDialog.create();

        final EditText mTask = view.findViewById(R.id.task_update);
        final EditText mDescription = view.findViewById(R.id.description_update);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        Button delButton = view.findViewById(R.id.delete_button_update);
        Button updateButton = view.findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delete_fun();

                task = mTask.getText().toString();
                description = mDescription.getText().toString();
                String date = DateFormat.getDateInstance().format(new Date());

                getter_setter_class model = new getter_setter_class( task, description, key, date);


               final FirebaseDatabase database = FirebaseDatabase.getInstance();
                reference.child(task).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()){
                                Toast.makeText(context, "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                            }else {
                               // String error = task.getException().toString();
                                Toast.makeText(context, "update failed ", Toast.LENGTH_SHORT).show();
                            }
                        dialog.dismiss();

                        }
                    });

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            final AlertDialog dialog = myDialog.create();
            @Override
            public void onClick(View v) {

                delete_fun();
                dialog.dismiss();

                }

        });

       dialog.show();

    }

    void delete_fun ()
    {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_data, null);
        AlertDialog dialog = myDialog.create();

        myDialog.setView(view);
        final EditText mTask = view.findViewById(R.id.task_update);
        final EditText mDescription = view.findViewById(R.id.description_update);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        task = mTask.getText().toString();
        description = mDescription.getText().toString();
        String date = DateFormat.getDateInstance().format(new Date());
        task = mTask.getText().toString().trim();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference.child(task).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show();

                } else {
                    //  String err = task.getException().toString();
                    Toast.makeText(context, "Failed to delete task ", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

        });
    }
}

