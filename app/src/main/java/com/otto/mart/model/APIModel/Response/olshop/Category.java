package com.otto.mart.model.APIModel.Response.olshop;

import java.util.List;

public class Category{
	private List<Category> sub_categories;
	private String name;
	private String icon;
	private Integer id;
	private boolean isSelected;

	public Category() {
	}

	public Category(List<Category> sub_categories, String name, String icon, Integer id, boolean isSelected) {
		this.sub_categories = sub_categories;
		this.name = name;
		this.icon = icon;
		this.id = id;
		this.isSelected = isSelected;
	}

	public Category copy(Category category){
		return new Category(category.sub_categories,category.name,category.icon,category.id,category.isSelected);
	}

	public Category(Category category) {
		this(category.sub_categories,category.name,category.icon,category.id,category.isSelected);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public List<Category> getSub_categories() {
		return sub_categories;
	}

	public void setSub_categories(List<Category> sub_categories) {
		this.sub_categories = sub_categories;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return icon;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return id;
	}
}