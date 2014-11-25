package com.example.thothv2.thothnews;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.adapters.MainPageAdapter;
import com.example.thothv2.thothnews.items.ThothClass;
import com.example.thothv2.thothnews.utils.SimpleJSONRetriever;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class ThothNewsMainActivity extends ListActivity {

	public static final String BASE_URL= "http://thoth.cc.e.ipl.pt/api/v1";
	
    @Override
    protected void onResume(){
    	 super.onResume();
    	 SharedPreferences prefs = getSharedPreferences("turmas", Context.MODE_PRIVATE);
         
         //if(!prefs.contains("classes"))
 	        callClassSelector();
         
         Collection<String> classes = prefs.getStringSet("classes", null);
         if(classes == null) return;
         Integer[] classesIds = parseToInt(classes);
         
         new AsyncTask<Integer, Void, List<ThothClass>>(){

 			@Override
 			protected List<ThothClass> doInBackground(Integer... params) {
 				
 				List<ThothClass> classes = new LinkedList<ThothClass>();
 				
 				try {
 					SimpleJSONRetriever jr = new SimpleJSONRetriever(BASE_URL);
 					
 					for(Integer i : params)
 						classes.add(parseClassFromJSON(jr.getJSON("/"+ThothClass.URI+"/"+i.intValue())));
 					
 				} catch (JSONException e) {
 					Log.d("doInBackground", "JSONException");
 				} catch (IOException e) {
 					Log.d("SimpleJSONRetriever", "IOException");
 				}
 				
 				return classes;
 			}
 			
 			private ThothClass parseClassFromJSON(String json) throws JSONException
 			{
 				JSONObject root = new JSONObject(json);
 				ThothClass tc= new ThothClass();
 				tc.name = root.getString("fullName");
 				tc.id = root.getInt("id");
 				
 				return tc;
 				
 			}
 			
 			@Override
 			protected void onPostExecute(List<ThothClass> classes)
 			{
 				MainPageAdapter adp = new MainPageAdapter(ThothNewsMainActivity.this, classes);
 		        setListAdapter(adp);
 			}
 			
         }.execute(classesIds);
         
    }
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoth);     
    }

	private Integer[] parseToInt(Collection<String> toParse) {
		Integer[] result = new Integer[toParse.size()];
		int i = 0;
		
		for(String s : toParse)
		{
			result[i] = Integer.valueOf(Integer.parseInt(s));
			i++;
		}
					
		return result;
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
        if (id == R.id.thoth_menu_main_action_settings) {
            callClassSelector();
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void callClassSelector() {
		Intent i = new Intent(this, Settings_Activity.class);
		startActivity(i);
		
	}
}
