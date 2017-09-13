package product.domain;

import javax.persistence.*;

/**
 * Created by jiandong on 14/08/17.
 */

@Entity
@Table(name = "attribute")
public class Key {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        return !(name != null ? !name.equals(key.name) : key.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
