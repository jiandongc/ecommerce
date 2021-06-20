package shoppingcart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shoppingcart.domain.DeliveryOptionOffer;
import shoppingcart.mapper.DeliveryOptionOfferMapper;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DeliveryOptionRepositoryImpl implements DeliveryOptionRepository {

    private static final String INSERT_DELIVERY_OPTION_OFFER_SQL = "INSERT INTO delivery_option_offer " +
            "(country_code, method, min_spend, charge, min_days_required, max_days_required, vat_rate, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_DELIVERY_OPTION_OFFER_BY_COUNTRY_CODE_SQL =
            "SELECT * FROM delivery_option_offer WHERE country_code = ? AND start_date <= ? AND end_date >= ?";

    private static final String SELECT_DEFAULT_DELIVERY_OPTION_OFFER_BY_COUNTRY_CODE_SQL =
            "SELECT * FROM delivery_option_offer WHERE country_code = ? AND start_date IS NULL AND end_date IS NULL";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeliveryOptionOfferMapper deliveryOptionOfferMapper;

    @Override
    public void addDeliveryOptionOffers(DeliveryOptionOffer deliveryOptionOffer) {
        jdbcTemplate.update(INSERT_DELIVERY_OPTION_OFFER_SQL,
                deliveryOptionOffer.getCountryCode(),
                deliveryOptionOffer.getMethod(),
                deliveryOptionOffer.getMinSpend(),
                deliveryOptionOffer.getCharge(),
                deliveryOptionOffer.getMinDaysRequired(),
                deliveryOptionOffer.getMaxDaysRequired(),
                deliveryOptionOffer.getVatRate(),
                deliveryOptionOffer.getStartDate(),
                deliveryOptionOffer.getEndDate());
    }

    @Override
    public List<DeliveryOptionOffer> findDeliveryOptionOffersByCountryCode(String countryCode, LocalDate currentDate) {
        return jdbcTemplate.query(SELECT_DELIVERY_OPTION_OFFER_BY_COUNTRY_CODE_SQL, deliveryOptionOfferMapper, countryCode, currentDate, currentDate);
    }

    @Override
    public List<DeliveryOptionOffer> findDefaultDeliveryOptionsByCountryCode(String countryCode) {
        return jdbcTemplate.query(SELECT_DEFAULT_DELIVERY_OPTION_OFFER_BY_COUNTRY_CODE_SQL, deliveryOptionOfferMapper, countryCode);
    }
}
