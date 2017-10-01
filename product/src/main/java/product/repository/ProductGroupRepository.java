package product.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by jiandong on 30/09/17.
 */

@Repository
public class ProductGroupRepository {

    private static final String INSERT_SQL = "insert into product_group (product_group, type, product_id) values (?, ?, ?)";
    private static final String TRUNCATE_SQL = "truncate table product_group";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addColorVariant(long groupId, long productId){
        jdbcTemplate.update(INSERT_SQL, groupId, "Color", productId);
    }

    public void deleteAll(){
        jdbcTemplate.update(TRUNCATE_SQL);
    }
}
