package shoppingcart.repository;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import shoppingcart.domain.DeliveryOptionOffer;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class DeliveryOptionRepositoryImplTest extends AbstractRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DeliveryOptionRepository deliveryOptionRepository;

    @Test
    public void shouldRetrieveDeliveryOptionOffersByCountryCode() {
        // Given
        LocalDate today = LocalDate.now();

        DeliveryOptionOffer standardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today)
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(standardDelivery);

        DeliveryOptionOffer freeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.minusDays(1))
                .endDate(today)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(freeDelivery);

        DeliveryOptionOffer expiredStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.minusDays(10))
                .endDate(today.minusDays(9))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(expiredStandardDelivery);

        DeliveryOptionOffer futureFreeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.plusDays(9))
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(futureFreeDelivery);

        DeliveryOptionOffer defaultStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(null)
                .endDate(null)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(defaultStandardDelivery);

        DeliveryOptionOffer defaultFreeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(null)
                .endDate(null)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(defaultFreeDelivery);

        DeliveryOptionOffer usStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("US")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today)
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(usStandardDelivery);

        // When
        List<DeliveryOptionOffer> deliveryOptionOffers = deliveryOptionRepository.findDeliveryOptionOffersByCountryCode("UK", today);

        assertThat(deliveryOptionOffers.size(), CoreMatchers.is(2));
        assertThat(deliveryOptionOffers.get(0).getCharge(), CoreMatchers.is(3.99D));
        assertThat(deliveryOptionOffers.get(0).getStartDate(), CoreMatchers.is(today));
        assertThat(deliveryOptionOffers.get(0).getEndDate(), CoreMatchers.is(today.plusDays(10)));

        assertThat(deliveryOptionOffers.get(1).getCharge(), CoreMatchers.is(0D));
        assertThat(deliveryOptionOffers.get(1).getStartDate(), CoreMatchers.is(today.minusDays(1)));
        assertThat(deliveryOptionOffers.get(1).getEndDate(), CoreMatchers.is(today));
    }

    @Test
    public void shouldFindDefaultDeliveryOptionsByCountryCode(){
        // Given
        LocalDate today = LocalDate.now();

        DeliveryOptionOffer standardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today)
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(standardDelivery);

        DeliveryOptionOffer freeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.minusDays(1))
                .endDate(today)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(freeDelivery);

        DeliveryOptionOffer expiredStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.minusDays(10))
                .endDate(today.minusDays(9))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(expiredStandardDelivery);

        DeliveryOptionOffer futureFreeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today.plusDays(9))
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(futureFreeDelivery);

        DeliveryOptionOffer defaultStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(null)
                .endDate(null)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(defaultStandardDelivery);

        DeliveryOptionOffer defaultFreeDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("UK")
                .method("FREE Delivery")
                .minSpend(39.99D)
                .charge(0D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(null)
                .endDate(null)
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(defaultFreeDelivery);

        DeliveryOptionOffer usStandardDelivery = DeliveryOptionOffer.deliveryOptionOfferBuilder()
                .countryCode("US")
                .method("Standard Delivery")
                .minSpend(0D)
                .charge(3.99D)
                .minDaysRequired(2)
                .maxDaysRequired(5)
                .vatRate(20)
                .startDate(today)
                .endDate(today.plusDays(10))
                .build();
        deliveryOptionRepository.addDeliveryOptionOffers(usStandardDelivery);

        // When
        List<DeliveryOptionOffer> deliveryOptionOffers = deliveryOptionRepository.findDefaultDeliveryOptionsByCountryCode("UK");

        assertThat(deliveryOptionOffers.size(), CoreMatchers.is(2));
        assertThat(deliveryOptionOffers.get(0).getCharge(), CoreMatchers.is(3.99D));
        assertThat(deliveryOptionOffers.get(0).getStartDate(), CoreMatchers.nullValue());
        assertThat(deliveryOptionOffers.get(0).getEndDate(), CoreMatchers.nullValue());

        assertThat(deliveryOptionOffers.get(1).getCharge(), CoreMatchers.is(0D));
        assertThat(deliveryOptionOffers.get(1).getStartDate(), CoreMatchers.nullValue());
        assertThat(deliveryOptionOffers.get(1).getEndDate(), CoreMatchers.nullValue());
    }

}