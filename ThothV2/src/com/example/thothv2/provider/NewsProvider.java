package com.example.thothv2.provider;


import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NewsProvider extends ThothProviderAbstract{

	public final static int ROOT_NEWS  = 10;
	public final static int NEWS = 11;
	
	static {
		
		_urimatcher = new UriMatcher(ROOT_NEWS);
		_urimatcher.addURI(AUTHORITY,"news",NEWS);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = _sql.getReadableDatabase();
		Cursor c = null;
		
		switch(_urimatcher.match(uri)){
				
			case NEWS:
				c = db.query("news", projection, 
						selection, selectionArgs, 
						null, null, sortOrder);
				return c;	
				
			default: return null;
		}
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = _sql.getWritableDatabase();
	    // insert row
	    long tag_id = db.insert("news",null, values);
	    
	    String uri_id = uri.toString() + "\\" + Long.toString(tag_id);
	    return Uri.parse(uri_id);// TODO Auto-generated method stub
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean onCreate() {
		_sql = new SQLRunner(getContext());
		return false;
	}

}
