package com.mz.astroboy.entity;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * 对象关系映射，Object Relational Mapping
 * @author Mingzhou Zhuang (mingzhou.zhuang@gmail.com)
 *
 */
@DatabaseTable(tableName = "tbl_account")
public class Account {
	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField
	private String username;
	
	@DatabaseField(canBeNull = false)
	private String password;
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date registerDate;
	
	public Account() {
		//	必须有一个无参数的构造函数
	}
	
	public long getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(",username=").append(username);
		sb.append(",password=").append(password);
		sb.append(",registerDate=").append(registerDate.toString());
		return sb.toString();
	}
}
