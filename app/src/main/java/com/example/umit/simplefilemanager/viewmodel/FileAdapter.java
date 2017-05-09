package com.example.umit.simplefilemanager.viewmodel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.umit.simplefilemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> implements View.OnClickListener {

    private MainActivity activity;
    private Context context;
    private List<String> fileList;

    public FileAdapter(MainActivity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);
            this.textView = (TextView) v.findViewById(R.id.txt);
        }
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(fileList.get(position));
        holder.textView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void reload() {
        File currentFile = activity.getCurrentFile();
        ArrayList<String> fileList = new ArrayList<>();
        fileList.add("../");
        fileList.addAll(Arrays.asList(currentFile.list()));
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        CharSequence targetDirectory = ((TextView) v).getText();
        activity.changeCurrentDirectory(activity.getCurrentFile().getPath() + "/" + targetDirectory);
    }
}