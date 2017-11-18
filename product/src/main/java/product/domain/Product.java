package product.domain;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "product_code")
	private String code;
	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "category_id")
	private Category category;
	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "brand_id")
	private Brand brand;
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "parent_id")
	private Product parent;
	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
	@OrderBy(value="ordering")
	private List<Image> images;
	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "product")
	private List<Sku> skus;
	@ManyToMany(fetch = LAZY)
	@JoinTable(name = "product_attribute_value", joinColumns = {@JoinColumn(name = "product_id")}, inverseJoinColumns = {@JoinColumn(name = "attribute_value_id")})
	private List<Attribute> attributes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Product getParent() {
		return parent;
	}

	public void setParent(Product parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addImage(Image image){
		if(images == null){
			images = new ArrayList<>();
		}

		images.add(image);
		image.setProduct(this);
	}

	public void addSku(Sku sku){
		if(skus == null){
			skus = new ArrayList<>();
		}

		skus.add(sku);
		sku.setProduct(this);
	}

	public void addAttribute(Attribute attribute){
		if(attributes == null){
			attributes = new ArrayList<>();
		}
		attributes.add(attribute);
	}

	public String getMainImageUrl(){
		final Optional<Image> mainImage = images.stream().filter(image -> "Main".equalsIgnoreCase(image.getImageTypeValue())).findFirst();
		return mainImage.map(Image::getUrl).orElse(null);
	}

	public String getColorImageUrl(){
		final Optional<Image> mainImage = images.stream().filter(image -> "Color".equalsIgnoreCase(image.getImageTypeValue())).findFirst();
		return mainImage.map(Image::getUrl).orElse(null);
	}

	public BigDecimal getMinPrice(){
		final Optional<Sku> sku = skus.stream().min((s1, s2) -> s1.getPrice().compareTo(s2.getPrice()));
		return sku.map(Sku::getPrice).orElse(null);
	}

	public String getCategoryCode(){
		return category.getCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Product product = (Product) o;

		if (name != null ? !name.equals(product.name) : product.name != null) return false;
		if (description != null ? !description.equals(product.description) : product.description != null) return false;
		if (code != null ? !code.equals(product.code) : product.code != null) return false;
		if (category != null ? !category.equals(product.category) : product.category != null) return false;
		if (brand != null ? !brand.equals(product.brand) : product.brand != null) return false;
		if (parent != null ? !parent.equals(product.parent) : product.parent != null) return false;
		if (images != null ? !images.equals(product.images) : product.images != null) return false;
		if (skus != null ? !skus.equals(product.skus) : product.skus != null) return false;
		return attributes != null ? attributes.equals(product.attributes) : product.attributes == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (code != null ? code.hashCode() : 0);
		result = 31 * result + (category != null ? category.hashCode() : 0);
		result = 31 * result + (brand != null ? brand.hashCode() : 0);
		result = 31 * result + (parent != null ? parent.hashCode() : 0);
		result = 31 * result + (images != null ? images.hashCode() : 0);
		result = 31 * result + (skus != null ? skus.hashCode() : 0);
		result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", code='" + code + '\'' +
				", category=" + category +
				", brand=" + brand +
				", parent=" + parent +
				", images=" + images +
				", skus=" + skus +
				", attributes=" + attributes +
				'}';
	}
}
