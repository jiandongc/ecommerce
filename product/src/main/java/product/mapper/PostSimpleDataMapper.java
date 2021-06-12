package product.mapper;

import org.springframework.stereotype.Component;
import product.data.PostSimpleData;
import product.domain.post.Post;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
public class PostSimpleDataMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    public PostSimpleData map(Post post){
        return PostSimpleData.builder()
                .title(post.getTitle())
                .slug(post.getSlug())
                .image(post.getImage())
                .summary(post.getSummary())
                .author(post.getAuthor().getName())
                .createDate(dateTimeFormatter.format(post.getCreateDate()))
                .build();
    }
}
