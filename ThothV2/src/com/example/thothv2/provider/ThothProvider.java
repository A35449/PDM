package com.example.thothv2.provider;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class ThothProvider extends ThothProviderAbstract{
	
	private static List<ThothProvider> providers = new LinkedList<ThothProvider>();
	
	static {
		_urimatcher = new UriMatcher(0);
		providers.add(new NewsProvider());
		providers.add(new ClassesProvider());
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		ThothProvider provider = getProvider(_urimatcher.match(uri)); 
		
		if(provider != null)
			return provider.query(uri, projection, selection, selectionArgs, sortOrder); 
		
		throw new IllegalArgumentException("Unknown Uri "+uri);
	}
	

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		ThothProvider provider = getProvider(_urimatcher.match(uri)); 
		
		if(provider != null)
			return provider.insert(uri, values); 
		
		throw new IllegalArgumentException("Unknown Uri "+uri);
		
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		ThothProvider provider = getProvider(_urimatcher.match(uri)); 
		
		if(provider != null)
			return provider.delete(uri, selection, selectionArgs); 
		
		throw new IllegalArgumentException("Unknown Uri "+uri);
		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		ThothProvider provider = getProvider(_urimatcher.match(uri)); 
		
		if(provider != null)
			return provider.update(uri, values, selection, selectionArgs); 
		
		throw new IllegalArgumentException("Unknown Uri "+uri);
		
	}
	
	@Override
	public boolean onCreate() {
		_sql = new SQLRunner(getContext());
		return false;
	}
	
	protected ThothProvider getProvider(int uriCode)
	{
		
		for(ThothProvider p : providers)
		{
			ThothProvider tp = p.getProvider(uriCode);
			
			if(tp != null)
				return tp;
		}
		
		return null;
		
	}

}
