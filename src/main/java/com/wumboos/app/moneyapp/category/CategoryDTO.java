package com.wumboos.app.moneyapp.category;

public class CategoryDTO {

	private String name;

	private String owner;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "CategoryDTO [name=" + name + ", owner=" + owner + "]";
	}


}
