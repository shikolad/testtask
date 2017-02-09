package ru.mstoyan.shiko.testtask.Utils;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import ru.mstoyan.shiko.testtask.R;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This adapter is used to represent file list on device.
 * It can show both files and folder, or folders only.
 */
public class FileAdapter implements ListAdapter {

    private Context context;
    private String folder;
    private ArrayList<File> files;
    private ArrayList<DataSetObserver> observerArrayList;
    private int selectedIndex = -1;
    boolean foldersOnly =  false;

    public FileAdapter(Context context, String folder){
        this.context = context;
        files = new ArrayList<>();
        observerArrayList = new ArrayList<>();
        setFolder(folder);
    }

    public void setFolder(String folder){
        File file = new File(folder);
        if (file.exists()) {
            selectedIndex = -1;
            this.folder = folder;
            files.clear();
            File[] fileArray;

            if (foldersOnly){
                fileArray = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.isDirectory();
                    }
                });
            }else{
                fileArray = file.listFiles();
            }

            files.addAll(Arrays.asList(fileArray));
            notifyDataSetChanged();
        }
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        notifyDataSetChanged();
    }

    public File getSelectedFile(){
        if (selectedIndex == -1)
            return null;
        return files.get(selectedIndex);
    }

    public void setFoldersOnly(boolean foldersOnly){
        this.foldersOnly = foldersOnly;
        setFolder(folder);
    }

    public boolean getFoldersOnly(){
        return foldersOnly;
    }

    public String getFolder(){
        return folder;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        this.observerArrayList.add(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.observerArrayList.remove(observer);
    }

    public void notifyDataSetChanged(){
        for (DataSetObserver observer : observerArrayList){
            observer.onChanged();
        }
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.file_in_list,null);
        }
        ((TextView)convertView).setText(files.get(position).getName());
        if (selectedIndex == position)
            convertView.setBackgroundResource(R.color.active);
        else
            convertView.setBackgroundResource(R.color.not_active);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return files.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
