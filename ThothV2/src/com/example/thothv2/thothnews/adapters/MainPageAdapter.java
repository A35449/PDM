package com.example.thothv2.thothnews.adapters;

import java.util.List;

import com.example.thothv2.thothnews.items.ThothClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainPageAdapter extends BaseAdapter{

	Context ctx;
	List<ThothClass> classes;
	
	class ViewHolder 
	{
		public TextView tvClasses;
	}
	
	public MainPageAdapter(Context ctx, List<ThothClass> classes)
	{
		this.ctx = ctx;
		this.classes = classes;
	}

	@Override
	public int getCount() {
		
		return classes.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return classes.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return arg0;
	}

	@Override
	public View getView(int i, View view, ViewGroup arg2) {
		if(view == null){
			view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_1, null);
			view.setTag(createViewHolder(view));
		}
		
		bindItem(classes.get(i), view.getTag());
		return view;	
	}

	private void bindItem(ThothClass c, Object tag) {
		((ViewHolder)tag).tvClasses.setText(c.name);
		
	}

	private Object createViewHolder(View view) {
		ViewHolder vh = new ViewHolder();
		vh.tvClasses = (TextView)view.findViewById(android.R.id.text1);
		
		return vh;
	}

	
	
}
