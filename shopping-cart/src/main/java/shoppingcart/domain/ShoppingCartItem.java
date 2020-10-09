package shoppingcart.domain;

import java.math.BigDecimal;
import java.util.Date;

public class ShoppingCartItem {

    private long id;
    private long cartId;
    private String code;
    private String sku;
    private String name;
    private int vatRate;
    private BigDecimal price;
    private int quantity;
    private String imageUrl;
    private String description;
    private Date creationTime;
    private Date lastUpdateTime;

    private ShoppingCartItem(long id,
                             long cartId,
                             String code,
                             String sku,
                             String name,
                             int vatRate,
                             BigDecimal price,
                             int quantity,
                             String imageUrl,
                             String description,
                             Date creationTime,
                             Date lastUpdateTime) {
        this.id = id;
        this.cartId = cartId;
        this.code = code;
        this.sku = sku;
        this.name = name;
        this.vatRate = vatRate;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.description = description;
        this.creationTime = creationTime;
        this.lastUpdateTime = lastUpdateTime;
    }

    public ShoppingCartItem() {
    }

    public static ShoppingCartItemBuilder builder() {
        return new ShoppingCartItemBuilder();
    }

    public long getId() {
        return id;
    }

    public long getCartId() {
        return cartId;
    }

    public String getCode() {
        return code;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public int getVatRate(){
        return vatRate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public BigDecimal getGrossTotal(){
        return this.price.multiply(BigDecimal.valueOf(this.quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getVat(){
        return this.getGrossTotal().subtract(this.getNetAmount());
    }

    public BigDecimal getNetAmount(){
        double divisor = (double) vatRate / 100 + 1;
        return this.getGrossTotal().divide(BigDecimal.valueOf(divisor), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCartItem that = (ShoppingCartItem) o;

        if (id != that.id) return false;
        if (cartId != that.cartId) return false;
        if (quantity != that.quantity) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (vatRate != that.vatRate) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (imageUrl != null ? !imageUrl.equals(that.imageUrl) : that.imageUrl != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (creationTime != null ? !creationTime.equals(that.creationTime) : that.creationTime != null) return false;
        return lastUpdateTime != null ? lastUpdateTime.equals(that.lastUpdateTime) : that.lastUpdateTime == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (cartId ^ (cartId >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (sku != null ? sku.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + vatRate;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        return result;
    }

    public static class ShoppingCartItemBuilder {
        private long id;
        private long cartId;
        private String code;
        private String sku;
        private String name;
        private int vatRate;
        private BigDecimal price;
        private int quantity;
        private String imageUrl;
        private String description;
        private Date creationTime;
        private Date lastUpdateTime;

        public ShoppingCartItemBuilder id(long id) {
            this.id = id;
            return this;
        }

        public ShoppingCartItemBuilder cartId(long cartId) {
            this.cartId = cartId;
            return this;
        }

        public ShoppingCartItemBuilder code(String code) {
            this.code = code;
            return this;
        }

        public ShoppingCartItemBuilder sku(String sku) {
            this.sku = sku;
            return this;
        }

        public ShoppingCartItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ShoppingCartItemBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ShoppingCartItemBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ShoppingCartItemBuilder vatRate(int vatRate) {
            this.vatRate = vatRate;
            return this;
        }

        public ShoppingCartItemBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ShoppingCartItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ShoppingCartItemBuilder creationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public ShoppingCartItemBuilder lastUpdateTime(Date lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
            return this;
        }

        public ShoppingCartItem build() {
            return new ShoppingCartItem(id, cartId, code, sku, name, vatRate, price, quantity, imageUrl, description, creationTime, lastUpdateTime);
        }


    }
}
