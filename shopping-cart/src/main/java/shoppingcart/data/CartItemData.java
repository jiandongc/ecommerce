package shoppingcart.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public final class CartItemData {

    private final String sku;
    private final String name;
    private final BigDecimal price;
    private final Integer quantity;
    private final String thumbnail;
    private final String description;

    @JsonCreator
    private CartItemData(@JsonProperty("sku") String sku,
                         @JsonProperty("name") String name,
                         @JsonProperty("price") BigDecimal price,
                         @JsonProperty("quantity") Integer quantity,
                         @JsonProperty("thumbnail") String thumbnail,
                         @JsonProperty("description") String description) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder {
        private String sku;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private String thumbnail;
        private String description;

        public Builder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public CartItemData build(){
            return new CartItemData(sku, name, price, quantity, thumbnail, description);
        }
    }
}
