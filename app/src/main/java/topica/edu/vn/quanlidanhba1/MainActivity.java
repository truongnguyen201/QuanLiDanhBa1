package topica.edu.vn.quanlidanhba1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import topica.edu.vn.adapter.ContactAdapter;
import topica.edu.vn.model.Contact;


public class MainActivity extends AppCompatActivity {
    String DATABASE_NAME="cbContact.db";
    String DB_PATH_SUFFIX="/databases/";
    public static SQLiteDatabase database=null;


    ContactAdapter adapterContact;
    ListView lvContact;
    ArrayList<Contact>dsContact;

    Button btnLuu;
    EditText txtTen;
    EditText txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saochepCoSoDuLieuVaoHethongMobile();
        addControls();
        addEvents();
        showAllContactListView();
    }

    private void showAllContactListView() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor=database.query("Contact",null,null,
                null,null,null,null);
        //Cursor cursor1=database.rawQuery("select * from Contact where ma=",)
        dsContact.clear();
        while (cursor.moveToNext())
        {
            int ma=cursor.getInt(0);
            String ten=cursor.getString(1);
            String phone=cursor.getString(2);
            dsContact.add(new Contact(ma,ten,phone));
        }
        cursor.close();
        adapterContact.notifyDataSetChanged();
    }

    private void saochepCoSoDuLieuVaoHethongMobile() {
        File dbFile=getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists())
            try {
                saochepCoSoDuLieuTuAssets();
                Toast.makeText(this,"Thành công",Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
    }

    private void saochepCoSoDuLieuTuAssets() {
        try {
            InputStream myInput;
            myInput=getAssets().open(DATABASE_NAME);
            String outFileName=layduongLuuTru();
            File file=new File(getApplicationInfo().dataDir+DB_PATH_SUFFIX);
            if(!file.exists())
            {
                file.mkdir();
                OutputStream myOutput=new FileOutputStream(outFileName);

                byte [] buffer=new byte[1024];
                int length;
                while((length=myInput.read(buffer))>0)
                {
                    myOutput.write(buffer,0,length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }

        }
        catch (Exception e)
        {
            Log.e("Loi sao chep",e.toString());
        }
    }


    private String layduongLuuTru()
    {
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }


    private void addEvents() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiLuu();
            }
        });
    }

    private void xuLiLuu() {
        ContentValues row=new ContentValues();
        String ten=txtTen.getText().toString();
        String phone=txtPhone.getText().toString();
        if(ten.equals("") && phone.equals(""))
        {
            return;
        }else {
            row.put("Ten", ten);
            row.put("Phone", phone);
            long r = database.insert("Contact", null, row);
            adapterContact.notifyDataSetChanged();
            txtTen.setText("");
            txtPhone.setText("");
            txtTen.requestFocus();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            showAllContactListView();
        }
    }

    private void addControls() {
        btnLuu=findViewById(R.id.btnLuu);
        lvContact=findViewById(R.id.lvContact);
        dsContact=new ArrayList<Contact>();
        adapterContact=new ContactAdapter(MainActivity.this,R.layout.item,dsContact);
        lvContact.setAdapter(adapterContact);
        txtTen=findViewById(R.id.txtTen1);
        txtPhone=findViewById(R.id.txtPhone1);

    }

}