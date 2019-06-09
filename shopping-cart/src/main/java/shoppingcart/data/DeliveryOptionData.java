package shoppingcart.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryOptionData {

    private final String method;
    private final Double charge;
    private final Integer minDays;
    private final Integer maxDays;
    private final String eta;

    @JsonCreator
    public DeliveryOptionData(@JsonProperty("method") String method,
                              @JsonProperty("charge") Double charge,
                              @JsonProperty("minDays") Integer minDays,
                              @JsonProperty("maxDays") Integer maxDays,
                              @JsonProperty("eta") String eta) {
        this.method = method;
        this.charge = charge;
        this.minDays = minDays;
        this.maxDays = maxDays;
        this.eta = eta;
    }

    public static DeliveryOptionData.Builder builder(){
        return new DeliveryOptionData.Builder();
    }

    public String getMethod() {
        return method;
    }

    public Double getCharge() {
        return charge;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public String getEta() {
        return eta;
    }

    public static class Builder {
        private String method;
        private Double charge;
        private Integer minDays;
        private Integer maxDays;
        private String eta;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder charge(Double charge) {
            this.charge = charge;
            return this;
        }

        public Builder minDays(Integer minDays) {
            this.minDays = minDays;
            return this;
        }

        public Builder maxDays(Integer maxDays) {
            this.maxDays = maxDays;
            return this;
        }

        public DeliveryOptionData build(){
            return new DeliveryOptionData(method, charge, minDays, maxDays, eta);
        }

    }
}
