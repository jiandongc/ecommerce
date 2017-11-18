package product.domain;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "category_code")
	private String code;
	@Column(name = "image_url")
	private String imageUrl;
	@Column(name = "hidden")
	private boolean hidden;
	@ManyToOne(fetch = EAGER)
	@JoinColumn(name = "parent_id")
	private Category parent;
	@ManyToMany(fetch = LAZY)
	@JoinTable(name = "category_filter_attribute", joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {@JoinColumn(name = "attribute_id")})
	@OrderColumn(name = "ordering")
	private List<Key> filterKeys;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Key> getFilterKeys() {
		return filterKeys;
	}

	public void setFilterKeys(List<Key> filterKeys) {
		this.filterKeys = filterKeys;
	}

	public boolean isTopCategory(){
		return parent == null;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Category category = (Category) o;

		if (hidden != category.hidden) return false;
		if (name != null ? !name.equals(category.name) : category.name != null) return false;
		if (description != null ? !description.equals(category.description) : category.description != null)
			return false;
		if (code != null ? !code.equals(category.code) : category.code != null) return false;
		if (imageUrl != null ? !imageUrl.equals(category.imageUrl) : category.imageUrl != null) return false;
		if (parent != null ? !parent.equals(category.parent) : category.parent != null) return false;
		return filterKeys != null ? filterKeys.equals(category.filterKeys) : category.filterKeys == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (code != null ? code.hashCode() : 0);
		result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
		result = 31 * result + (hidden ? 1 : 0);
		result = 31 * result + (parent != null ? parent.hashCode() : 0);
		result = 31 * result + (filterKeys != null ? filterKeys.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Category{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", code='" + code + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", hidden=" + hidden +
				", parent=" + parent +
				", filterKeys=" + filterKeys +
				'}';
	}
}