package com.logistics.entity;

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
	
	@DatabaseField
	private String phone;
	
	@DatabaseField//(canBeNull = false)
	private String password;
	
//	@DatabaseField(dataType = DataType.DATE_LONG)
//	private Date registerDate;
	
	public Account() {
		//	必须有一个无参数的构造函数
	}
	
	public long getId() {
		return id;
	}
		
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append(",phone=").append(phone);
		sb.append(",username=").append(username);
		sb.append(",password=").append(password);
		//sb.append(",registerDate=").append(registerDate.toString());
		return sb.toString();
	}
}
