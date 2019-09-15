package product.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "image_type_id")
    private ImageType imageType;

    @Column(name = "url")
    private String url;

    @Column(name = "ordering")
    private int ordering;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public String getImageTypeValue(){
        return imageType.getType();
    }
}
