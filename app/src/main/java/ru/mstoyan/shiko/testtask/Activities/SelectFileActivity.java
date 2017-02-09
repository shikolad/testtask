package ru.mstoyan.shiko.testtask.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import ru.mstoyan.shiko.testtask.R;
import ru.mstoyan.shiko.testtask.Utils.FileAdapter;

public class SelectFileActivity extends AppCompatActivity {

    public static final int GET_FILE_CODE = 0;
    private String currentDir = Environment.getExternalStorageDirectory().getPath();
    FileAdapter adapter;
    public static final String FILE_PATH = "file_path";
    public static final String DIRECTORY_ONLY = "directory_only";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        Intent intent = getIntent();

        ListView lstView = (ListView)findViewById(R.id.files);
        adapter = new FileAdapter(this,currentDir);
        adapter.setFoldersOnly(intent.getBooleanExtra(DIRECTORY_ONLY, false));

        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = (File)adapter.getItem(position);
                if (file.isDirectory()){
                    adapter.setFolder(file.getPath());
                }else{
                    adapter.setSelectedIndex(position);
                }
            }
        });

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getFoldersOnly()){
                    showEnterFileNameDialog();
                }else {
                    File file = adapter.getSelectedFile();
                    if (file == null)
                        return;
                    succeedExit(file.getPath());
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    /*
     * This is executed when we want to select output file location.
     * This function shows an alert dialog with request of file name
     */
    private void showEnterFileNameDialog(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.text_input_dialog,null);
        final EditText input = (EditText)dialogView.findViewById(R.id.input);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false)
                .setTitle(R.string.enter_file_name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = input.getText().toString();
                        if (fileName.length() == 0) {
                            Toast.makeText(SelectFileActivity.this,
                                    R.string.empty_file_name,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String filePath = adapter.getFolder() + "/" + fileName;
                            succeedExit(filePath);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /*
     * This is called when user selected input file or
     * entered output file name
     */
    public void succeedExit(String filePath){
        Intent result = new Intent();
        result.putExtra(FILE_PATH,filePath);
        setResult(RESULT_OK,result);
        finish();
    }

}
