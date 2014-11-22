package com.example.thothv2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thothv2.provider.ClassesProvider;
import com.example.thothv2.thothnews.ThothNewsMainActivity;
import com.example.thothv2.thothcontacts.ContactsMainActivity;

public class MainActivity extends Activity {
	Button thoth,contact,add,del,query;
	Intent ti,ci;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thoth = (Button)findViewById(R.id.thoth_button);
        contact = (Button)findViewById(R.id.contact_button);
        add = (Button)findViewById(R.id.add_button);
        del = (Button)findViewById(R.id.delete_button);
        query = (Button)findViewById(R.id.query_button);
        
        thoth.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				Intent ti = new Intent(getBaseContext(),ThothNewsMainActivity.class);
				startActivity(ti);
				
			}
		});
        
        contact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent ti = new Intent(getBaseContext(),ContactsMainActivity.class);
				startActivity(ti);
				
			}
		});
        
        add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri class_uri = Uri.parse("content://com.example.thothv2/classes");
				ContentValues values = new ContentValues();
				//values.put("_id","1");
				values.put("_selected","0");
				values.put("name", "turma nova");
				
				ContentResolver cr = getContentResolver();
				//ClassesProvider cp = new ClassesProvider();
				cr.insert(class_uri, values);
			}
		});
        
        query.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri class_uri = Uri.parse("content://com.example.thothv2/classes");
				
				ContentResolver cr = getContentResolver();
				Cursor c = cr.query(class_uri, null, "_id = ?", new String[] {"1"}, null);
				c.moveToFirst();
				int idx = c.getColumnIndex("name");
				String s = c.getString(idx);
				Toast t = new Toast(getBaseContext()).makeText(getBaseContext(), s, Toast.LENGTH_LONG);
				t.show();
			}
		});
        
        del.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Uri class_uri = Uri.parse("content://com.example.thothv2/classes");
				
				ContentResolver cr = getContentResolver();
				//ClassesProvider cp = new ClassesProvider();
				cr.delete(class_uri, "_id = ?", new String[] {"1"});
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
