package com.example.thothv2.provider;


import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ClassesProvider extends ThothProviderAbstract {
	
	public final static int CLASSES = 1;
	public final static int SELECTED = 2;
	public final static int NEWS = 3;
	public final static int ROOT_CLASSES = 0;
	
	static {
		_urimatcher = new UriMatcher(ROOT_CLASSES);
		_urimatcher.addURI(AUTHORITY, "classes", CLASSES);
		_urimatcher.addURI(AUTHORITY, "classes/selected", SELECTED);
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = _sql.getReadableDatabase();
		Cursor c = null;
		
		switch(_urimatcher.match(uri)){
		
			case CLASSES:
				c = db.query("classes", projection, 
						selection, selectionArgs, 
						null, null, sortOrder);
				return c;
			case SELECTED:
				c = db.query("classes", projection, 
						"_selected = 1", null, 
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
	    long tag_id = db.insert("classes",null, values);
	    
	    String uri_id = uri.toString() + "/" + Long.toString(tag_id);
	    return Uri.parse(uri_id);
	}

	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		SQLiteDatabase db =  _sql.getWritableDatabase();
		
		return db.delete(uri.toString(), whereClause, whereArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onCreate() {
		if(_sql != null) _sql.close();
		_sql = new SQLRunner(getContext());
		return true;
	}

}
