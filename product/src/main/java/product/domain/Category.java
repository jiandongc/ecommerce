package product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	@OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "category")
	@OrderBy(value = "ordering")
	private List<CategoryAttribute> categoryAttributes;

	public boolean isTopCategory(){
		return parent == null;
	}

	public void addCategoryAttribute(CategoryAttribute categoryAttribute) {
		if (categoryAttributes == null) {
			categoryAttributes = new ArrayList<>();
		}
		categoryAttributes.add(categoryAttribute);
		categoryAttribute.setCategory(this);
	}

}