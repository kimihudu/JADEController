package lkphan.btcontroller.jadecontroller.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import lkphan.btcontroller.jadecontroller.R;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class TestActivity extends AppCompatActivity {
    LinearLayout frameHolder;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final ArrayList<String> listImg = (ArrayList<String>) getIntent().getExtras().get("listImg");
        frameHolder = (LinearLayout) findViewById(R.id.frameHolder);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String file : listImg){
                    File fileDeleted = new File(file);
                    try {
                        fileDeleted.getCanonicalFile().delete();
                    }catch (Exception e){}
                }
                frameHolder.removeViews(0,listImg.size());
                Toast.makeText(getApplicationContext(),"deleted all",Toast.LENGTH_SHORT).show();
            }
        });


        printListImg(listImg);

    }

    private void printListImg(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView imgView = new ImageView(this);
            String filePath = list.get(i).toString();
            Bitmap bmp = BitmapFactory.decodeFile(filePath);
            imgView.setLayoutParams(lp);
            imgView.setImageBitmap(bmp);
            frameHolder.addView(imgView);
        }

    }
}
