package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.Voucher;
import shoppingcart.mapper.VoucherMapper;

import java.sql.Types;
import java.util.List;
import java.util.UUID;

@Repository
public class VoucherRepositoryImpl implements VoucherRepository {

    private static final String INSERT_VOUCHER_SQL = "INSERT INTO voucher " +
            "(type, code, name, max_uses, max_uses_user, min_spend, discount_amount, start_date, end_date, customer_uid) " +
            "VALUES (:type, :code, :name, :max_uses, :max_uses_user, :min_spend, :discount_amount, :start_date, :end_date, :customer_uid)";

    private static final String SELECT_CUSTOMER_VOUCHER_SQL = "select * from voucher where customer_uid = ? and soft_delete = false";

    private static final String NUMBER_OF_USES = "select count(p.id) from promotion p " +
            "join shopping_cart sc on p.shopping_cart_id = sc.id " +
            "where sc.active = false " +
            "and p.voucher_code = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public void save(Voucher voucher) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("type", voucher.getType(), Types.VARCHAR);
        namedParameters.addValue("code", voucher.getCode(), Types.VARCHAR);
        namedParameters.addValue("name", voucher.getName(), Types.VARCHAR);
        namedParameters.addValue("max_uses", voucher.getMaxUses(), Types.INTEGER);
        namedParameters.addValue("max_uses_user", voucher.getMaxUsesUser(), Types.INTEGER);
        namedParameters.addValue("min_spend", voucher.getMinSpend(), Types.DOUBLE);
        namedParameters.addValue("discount_amount", voucher.getDiscountAmount(), Types.DOUBLE);
        namedParameters.addValue("start_date", voucher.getStartDate(), Types.DATE);
        namedParameters.addValue("end_date", voucher.getEndDate(), Types.DATE);
        namedParameters.addValue("customer_uid", voucher.getCustomerUid(), Types.OTHER);
        namedParameterJdbcTemplate.update(INSERT_VOUCHER_SQL, namedParameters);
    }

    @Override
    public List<Voucher> findByCustomerUid(UUID customerUid) {
        return jdbcTemplate.query(SELECT_CUSTOMER_VOUCHER_SQL, voucherMapper, customerUid);
    }

    @Override
    public Integer findNumberOfUses(String code) {
        return jdbcTemplate.queryForObject(NUMBER_OF_USES, new Object[] {code}, Integer.class);
    }
}
