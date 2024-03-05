package com.otto.mart.model.localmodel.ui;

public class SortModel {
	private String name;
	private String key;
	private boolean selected;
	private int pos;

	public SortModel() {
	}

	public SortModel(String name, String key) {
		this.name = name;
		this.key = key;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}
}
