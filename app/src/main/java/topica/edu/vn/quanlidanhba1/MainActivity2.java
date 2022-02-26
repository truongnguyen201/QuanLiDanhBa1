package topica.edu.vn.quanlidanhba1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import topica.edu.vn.model.Contact;

public class MainActivity2 extends AppCompatActivity {
    Button btnGui;
    Intent intent;
    TextView txtNguoiNhan;
    EditText txtNoiDung;
    Contact contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        addControls();
        addEvents();

    }

    private void addEvents() {
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiGui();
            }
        });

    }

    private void xuLiGui() {
        final SmsManager smsManager=SmsManager.getDefault();
        Intent intent=new Intent(Intent.ACTION_SEND);
        final PendingIntent pendingIntent=
                PendingIntent.getBroadcast(this,0,intent,0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int result=getResultCode();
                String msg="send ok";
                if(result!= Activity.RESULT_OK)
                    msg="send fail";
                Toast.makeText(MainActivity2.this,msg,Toast.LENGTH_LONG).show();
            }
        },new IntentFilter(Intent.ACTION_SEND));
        smsManager.sendTextMessage(contact.getPhone(),null,txtNoiDung.getText().toString(),pendingIntent,null);
    }

    private void addControls() {
        btnGui=findViewById(R.id.btnGui);
        txtNguoiNhan=findViewById(R.id.txtNguoiNhan);
        txtNoiDung=findViewById(R.id.txtNoiDung);
        intent=getIntent();
        contact= (Contact) intent.getSerializableExtra("contact");
        txtNguoiNhan.setText(contact.getTen()+"-"+contact.getPhone());
    }
}
