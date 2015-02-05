package product.domain;

public enum Category {
	PUFFED_SNACKS("Puffed Snacks", "PS");
	
	private final String desc;
	private final String acronym;
	
	Category(String desc, String acronym){
		this.desc = desc;
		this.acronym = acronym;
	}
	
	public String getDesc() {
		return desc;
	}

	public String getAcronym() {
		return acronym;
	}
}