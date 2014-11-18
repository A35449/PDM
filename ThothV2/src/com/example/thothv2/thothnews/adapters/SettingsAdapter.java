package com.example.thothv2.thothnews.adapters;

import java.util.List;

import com.example.thothv2.thothnews.items.ThothClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingsAdapter extends MainPageAdapter{


	public SettingsAdapter(Context ctx, List<ThothClass> classes) {
		super(ctx, classes);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		if(view == null){
			view = ((LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(android.R.layout.simple_list_item_multiple_choice, null);
			view.setTag(createViewHolder(view));
		}
		
		bindItem(classes.get(position), view.getTag());
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
