package com.example.thothv2.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public abstract class ThothProviderAbstract extends ContentProvider {
	
	protected static final String AUTHORITY = "com.example.thothv2";	

	public static SQLRunner _sql;
	public static  UriMatcher _urimatcher;

	@Override
	public abstract Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder);

	@Override
	public abstract String getType(Uri uri);

	@Override
	public abstract Uri insert(Uri uri, ContentValues values);

	@Override
	public abstract int delete(Uri uri, String selection, String[] selectionArgs);

	@Override
	public abstract int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs);
	

}
