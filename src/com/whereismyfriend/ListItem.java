package com.whereismyfriend;

public class ListItem {
	public int icon;
    public String title;
    public String id;
    
    public ListItem(){
        super();
    }
    
    public ListItem(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
    
    
}
