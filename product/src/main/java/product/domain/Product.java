package product.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "product")
public class Product {
	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "product_seq", sequenceName = "product_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "unitprice")
	private Double unitPrice;
	@Column(name = "description")
	private String description;
	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "categoryid")
	private Category category;
	@Column(name = "imageurl")
	private String imageUrl;

	Product() {
		this(null, null, null, null, null);
	}

	public Product(String name, 
			Double unitPrice, 
			String description,
			Category category,
			String imageUrl) {
		this.name = name;
		this.unitPrice = unitPrice;
		this.description = description;
		this.category = category;
		this.imageUrl = imageUrl;
	}

	public Product(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Product product = (Product) o;

		if (id != product.id) return false;
		if (name != null ? !name.equals(product.name) : product.name != null) return false;
		if (unitPrice != null ? !unitPrice.equals(product.unitPrice) : product.unitPrice != null) return false;
		if (description != null ? !description.equals(product.description) : product.description != null) return false;
		if (category != null ? !category.equals(product.category) : product.category != null) return false;
		return !(imageUrl != null ? !imageUrl.equals(product.imageUrl) : product.imageUrl != null);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (unitPrice != null ? unitPrice.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (category != null ? category.hashCode() : 0);
		result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
		return result;
	}
}
