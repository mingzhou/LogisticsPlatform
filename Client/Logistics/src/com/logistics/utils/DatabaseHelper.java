package com.logistics.utils;

import java.sql.SQLException;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.logistics.entity.MyData;

/**
 * 数据库
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 * @modified Li.ZHuo
 */
@ContextSingleton
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "logistics.db";
	
	/**
	 * 数据库的版本，更新一次数据库，会把原来的是数据都删掉（TODO 修改成升级策略）
	 * 更新的做法，是把DATABASE_VERSION加1
	 */
	private static final int DATABASE_VERSION = 1;
	
	private Dao<MyData, Integer> mydataDao;
	
	@Inject
	private static Provider<Context> contextProvider;
	
	public DatabaseHelper() {
		super(contextProvider.get(), DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * 第一次安装的时候创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {		
		try {
			//TableUtils.createTable(connectionSource, Account.class);
			TableUtils.createTable(connectionSource, MyData.class);
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
			//TableUtils.dropTable(connectionSource, Account.class, true);
			TableUtils.dropTable(connectionSource, MyData.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<MyData, Integer> getMyDataDao() throws SQLException {
		if (mydataDao == null) {
			mydataDao = getDao(MyData.class);
		}
		return mydataDao;
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	
	
}
