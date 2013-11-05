package com.whereismyfriend;

public class ListItem {
	public int icon;
    public String title;
    private String id;
    private String idSol;
    private String name;
    
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

	public String getIdSol() {
		return idSol;
	}

	public void setIdSol(String idSol) {
		this.idSol = idSol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
    
}