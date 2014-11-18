package com.example.thothv2.thothnews;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.thothv2.R;
import com.example.thothv2.thothnews.items.ThothClass;
import com.example.thothv2.thothnews.items.ThothNews;
import com.example.thothv2.thothnews.utils.SimpleJSONRetriever;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


class ViewHolder 
{
	public TextView tnHeader;
	public TextView tnDate;
	public TextView tnContent;
}

class NewsInfo extends AsyncTask<Integer, Void, ThothNews> {

	@Override
	protected ThothNews doInBackground(Integer ... params) {
	    SimpleJSONRetriever  retriever = new SimpleJSONRetriever(ThothNewsMainActivity.BASE_URL) ;
	    try {
	    	String uri = ThothNews.URI + "/" + Integer.toString(params[0]); //news_ID
	    	String json = retriever.getJSON(uri);
			ThothNews tn = parseNewsFromJSON(json);
			return tn;
			//bindItem(tn,initiateViewHolder());
		} catch (IOException e) {
			Log.d("SingleNewActivity.SimpleJsonRetriever", "IOException");
		} catch (JSONException e) {
			Log.d("SingleNewActivity.SimpleJsonRetriever", "JSONException");
		}   
		return null;
	}
	
	private ThothNews parseNewsFromJSON(String json) throws JSONException
	{
		JSONObject newsitem = new JSONObject(json);
		ThothNews tn = new ThothNews();
		
		tn.content = newsitem.getString("content").replace("<br>", "\n").replace("<p>", "").replace("</p>", "").replace("&nbsp;"," ");
		tn.id = newsitem.getInt("id");
		tn.title = newsitem.getString("title");	
		tn.when = parseDate(newsitem.getString("when"));
		
		return tn;
	}
	
	private String parseDate(String date){
		
		int year,month,day,hour,minute;
		String [] sp1 = date.split("-");
		year = Integer.parseInt(sp1[0]);
		month = Integer.parseInt(sp1[1]);
		String [] sp2 = sp1[2].split("T");
		day = Integer.parseInt(sp2[0]);
		String [] sp3 = sp2[1].split(":");
		hour = Integer.parseInt(sp3[0]);
		minute = Integer.parseInt(sp3[1]);
		return day + "/" + month + "/" + year + " " + hour + ":" + minute;
	}
}

public class SingleNewActivity extends Activity{
	
	 protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.news_layout);    
        
	    int newsId = getIntent().getIntExtra("news_id", 0);
		if(newsId == 0) return;
		
		NewsInfo newsinfo = new NewsInfo(){
			@Override
			protected void onPostExecute(ThothNews tn){
				bindItem(tn,initiateViewHolder());
			}
		};
		
		newsinfo.execute(newsId);
	 }
	 
	private ViewHolder initiateViewHolder() {
		ViewHolder vh = new ViewHolder();
		vh.tnContent = (TextView)findViewById(R.id.news_content);
		vh.tnDate = (TextView)findViewById(R.id.news_date);
		vh.tnHeader = (TextView)findViewById(R.id.news_header);
	
		return vh;
	}
	 
	private void bindItem(ThothNews n, ViewHolder vh) {
			vh.tnHeader.setText(n.title);
			vh.tnContent.setText(n.content);
			vh.tnDate.setText(n.when);	
	}
	 

}
