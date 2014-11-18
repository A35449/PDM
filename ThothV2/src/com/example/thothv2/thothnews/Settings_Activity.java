package com.example.thothv2.thothnews;

/**
 * Created by Essen on 15-10-2014.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.adapters.SettingsAdapter;
import com.example.thothv2.thothnews.items.ThothClass;
import com.example.thothv2.thothnews.utils.SimpleJSONRetriever;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
    	sharedSpace = getApplicationContext().getSharedPreferences("turmas", MODE_PRIVATE);
        
        thothList = (ListView)findViewById(R.id.thothList);
        thothList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ThothInfo info = new ThothInfo() {
            @Override
            protected void onPostExecute(ThothClass[] result) {

                if(result != null) {
                    List<ThothClass> list = new LinkedList<ThothClass>();
                    for (ThothClass c : result)
                    {
                        if(c.semester.compareTo("1415i") == 0) /*HARDCODED*/
                            list.add(c);
                    }

                    adpt = new SettingsAdapter(Settings_Activity.this,list);

                    thothList.setAdapter(adpt);
                    restoreMarks(result);
                }
            }
        };
        
        button_update = (Button) findViewById(R.id.update_button);
        button_clean = (Button) findViewById(R.id.clean_button);
        button_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor;
		        editor = sharedSpace.edit();
		         
			    SparseBooleanArray checked = thothList.getCheckedItemPositions();
			    Set<String> selectedItems = new LinkedHashSet<String>();
			    
			    for (int i = 0; i < checked.size(); i++) {
			        // Item position in adapter
			        int position = checked.keyAt(i);
			        // Add sport if it is checked i.e.) == TRUE!
			        if (checked.valueAt(i)) {
			            ThothClass selected = (ThothClass)adpt.getItem(position);
			            selectedItems.add(Integer.toString(selected.id));
			        }
			    }
			    
			    editor.putStringSet("classes", selectedItems);
			    if(editor.commit())
			    	Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
			    else
			    	 Toast.makeText(getApplicationContext(), "Couldn't Save!", Toast.LENGTH_SHORT).show();
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
        
        info.execute();
    }

    class ThothInfo extends AsyncTask<Void, Void, ThothClass[]> {

        @Override
        protected ThothClass[] doInBackground(Void... voids) {
            try {
                SimpleJSONRetriever retriever = new SimpleJSONRetriever(ThothNewsMainActivity.BASE_URL);
                String data = retriever.getJSON(ThothClass.URI);
               
                return parseFrom(data);
            } catch (IOException e ) {
                return null;
            }catch (JSONException j) {
                return null;
            }
        }
            

        private ThothClass[] parseFrom(String s) throws JSONException {
            JSONObject root = new JSONObject(s);
            JSONArray classes = root.getJSONArray("classes");
            ThothClass[] info = new ThothClass[classes.length()];

            for (int i = 0; i < classes.length(); ++i) {
                JSONObject jsonclass = classes.getJSONObject(i);
                ThothClass clas = new ThothClass();
                clas.name = jsonclass.getString("fullName");
                clas.semester = jsonclass.getString("lectiveSemesterShortName");
                clas.id = jsonclass.getInt("id");
                info[i] = clas;
            }
            return info;
        }
    }
}

