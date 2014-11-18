package com.example.thothv2.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThothProvider extends ThothProviderAbstract{
	
	private static NewsProvider _newsProvider;
	private static ClassesProvider _classProvider;
	
	static {
		
		_newsProvider = new NewsProvider();
		_classProvider = new ClassesProvider();
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		int uriMatchResult = ThothProviderAbstract._urimatcher.match(uri);
		
		switch(uriMatchResult){
			case NewsProvider.ROOT_NEWS:
				return _newsProvider.query(uri, projection, selection, selectionArgs, sortOrder);
			
			case ClassesProvider.ROOT_CLASSES:
				return _classProvider.query(uri, projection, selection, selectionArgs, sortOrder);
				
			default:
				 throw new IllegalArgumentException("Unknown Uri "+uri);
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriMatchResult = ThothProviderAbstract._urimatcher.match(uri);
		
		switch(uriMatchResult){
			case NewsProvider.ROOT_NEWS:
				return _newsProvider.insert(uri, values);
			
			case ClassesProvider.ROOT_CLASSES:
				return _classProvider.insert(uri,values);
				
			default:
				 throw new IllegalArgumentException("Unknown Uri "+uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriMatchResult = ThothProviderAbstract._urimatcher.match(uri);
		
		switch(uriMatchResult){
			case NewsProvider.ROOT_NEWS:
				return _newsProvider.delete(uri, selection, selectionArgs);
			case ClassesProvider.ROOT_CLASSES:
				return _classProvider.delete(uri,selection,selectionArgs);
			default:
				 throw new IllegalArgumentException("Unknown Uri "+uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriMatchResult = ThothProviderAbstract._urimatcher.match(uri);
		
		switch(uriMatchResult){
			case NewsProvider.ROOT_NEWS:
				return _newsProvider.update(uri, values, selection, selectionArgs);
			case ClassesProvider.ROOT_CLASSES:
				return _classProvider.update(uri, values, selection, selectionArgs);
			default:
				 throw new IllegalArgumentException("Unknown Uri "+uri);
		}
	}
	
	@Override
	public boolean onCreate() {
		_sql = new SQLRunner(getContext());
		return false;
	}

}
