package product.domain;

import javax.persistence.*;

/**
 * Created by jiandong on 15/08/17.
 */

@Entity
@Table(name = "image_type")
public class ImageType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "type")
    private String type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageType imageType = (ImageType) o;

        return !(type != null ? !type.equals(imageType.type) : imageType.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ImageType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
