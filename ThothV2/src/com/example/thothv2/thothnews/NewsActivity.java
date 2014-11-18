package com.example.thothv2.thothnews;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.adapters.NewsAdapter;
import com.example.thothv2.thothnews.items.ThothClass;
import com.example.thothv2.thothnews.items.ThothNews;
import com.example.thothv2.thothnews.utils.SimpleJSONRetriever;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class NewsActivity extends ListActivity {
	
	ListView list_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
	
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adp, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(), "Click!", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getBaseContext(), SingleNewActivity.class);
				startActivity(intent.putExtra("news_id",( (ThothNews)adp.getItemAtPosition(position)).id));
			}
		});
		
		int classId = getIntent().getIntExtra("classId", 0);
		
		if(classId == 0) return;
		
		new AsyncTask<Integer, Void, List<ThothNews>>(){

			@Override
			protected List<ThothNews> doInBackground(Integer... params) {
				
				try {
					SimpleJSONRetriever jr = new SimpleJSONRetriever(ThothNewsMainActivity.BASE_URL);
					
					return parseClassFromJSON(jr.getJSON("/"+ThothClass.URI+"/"+params[0].intValue()+"/newsitems"));
					
				} catch (JSONException e) {
					Log.d("NewsActivity.doInBackground", "JSONException");
				} catch (IOException e) {
					Log.d("SimpleJSONRetriever", "IOException");
				}
				
				return new LinkedList<ThothNews>();
			}
			
			private List<ThothNews> parseClassFromJSON(String json) throws JSONException
			{
				JSONObject root = new JSONObject(json);
				JSONArray news = root.getJSONArray("newsItems");
				ThothNews tn;
				List<ThothNews> newslist = new LinkedList<ThothNews>();
				
				for(int i = 0; i < news.length(); i++)
				{
					JSONObject newsitem = news.getJSONObject(i);
					tn= new ThothNews();
					
					tn.id = newsitem.getInt("id");
					tn.title = newsitem.getString("title");
					tn.when =newsitem.getString("when"); // should be date
					
					newslist.add(tn);
				}
				
				return newslist;
				
			}
			
			@Override
			protected void onPostExecute(List<ThothNews> news)
			{
				NewsAdapter adp = new NewsAdapter(NewsActivity.this, news);
		        setListAdapter(adp);
			}
			
        }.execute(Integer.valueOf(classId));
		
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
		if (id == R.id.thoth_menu_news_action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
