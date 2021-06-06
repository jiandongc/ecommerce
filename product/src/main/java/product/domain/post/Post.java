package product.domain.post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "summary")
    private String summary;

    @Column(name = "image")
    private String image;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @JsonIgnore
    @Column(name = "published")
    private Boolean published;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany
    @JoinTable(name = "post_category_link", joinColumns = {@JoinColumn(name = "post_id")}, inverseJoinColumns = {@JoinColumn(name = "post_category_id")})
    List<PostCategory> categories;

    @ManyToMany
    @JoinTable(name = "post_tag_link", joinColumns = {@JoinColumn(name = "post_id")}, inverseJoinColumns = {@JoinColumn(name = "post_tag_id")})
    List<PostTag> tags;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "post")
    private List<PostMeta> metas;

    public void addPostCategory(PostCategory postCategory) {
        if (categories == null) {
            categories = new ArrayList<>();
        }

        categories.add(postCategory);
    }

    public void addTag(PostTag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }

        tags.add(tag);
    }

    public void addMeta(PostMeta meta) {
        if (metas == null) {
            metas = new ArrayList<>();
        }

        metas.add(meta);
        meta.setPost(this);
    }

}