package com.example.thothv2.thothnews;

import java.util.LinkedList;
import java.util.List;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.adapters.MainPageAdapter;
import com.example.thothv2.thothnews.items.ThothClass;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class ThothNewsMainActivity extends ListActivity {

	public static final String BASE_URL= "http://thoth.cc.e.ipl.pt/api/v1";
	public static final String AUTHORITY = "com.example.thothv2";
	
    @Override
    protected void onResume(){
    	 super.onResume();
    	 
    	 new AsyncTask<Void, Void, List<ThothClass>>(){

			@Override
			protected List<ThothClass> doInBackground(Void... params) {

				Cursor c = getContentResolver().query(Uri.parse("content://"+ThothNewsMainActivity.AUTHORITY+"/classes"), null, "_selected = 1", null, null);
				
				return parseFrom(c);
			}
			
			private List<ThothClass> parseFrom(Cursor c)
            {
            	List<ThothClass> list = new LinkedList<ThothClass>();
            	ThothClass cls;
            	
            	while(c.moveToNext())
            	{
            		cls = new ThothClass();
            		cls.id = c.getInt(0);
            		cls.selected = c.getInt(1);
            		cls.name = c.getString(2);
            		cls.semester = c.getString(3);
            		list.add(cls);
            	}
            	
    			return list;
            	
            }
    		 
			@Override
 			protected void onPostExecute(List<ThothClass> classes)
 			{
				if(classes.size() > 0)
				{
	 				MainPageAdapter adp = new MainPageAdapter(ThothNewsMainActivity.this, classes);
	 		        setListAdapter(adp);
				}
				else
					callClassSelector();
 			}
    		 
    	 }.execute();
    	 
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoth);     
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent i = new Intent(this, NewsActivity.class);
		startActivity(i.putExtra("classId", ((ThothClass)l.getAdapter().getItem(position)).id));
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.thoth_menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //if (id == R.id.thoth_menu_main_action_settings) {
            callClassSelector();
        //}
        return super.onOptionsItemSelected(item);
    }
    
    private void callClassSelector() {
		Intent i = new Intent(this, Settings_Activity.class);
		startActivity(i);
		
	}
}
