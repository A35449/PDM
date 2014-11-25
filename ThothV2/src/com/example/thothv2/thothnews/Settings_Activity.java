package com.example.thothv2.thothnews;

/**
 * Created by Essen on 15-10-2014.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.adapters.SettingsAdapter;
import com.example.thothv2.thothnews.items.ThothClass;
import com.example.thothv2.thothnews.utils.SimpleJSONRetriever;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Settings_Activity extends Activity {

    private ListView thothList;
    private SettingsAdapter adpt;
    private Button button_update, button_clean;
    private SharedPreferences sharedSpace;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        thothList = (ListView)findViewById(R.id.thothList);
        thothList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
    	sharedSpace = getApplicationContext().getSharedPreferences("turmas", MODE_PRIVATE);
    	
    	if(!sharedSpace.contains("firstRun"))
    	{
    		sharedSpace.edit().putBoolean("firstRun", false).commit();
    		//query api for classes
    		new ThothInfo().execute();
    	}
    	else
    	{
    		//query db for classes  
    		new ThothInfo() {
        		@Override
                protected List<ThothClass> doInBackground(Void... voids) {
                    
                    	Cursor c = getContentResolver().query(Uri.parse("content://"+ThothNewsMainActivity.AUTHORITY+"/classes"), null, null, null, null);
                    	return parseFrom(c);
                }  	
        		
        		@Override
        		protected void onPostExecute(List<ThothClass> list)
        		{
        			super.onPostExecute(list);
        			restoreMarks(list);
        		}
        		
        		private void restoreMarks(List<ThothClass> result){    
        	        
        	        for(int i = 0 ; i < result.size(); i++)
        	        {
        	        	if(result.get(i).selected > 0)
        	        		thothList.setItemChecked(i,true);
        	        }
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
    		}.execute();
    		
    	}
    	
        button_update = (Button) findViewById(R.id.update_button);
        button_clean = (Button) findViewById(R.id.clean_button);
        button_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		         
			    SparseBooleanArray checked = thothList.getCheckedItemPositions();
			    List<ThothClass> selectedItems = new LinkedList<ThothClass>();
			    
			    for (int i = 0; i < checked.size(); i++) {
			        // Item position in adapter
			        int position = checked.keyAt(i);
			        // Add sport if it is checked i.e.) == TRUE!
			        if (checked.valueAt(i)) {
			            selectedItems.add((ThothClass)adpt.getItem(position));
			        }
			    }
			    
			    //update _selected status
			    new AsyncTask<ThothClass, Void, Void>(){

					@Override
					protected Void doInBackground(ThothClass... selectedClasses) {
						
						
						ContentValues cv = new ContentValues();
						ContentResolver cr = getContentResolver();
						
						//reset entries to 'not selected' state (_selected = 0)
						cv.put("_selected", 0);
						cr.update(Uri.parse("content://"+ThothNewsMainActivity.AUTHORITY+"/classes"), cv, null, null);
						cv.remove("_selected");
						//update selected entries to 'selected' state (_selected = 1)
						String where = "";
						String[] selectionArgs = new String[selectedClasses.length];
						
						for(int i = 0; i < selectedClasses.length; i++)
						{
							if(i == selectedClasses.length - 1)
								where += "_id=?";
							else
								where += "_id=? OR ";
							
							selectionArgs[i] = Integer.toString(selectedClasses[i].id);
						}
						cv.put("_selected", 1);
						
						cr.update(Uri.parse("content://"+ThothNewsMainActivity.AUTHORITY+"/classes"), cv, where, selectionArgs);
						
						return null;
					}
			    	
					@Override
					protected void onPostExecute(Void voids)
					{
						Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
					}
			    }.execute(selectedItems.toArray(new ThothClass[0]));
			    
			    //TODO
		    	//fetch and save news for selected classes
			    //insert only if news_id not in db
			    
			}
		});
        
        button_clean.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor;
		        editor = sharedSpace.edit();
		        editor.clear();
		        editor.commit();
			}
		});
        
    }
    
    class ThothInfo extends AsyncTask<Void, Void, List<ThothClass>> {

        @Override
        protected List<ThothClass> doInBackground(Void... voids) {
        
	    	try {
	            SimpleJSONRetriever retriever = new SimpleJSONRetriever(ThothNewsMainActivity.BASE_URL);
	            String data = retriever.getJSON(ThothClass.URI);
	           
	            List<ThothClass> list = parseFrom(data);
	            
                for (ThothClass c : list)
                {
                    if(c.semester.compareTo("1415i") != 0) /*HARDCODED*/
                        list.remove(c);
                }
                
                for(ThothClass tc : list)
                	getContentResolver().insert(Uri.parse("content://"+ThothNewsMainActivity.AUTHORITY+"/classes"), extractValues(tc));
                    
	            return list;
	            
	        } catch (IOException e ) {
	            return null;
	        }catch (JSONException j) {
	            return null;
	        }
            
        }
        
        @Override
        protected void onPostExecute(List<ThothClass> list) {

            adpt = new SettingsAdapter(Settings_Activity.this,list);
        	thothList.setAdapter(adpt);
                
        }
            
        private List<ThothClass> parseFrom(String s) throws JSONException {
            JSONObject root = new JSONObject(s);
            JSONArray classes = root.getJSONArray("classes");
            List<ThothClass> info = new LinkedList<ThothClass>();

            for (int i = 0; i < classes.length(); ++i) {
                JSONObject jsonclass = classes.getJSONObject(i);
                ThothClass clas = new ThothClass();
                clas.name = jsonclass.getString("fullName");
                clas.semester = jsonclass.getString("lectiveSemesterShortName");
                clas.id = jsonclass.getInt("id");
                //clas.newsitemsURL = jsonclass.getJSONObject("_links").getString("newsItems");
                info.add(clas);
            }
            return info;
        }
        
        private ContentValues extractValues(ThothClass c)
        {
        	ContentValues cv = new ContentValues();
        	cv.put("_id", c.id);
        	cv.put("_selected", 0);
        	cv.put("name", c.name);
        	cv.put("semester", c.semester);
        	//cv.put("newsitemsURL", c.newsitemsURL);
        	
        	return cv;
        	
        }
        
    }
    
    

}

