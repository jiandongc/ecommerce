package shoppingcart.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeliveryOptionData {

    private final String method;
    private final Double charge;
    private final Integer minDaysRequired;
    private final Integer maxDaysRequired;
    private final String eta;

    @JsonCreator
    public DeliveryOptionData(@JsonProperty("method") String method,
                              @JsonProperty("charge") Double charge,
                              @JsonProperty("minDaysRequired") Integer minDaysRequired,
                              @JsonProperty("maxDaysRequired") Integer maxDaysRequired,
                              @JsonProperty("eta") String eta) {
        this.method = method;
        this.charge = charge;
        this.minDaysRequired = minDaysRequired;
        this.maxDaysRequired = maxDaysRequired;
        this.eta = eta;
    }

    public static DeliveryOptionData.Builder builder(){
        return new DeliveryOptionData.Builder();
    }

    public String getMethod() {
        return method;
    }

    public Integer getMinDaysRequired() {
        return minDaysRequired;
    }

    public Integer getMaxDaysRequired() {
        return maxDaysRequired;
    }

    public Double getCharge() {
        return charge;
    }

    public String getEta() {
        return eta;
    }

    public static class Builder {
        private String method;
        private Double charge;
        private Integer minDaysRequired;
        private Integer maxDaysRequired;
        private String eta;

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder charge(Double charge) {
            this.charge = charge;
            return this;
        }

        public Builder minDaysRequired(Integer minDaysRequired) {
            this.minDaysRequired = minDaysRequired;
            return this;
        }

        public Builder maxDaysRequired(Integer maxDaysRequired) {
            this.maxDaysRequired = maxDaysRequired;
            return this;
        }

        public Builder eta(String eta) {
            this.eta = eta;
            return this;
        }

        public DeliveryOptionData build(){
            return new DeliveryOptionData(method, charge, minDaysRequired, maxDaysRequired, eta);
        }

    }
}
