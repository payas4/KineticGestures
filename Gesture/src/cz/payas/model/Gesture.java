package cz.payas.model;

public class Gesture {

	private Integer id;
	private String name;

	public Gesture() {
	}

	
	
	public Gesture(String name) {
		super();
		this.name = name;
	}



	public Gesture(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
