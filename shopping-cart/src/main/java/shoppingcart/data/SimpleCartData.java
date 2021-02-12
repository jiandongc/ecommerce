package shoppingcart.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCartData {
    private String cartUid;
    private String customerUid;
    private String email;
    private boolean active;
    private String creationTime;
}
