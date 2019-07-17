package order.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private long id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "items")
    private BigDecimal items;

    @Column(name = "postage")
    private BigDecimal postage;

    @Column(name = "promotion")
    private BigDecimal promotion;

    @Column(name = "total_before_vat")
    private BigDecimal totalBeforeVat;

    @Column(name = "items_vat")
    private BigDecimal itemsVat;

    @Column(name = "postage_vat")
    private BigDecimal postageVat;

    @Column(name = "promotion_vat")
    private BigDecimal promotionVat;

    @Column(name = "total_vat")
    private BigDecimal totalVat;

    @Column(name = "order_total")
    private BigDecimal orderTotal;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    @Column(name = "min_days_required")
    private Integer minDaysRequired;

    @Column(name = "max_days_required")
    private Integer maxDaysRequired;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "order")
    private List<OrderItem> orderItems;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "order")
    private List<OrderAddress> orderAddresses;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "order")
    private List<OrderStatus> orderStatuses;

    @Transient
    private String eta;

    @PostLoad
    public void onLoad() {
        final String etaFromDate = formatDate(this.orderDate, this.minDaysRequired);
        final String etaToDate = formatDate(this.orderDate, this.maxDaysRequired);

        if (etaFromDate.equals(etaToDate)) {
            this.eta = etaFromDate;
        } else {
            this.eta = String.format("%s - %s", etaFromDate, etaToDate);
        }
    }

    private String formatDate(LocalDate orderDate, Integer days) {
        LocalDate localDate = days != null ? orderDate.plusDays(days) : orderDate;
        String dayOfWeek = localDate.getDayOfWeek().name();
        dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase();
        return dayOfWeek + ", " + DateTimeFormatter.ofPattern("MMM. dd").format(localDate);
    }

    public void addOrderItem(OrderItem orderItem) {
        if (this.orderItems == null) {
            this.orderItems = new ArrayList<>();
        }

        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void addOrderAddress(OrderAddress orderAddress) {
        if (this.orderAddresses == null) {
            this.orderAddresses = new ArrayList<>();
        }

        this.orderAddresses.add(orderAddress);
        orderAddress.setOrder(this);
    }

    public void addOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatuses == null) {
            this.orderStatuses = new ArrayList<>();
        }

        this.orderStatuses.add(orderStatus);
        orderStatus.setOrder(this);
    }
}
