package product.domain;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@Column(name = "id", nullable = false)
	private long id;
	@Column(name = "name")
	private String name;
	@Column(name = "description")
	private String description;
	@Column(name = "imageurl")
	private String imageUrl;
	@Column(name = "parentid")
	private long parentId;

	public Category(){
	}

	public Category(long id, String name, String description, String imageUrl, long parentId){
		this.id = id;
		this.name = name;
		this.description = description;
		this.imageUrl = imageUrl;
		this.parentId = parentId;
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

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public boolean isTopCategory () {
		return parentId == 0l;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Category category = (Category) o;

		if (id != category.id) return false;
		if (parentId != category.parentId) return false;
		if (name != null ? !name.equals(category.name) : category.name != null) return false;
		if (description != null ? !description.equals(category.description) : category.description != null)
			return false;
		return !(imageUrl != null ? !imageUrl.equals(category.imageUrl) : category.imageUrl != null);

	}

	@Override
	public int hashCode() {
		int result = (int) (id ^ (id >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
		result = 31 * result + (int) (parentId ^ (parentId >>> 32));
		return result;
	}
}