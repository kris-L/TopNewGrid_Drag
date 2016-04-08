package com.tisson.test;

import java.io.Serializable;

/**
 * ITEM�Ķ�Ӧ���򻯶�������
 * */
public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/**
	 * ��Ŀ��ӦID
	 * */
	public Integer id;
	/**
	 * ��Ŀ��ӦNAME
	 * */
	public String name;
	
	public int image;

	public boolean selected;// �Ƿ�ѡ��

	public ChannelItem() {
	}

	public ChannelItem(int id, String name,int image) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.image = image;
	}

	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}
	
	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name + "]";
	}
}