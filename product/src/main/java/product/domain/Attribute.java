package product.domain;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

/**
 * Created by jiandong on 14/08/17.
 */

@Entity
@Table(name = "attribute_value")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "attribute_id")
    private Key key;
    @Column(name = "attribute_value")
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKeyName(){
        return key.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (key != null ? !key.equals(attribute.key) : attribute.key != null) return false;
        return !(value != null ? !value.equals(attribute.value) : attribute.value != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}
