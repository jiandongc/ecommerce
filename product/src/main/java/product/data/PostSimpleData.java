package product.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSimpleData {

    private String title;

    private String slug;

    private String summary;

    private String image;

    private String createDate;

    private String author;


}
