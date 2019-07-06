package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressData {

    private String addressType;

    private String name;

    private String title;

    private String mobile;

    private String addressLine1;

    private String addressLine2;

    private String addressLine3;

    private String city;

    private String country;

    private String postcode;
}
