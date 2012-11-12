package cz.payas.model;

public class Move {

	private Integer id;
	private Integer order;
	
	// mark 0 is positive, mark 1 is negative direction
	private Integer direction;
	
	private Float size;
	
	// mark 1 < |0,7| < mark 2 < |1,4| < mark 3 > |3| 
	private Integer sizeNormalize;
	
	private Integer axis;

	public Move() {
		
	}
	
	

	public Move(Integer order, Integer direction, Float size, Integer axis) {
		super();
		this.order = order;
		this.direction = direction;
		this.size = size;
		this.axis = axis;
		this.sizeNormalize = countSizeNormalize();
	}



	private Integer countSizeNormalize() {
		float sizeAbs = Math.abs(size);
		if(sizeAbs < 0.7){
			return 1;
		} else if (sizeAbs> 0.7 && sizeAbs < 1.4 ) {
			return 2;
		} else {
			return 3;
		}
		
	}



	public Move(Integer id, Integer order, Integer direction, Float size, Integer sizeNormalize,
			Integer axis) {
		super();
		this.id = id;
		this.order = order;
		this.direction = direction;
		this.size = size;
		this.axis = axis;
		this.sizeNormalize = sizeNormalize;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}

	public Float getSize() {
		return size;
	}

	public void setSize(Float size) {
		this.size = size;
	}

	public Integer getAxis() {
		return axis;
	}

	public void setAxis(Integer axis) {
		this.axis = axis;
	}
	
	public void setSizeNormalize(Integer sizeNormalize) {
		this.sizeNormalize = sizeNormalize;
	}
	
	public Integer getSizeNormalize() {
		return sizeNormalize;
	}
	
	@Override
	public String toString() {
		return "Move [ id=" + id + " order=" + order + " direction=" + direction + " size=" + size + " axis=" + axis + " ]";
	}

}
