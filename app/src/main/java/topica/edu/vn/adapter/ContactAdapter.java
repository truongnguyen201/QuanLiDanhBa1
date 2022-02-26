package topica.edu.vn.adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import topica.edu.vn.model.Contact;
import topica.edu.vn.quanlidanhba1.MainActivity;
import topica.edu.vn.quanlidanhba1.MainActivity2;
import topica.edu.vn.quanlidanhba1.R;

import static topica.edu.vn.quanlidanhba1.MainActivity.database;


public class ContactAdapter extends ArrayAdapter<Contact> {
    Activity context; int resource; @NonNull List<Contact> objects;

    public ContactAdapter(@NonNull Activity context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(this.resource,null);
        TextView txtPhone=row.findViewById(R.id.txtPhone);
        TextView txtName=row.findViewById(R.id.txtTen);
        ImageButton btnCall=row.findViewById(R.id.btnCall);
        ImageButton btnSms=row.findViewById(R.id.btnSms);
        ImageButton btnDelete=row.findViewById(R.id.btnDelete);
        final Contact contact=this.objects.get(position);
        txtName.setText(contact.getTen());
        txtPhone.setText(contact.getPhone());
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuliGoiLuon(contact);
            }
        });
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiSms(contact);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLiDelete(contact);
            }
        });
        return row;
    }

    private void xuLiDelete(Contact contact) {
        //this.remove(contact);
        this.objects.remove(contact);
        database.delete("Contact","ma=?",new String[]{String.valueOf(contact.getMa())});
       this.notifyDataSetChanged();
    }

    private void xuLiSms(Contact contact) {
        Intent intent=new Intent(this.context, MainActivity2.class);
        intent.putExtra("contact",contact);
        this.context.startActivity(intent);
    }

    private void xuliGoiLuon(Contact contact) {
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri uri=Uri.parse("tel:" + contact.getPhone());
        intent.setData(uri);
        this.context.startActivity(intent);
    }
}
