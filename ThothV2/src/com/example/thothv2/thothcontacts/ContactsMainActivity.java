package com.example.thothv2.thothcontacts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.thothv2.R;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;

public class ContactsMainActivity extends ListActivity {

	final static String[] days = {"7 Days","15 Days","30 Days"};
	final static int[] dayValue = {7, 15, 30};
	int selectedDays;
	//trololo
	Button not;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        /// trololol
        not=(Button)findViewById(R.id.button_notification);
        not.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			        /*Intent i = new Intent();
			        i.setAction("android.intent.action.ANNIVERSARY_CHECK");
			        getBaseContext().sendBroadcast(i);*/
				
			}
		});
        
        SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        selectedDays = sp.getInt("days", 0);
                
    }


    @Override
    public void onResume()
    {
    	super.onResume();
    	String[] projection = {Contacts.DISPLAY_NAME, Event.CONTACT_ID, Event.START_DATE,Contacts.PHOTO_THUMBNAIL_URI};
        
        String where = Data.MIMETYPE + "= ? AND " + Event.TYPE + "=" + Event.TYPE_BIRTHDAY;
        
        String[] selectionArgs = {Event.CONTENT_ITEM_TYPE};
        
        Cursor cursor = getContentResolver().query(Data.CONTENT_URI, projection, where, selectionArgs, null);
        		
		int nameCol = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
		int dateCol = cursor.getColumnIndex(Event.START_DATE);
		int thumbCol = cursor.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI);
		
		Calendar today = Calendar.getInstance();
		Calendar birthday = Calendar.getInstance();
		Calendar threshold = Calendar.getInstance();
		
		int month_threshold = threshold.get(Calendar.MONTH);
		if(month_threshold == 12)month_threshold = 1;
		else month_threshold++;
		
		threshold.set(Calendar.MONTH,month_threshold);
		threshold.add(Calendar.DAY_OF_MONTH, dayValue[selectedDays]);
				String[] date;
		
		List<Map<String,String>> contacts = new LinkedList<Map<String,String>>();
		
		while(cursor.moveToNext())
		{
			
			date = cursor.getString(dateCol).split("/");
			birthday.set(Calendar.MONTH, Integer.valueOf(date[1]));
			birthday.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[0]));
			
			if(birthday.after(today) && birthday.before(threshold))
			{
				Map<String,String> m = new HashMap<String,String>();
				m.put("name", cursor.getString(nameCol));
				m.put("date", cursor.getString(dateCol));
				m.put("thumb",cursor.getString(thumbCol));
				contacts.add(m);
			}
		}
		
		String[] properties = {"name", "date", "thumb"};
        int[] widgets = {R.id.contact_item_name,R.id.contact_item_date,R.id.contact_item_thumb};
        
        SimpleAdapter adp = new SimpleAdapter(this, contacts ,R.layout.contact_item_layout, properties, widgets);
        
        setListAdapter(adp);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contats_menu_main, menu);
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	
    	if(item.getItemId() == R.id.itemSettings)
    	{
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Birthdays in:").setSingleChoiceItems(days, selectedDays, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedDays = which;
				}
			}).setPositiveButton("Done", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
   
					SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
					sp.edit().putInt("days", selectedDays).commit();
					onResume();
               }});
    		
    		AlertDialog dialog = builder.create();
    		dialog.show();
    		
    		
    	}else if(item.getItemId() == R.id.itemEdit)
    	{
    		Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
    	    intent.setType(Phone.CONTENT_TYPE);
    	        
    	    startActivityForResult(intent,0);
    	}
    	
    	return super.onOptionsItemSelected(item);
    	
    }
    
    @Override
	protected void onActivityResult(int reqCode, int resCode, Intent data){
		if(resCode == RESULT_OK)
		{
			final Uri contact_uri = data.getData();
			final Calendar myCalendar = Calendar.getInstance();
			
			DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() { 
			
			        @Override
			        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			            myCalendar.set(Calendar.YEAR, year);
			            myCalendar.set(Calendar.MONTH, monthOfYear);
			            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			            insert_query_postProcess(contact_uri, myCalendar);
			        }
			   };
			   
			new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
			       myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				   	
		
		}

    }
    
    public void insert_query_postProcess(Uri contact_uri, Calendar myCalendar){
		
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String anniversary_date = sdf.format(myCalendar.getTime());
		
		//Obtain ContactRaw_ID
	    Cursor cursor = getContentResolver().query(contact_uri, null,null,null, null);
		cursor.moveToFirst();
		int rawContact_id = cursor.getColumnIndex(Data.RAW_CONTACT_ID);
		rawContact_id = cursor.getInt(rawContact_id);
		//Insert Anniversary
		ContentValues cv = new ContentValues();
		cv.put(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE);
		cv.put(Data.RAW_CONTACT_ID, rawContact_id);
		cv.put(Event.TYPE, Event.TYPE_BIRTHDAY);
		cv.put(Event.RAW_CONTACT_ID, rawContact_id);
		cv.put(Event.START_DATE, anniversary_date);
		getContentResolver().insert(Data.CONTENT_URI, cv);  
		
		onResume();

	}
    
}
