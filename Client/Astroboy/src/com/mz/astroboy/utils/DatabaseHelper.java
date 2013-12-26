package com.mz.astroboy.utils;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mz.astroboy.entity.Account;
import com.mz.astroboy.entity.internal.ContextInfo;

/**
 * 数据库
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@Singleton
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "astroboy.db";
	
	/**
	 * 数据库的版本
	 */
	private static final int DATABASE_VERSION = 1;
	
	@Inject
	private static ContextInfo contextInfo;
	
	public DatabaseHelper() {
		super(contextInfo.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 第一次安装的时候创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {		
		try {
			TableUtils.createTable(connectionSource, Account.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * APP更新的时候更新数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Account.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
