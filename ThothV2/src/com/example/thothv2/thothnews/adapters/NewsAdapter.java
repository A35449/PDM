package com.example.thothv2.thothnews.adapters;

import java.util.List;

import com.example.thothv2.thothnews.items.ThothNews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter{

	Context ctx;
	List<ThothNews> news;
	
	class ViewHolder 
	{
		public TextView tvTitle;
		public TextView tvDate;
	}
	
	public NewsAdapter(Context ctx, List<ThothNews> classes)
	{
		this.ctx = ctx;
		this.news = classes;
	}

	@Override
	public int getCount() {
		
		return news.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return news.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int i, View view, ViewGroup arg2) {
		if(view == null){
			view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_2, null);
			view.setTag(createViewHolder(view));
		}
		
		bindItem(news.get(i), view.getTag());
		return view;	
	}

	private void bindItem(ThothNews n, Object tag) {
		((ViewHolder)tag).tvTitle.setText(n.title);
		((ViewHolder)tag).tvDate.setText(parseDate(n.when.toString()));
	}

	private Object createViewHolder(View view) {
		ViewHolder vh = new ViewHolder();
		vh.tvTitle = (TextView)view.findViewById(android.R.id.text1);
		vh.tvDate = (TextView)view.findViewById(android.R.id.text2);
		
		return vh;
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
