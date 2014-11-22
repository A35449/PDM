package com.example.thothv2.provider;


import java.util.List;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThothProvider extends ThothProviderAbstract{
	
	private static NewsProvider _newsProvider;
	private static ClassesProvider _classProvider;
	
	private static final String NEWS_TABLE = "news";
	private static final String CLASSES_TABLE = "classes";
	
	static {
		
		_newsProvider = new NewsProvider();
		_classProvider = new ClassesProvider();
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		String table = uri.getPathSegments().get(0);
		
		if(table.compareTo(NEWS_TABLE) == 0)
				return _newsProvider.query(uri, projection, selection, selectionArgs, sortOrder);
			
		if(table.compareTo(CLASSES_TABLE) == 0)	
				return _classProvider.query(uri, projection, selection, selectionArgs, sortOrder);
				
		throw new IllegalArgumentException("Unknown Uri "+uri);
	}
	

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		String table = uri.getPathSegments().get(0);
		
		if(table.compareTo(NEWS_TABLE) == 0)
			return _newsProvider.insert(uri, values);
			
		if(table.compareTo(CLASSES_TABLE) == 0)	
			return _classProvider.insert(uri,values);
				
		throw new IllegalArgumentException("Unknown Uri "+uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String table = uri.getPathSegments().get(0);
		
		if(table.compareTo(NEWS_TABLE) == 0)
			return _newsProvider.delete(uri, selection, selectionArgs);
			
		if(table.compareTo(CLASSES_TABLE) == 0)	
			return _classProvider.delete(uri,selection,selectionArgs);
		
		 throw new IllegalArgumentException("Unknown Uri "+uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		String table = uri.getPathSegments().get(0);
		
		if(table.compareTo(NEWS_TABLE) == 0)
			return _newsProvider.delete(uri, selection, selectionArgs);
			
		if(table.compareTo(CLASSES_TABLE) == 0)	
			return _classProvider.delete(uri,selection,selectionArgs);
		
		 throw new IllegalArgumentException("Unknown Uri "+uri);
	}
	
	@Override
	public boolean onCreate() {
		_sql = new SQLRunner(getContext());
		return false;
	}

}
