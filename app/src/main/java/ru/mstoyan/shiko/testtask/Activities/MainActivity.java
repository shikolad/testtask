package ru.mstoyan.shiko.testtask.Activities;

import android.content.Intent;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;

import ru.mstoyan.shiko.testtask.R;
import ru.mstoyan.shiko.testtask.Utils.PathReader;
import ru.mstoyan.shiko.testtask.Utils.SVGReader;
import ru.mstoyan.shiko.testtask.Views.PathDrawer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onLoadImagePressed(View v){
        startActivityForResult(new Intent(this,SelectFileActivity.class),SelectFileActivity.GET_FILE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case (SelectFileActivity.GET_FILE_CODE):
                    fileReceived(data);
                    break;
                default:
                    break;
            }
        }
    }

    public void fileReceived(Intent data){
        String filePath = data.getStringExtra(SelectFileActivity.FILE_PATH);
        if (filePath==null)
            return; //// TODO: 09.02.2017 error
        SVGReader reader = new PathReader(filePath);
        Path path = new Path();
        try {
            path = reader.getPath();
            PathDrawer pathDrawer = (PathDrawer)findViewById(R.id.imageDrawer);
            pathDrawer.setPath(path);
        } catch (IOException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


}
