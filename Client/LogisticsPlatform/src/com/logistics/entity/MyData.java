package com.logistics.entity;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "tbl_mydata")
public class MyData {
	@DatabaseField(generatedId = true)
	private long id;
	
	@DatabaseField
	private String mongoid;//服务器id
	
	@DatabaseField
	private String site;//来源网站
	
	@DatabaseField
	private String url;//该条目的网址
	
	@DatabaseField
	private String from;//起始地
	
	@DatabaseField
	private String to;//目的地
	
	@DatabaseField
	private String title;//货物名称
	
	@DatabaseField
	private String type;//货物类型
	
	@DatabaseField
	private String volume;//货物体积
	
	@DatabaseField
	private String quality;//货物质量
	
	@DatabaseField
	private String packing;//包装方式
	
	@DatabaseField
	private String vehicle;//货车类型
	
	@DatabaseField
	private String length;//货车长度
	
	@DatabaseField
	private String attention;//注意事项
		
	@DatabaseField
	private String fee;//预计运费
	
	@DatabaseField
	private String contact;//联系方式
	
	@DatabaseField
	private String others;//备注
	
	private Boolean unread = true;//是否未读
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date deadline;//发货时间
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date date;//发布时间
	
	@DatabaseField(dataType = DataType.DATE_LONG)
	private Date datetime;//插入数据库的时间
	
	public MyData(){
		//needed
	}

	public String getMongoid() {
		return mongoid;
	}

	public void setMongoid(String mongoid) {
		this.mongoid = mongoid;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getPacking() {
		return packing;
	}

	public void setPacking(String packing) {
		this.packing = packing;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "MyData [id=" + id + ", mongoid=" + mongoid + ", site=" + site
				+ ", url=" + url + ", from=" + from + ", to=" + to + ", title="
				+ title + ", type=" + type + ", volume=" + volume
				+ ", quality=" + quality + ", packing=" + packing
				+ ", vehicle=" + vehicle + ", length=" + length
				+ ", attention=" + attention + ", fee=" + fee + ", contact="
				+ contact + ", others=" + others + ", deadline=" + deadline
				+ ", date=" + date + ", datetime=" + datetime + ", toString()="
				+ super.toString() + "]";
	}

	public Boolean getUnread() {
		return unread;
	}

	public void setUnread(Boolean unread) {
		this.unread = unread;
	}

	
}
