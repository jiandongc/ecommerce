package order.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StripeMetaData {
    private String shoppingCartId;
    private String userName;
    private String siteName;
    private String homePage;
    private String registrationPage;
}
