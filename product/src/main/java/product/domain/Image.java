package product.domain;



import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

/**
 * Created by jiandong on 15/08/17.
 */

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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageTypeValue(){
        return imageType.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (imageType != null ? !imageType.equals(image.imageType) : image.imageType != null) return false;
        if (url != null ? !url.equals(image.url) : image.url != null) return false;
        return !(product != null ? !product.equals(image.product) : image.product != null);

    }

    @Override
    public int hashCode() {
        int result = imageType != null ? imageType.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }
}