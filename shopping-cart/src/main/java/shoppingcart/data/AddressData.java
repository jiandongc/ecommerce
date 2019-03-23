package shoppingcart.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressData {

    private final String name;
    private final String title;
    private final String mobile;
    private final String addressLine1;
    private final String addressLine2;
    private final String addressLine3;
    private final String city;
    private final String country;
    private final String postcode;

    @JsonCreator
    private AddressData(@JsonProperty("name") String name,
                       @JsonProperty("title") String title,
                       @JsonProperty("mobile") String mobile,
                       @JsonProperty("addressLine1") String addressLine1,
                       @JsonProperty("addressLine2") String addressLine2,
                       @JsonProperty("addressLine3") String addressLine3,
                       @JsonProperty("city") String city,
                       @JsonProperty("country") String country,
                       @JsonProperty("postcode") String postcode) {
        this.name = name;
        this.title = title;
        this.mobile = mobile;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.country = country;
        this.postcode = postcode;
    }

    public static AddressData.Builder builder(){
        return new AddressData.Builder();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPostcode() {
        return postcode;
    }

    public static class Builder {
        private String name;
        private String title;
        private String mobile;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String city;
        private String country;
        private String postcode;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder addressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public Builder addressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public Builder addressLine3(String addressLine3) {
            this.addressLine3 = addressLine3;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder postcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public AddressData build(){
            return new AddressData(name, title, mobile, addressLine1, addressLine2, addressLine3, city, country, postcode);
        }
    }
}
