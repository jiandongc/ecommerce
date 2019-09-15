package product.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.List;

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

	@ManyToMany(fetch = LAZY)
	@JoinTable(name = "category_filter_attribute", joinColumns = {@JoinColumn(name = "category_id")}, inverseJoinColumns = {@JoinColumn(name = "attribute_id")})
	@OrderColumn(name = "ordering")
	private List<Key> filterKeys;

	public boolean isTopCategory(){
		return parent == null;
	}

}