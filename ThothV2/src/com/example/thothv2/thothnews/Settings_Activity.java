package com.example.thothv2.thothnews;

/**
 * Created by Essen on 15-10-2014.
 */

import android.app.Activity;
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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Settings_Activity extends Activity {

    private ListView thothList;
    private SettingsAdapter adpt;
    private Button button_update, button_clean;
    private SharedPreferences sharedSpace;
   
    
    private boolean isSelected(int id, Integer[] selected_items){
    	
    	for(int i : selected_items){
    		if (i == id) return true;
    	}
    	return false;
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
    
    private void restoreMarks(ThothClass[] result){    
        
        Collection<String> classes;
        if((classes= sharedSpace.getStringSet("classes", null))== null) return;
        
        Integer[] selected_items = parseToInt(classes);
      
        
        for(int i = 0 ; i < result.length; i++)
        {
        	if(isSelected(result[i].id,selected_items))
        		thothList.setItemChecked(i,true);
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        thothList = (ListView)findViewById(R.id.thothList);
        thothList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
    	sharedSpace = getApplicationContext().getSharedPreferences("turmas", MODE_PRIVATE);
    	
    	ThothInfo getter = new ThothInfo() {
            @Override
            protected void onPostExecute(List<ThothClass> list) {

                adpt = new SettingsAdapter(Settings_Activity.this,list);
            	thothList.setAdapter(adpt);
                    
            }    	
		};
    	
    	if(!sharedSpace.contains("firstRun"))
    	{
    		sharedSpace.edit().putBoolean("firstRun", false).commit();
    		//query api for classes
    		getter.execute(false);
    	}
    	else
    	{
    		//query db for classes  
    		getter.execute(true);
    		
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
			    
		    	Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
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
    
    class ThothInfo extends AsyncTask<Boolean, Void, List<ThothClass>> {

        @Override
        protected List<ThothClass> doInBackground(Boolean... fromDb) {
            if(fromDb[0] == false)
            {
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
                    	getContentResolver().insert(Uri.parse("content://com.example.thothv2/classes"), extractValues(tc));
                        
    	            return list;
    	            
    	        } catch (IOException e ) {
    	            return null;
    	        }catch (JSONException j) {
    	            return null;
    	        }
            }
            else{
            	
            	Cursor c = getContentResolver().query(Uri.parse("content://com.example.thothv2/classes"), null, null, null, null);
            	return parseFrom(c);
            	
            }
            
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
    }
    
    

}

