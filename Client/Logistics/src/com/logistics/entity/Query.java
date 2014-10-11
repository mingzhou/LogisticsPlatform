package com.logistics.entity;

import java.sql.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/*
*建立数据表项，记录查询时间+查询内容
*@author lizhuo
*/
@DatabaseTable(tableName = "tbl_query")
public class Query {
	@DatabaseField(generatedId = true)
	private long id;
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date queryDate;
	
	@DatabaseField
	private String query;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Date queryDate) {
		this.queryDate = queryDate;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(",queryDate=").append(queryDate);
		sb.append(",query=").append(query);
		return sb.toString();
	}
	
	
}
