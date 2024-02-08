package com.im_oregano007.to_doapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.im_oregano007.to_doapp.AddNewTask;
import com.im_oregano007.to_doapp.MainActivity;
import com.im_oregano007.to_doapp.Model.ToDoModel;
import com.im_oregano007.to_doapp.R;
import com.im_oregano007.to_doapp.Utils.DatabaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity mainActivity;
    private DatabaseHelper myDb;
    public ToDoAdapter(MainActivity mainActivity, DatabaseHelper myDb){
        this.mainActivity = mainActivity;
        this.myDb = myDb;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ToDoModel item = mList.get(position);
        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDb.updateStatus(item.getId(),1);
                } else {
                    myDb.updateStatus(item.getId(),0);
                }
            }
        });
    }

    public Context getContext(){
        return mainActivity;
    }

    public void setTask(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position){
        ToDoModel item = mList.get(position);
        myDb.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);

    }

    public void editTask(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(mainActivity.getSupportFragmentManager(), task.getTag());


    }


    public boolean toBoolean(int num){
        return num!=0;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkBox);
        }
    }


}
