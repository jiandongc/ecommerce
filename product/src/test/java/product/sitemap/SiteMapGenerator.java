package product.sitemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import product.domain.Brand;
import product.domain.Category;
import product.domain.Product;
import product.domain.ProductTag;
import product.repository.AbstractRepositoryTest;
import product.repository.CategoryRepository;
import product.repository.ProductRepository;
import product.service.BrandService;
import product.service.ProductTagService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Ignore
public class SiteMapGenerator extends AbstractRepositoryTest {

    private static final String BASE_URL = "https://noodle-monster.co.uk";
    private static final String BASE_DIRECTORY = "/home/jiandong/Project2015";

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductTagService productTagService;

    @Autowired
    private BrandService brandService;

    @Test
    public void categoryMap() throws Exception {
        List<Category> categories = categoryRepository.findAll();
        JAXBContext contextObj = JAXBContext.newInstance(UrlSet.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        String lastModified = DateTimeFormatter.ISO_DATE.format(LocalDate.now());

        UrlSet urlSet = UrlSet.builder().xmlns("http://www.sitemaps.org/schemas/sitemap/0.9").build();
        categories.forEach(category -> {
            Url url = Url.builder().location(BASE_URL + "/category/" + category.getCode()).lastModified(lastModified).build();
            urlSet.addUrl(url);
        });

        marshallerObj.marshal(urlSet, new File(BASE_DIRECTORY + "/ecommerce/angular-app/category_map.xml"));
    }

    @Test
    public void productMap() throws Exception {
        List<Product> products = productRepository.findAll();
        JAXBContext contextObj = JAXBContext.newInstance(UrlSet.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        String lastModified = DateTimeFormatter.ISO_DATE.format(LocalDate.now());

        UrlSet urlSet = UrlSet.builder().xmlns("http://www.sitemaps.org/schemas/sitemap/0.9").build();
        products.stream().filter(Product::isActive).forEach(product -> {
            Url url = Url.builder().location(BASE_URL + "/products/" + product.getCode()).lastModified(lastModified).build();
            urlSet.addUrl(url);
        });

        marshallerObj.marshal(urlSet, new File(BASE_DIRECTORY + "/ecommerce/angular-app/product_map.xml"));
    }

    @Test
    public void tagMap() throws Exception {
        List<ProductTag> tags = productTagService.findAll();
        JAXBContext contextObj = JAXBContext.newInstance(UrlSet.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        String lastModified = DateTimeFormatter.ISO_DATE.format(LocalDate.now());

        UrlSet urlSet = UrlSet.builder().xmlns("http://www.sitemaps.org/schemas/sitemap/0.9").build();
        tags.forEach(productTag -> {
            Url url = Url.builder().location(BASE_URL + "/tags/" + productTag.getTag()).lastModified(lastModified).build();
            urlSet.addUrl(url);
        });

        marshallerObj.marshal(urlSet, new File(BASE_DIRECTORY + "/ecommerce/angular-app/tag_map.xml"));
    }

    @Test
    public void brandMap() throws Exception {
        List<Brand> brands = brandService.findAll();

        JAXBContext contextObj = JAXBContext.newInstance(UrlSet.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        String lastModified = DateTimeFormatter.ISO_DATE.format(LocalDate.now());

        UrlSet urlSet = UrlSet.builder().xmlns("http://www.sitemaps.org/schemas/sitemap/0.9").build();
        brands.forEach(brand -> {
            Url url = Url.builder().location(BASE_URL + "/brands/" + brand.getCode()).lastModified(lastModified).build();
            urlSet.addUrl(url);
        });

        marshallerObj.marshal(urlSet, new File(BASE_DIRECTORY + "/ecommerce/angular-app/brand_map.xml"));
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "urlset")
    public static class UrlSet {
        @XmlAttribute
        private String xmlns;
        @XmlElement(name = "url")
        private List<Url> urls;

        public void addUrl(Url url) {
            if (urls == null) {
                urls = new ArrayList<>();
            }
            urls.add(url);
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement(name = "url")
    public static class Url {
        @XmlElement(name = "loc")
        private String location;
        @XmlElement(name = "lastmod")
        private String lastModified;
        @XmlElement(name = "changefreq")
        private String changeFreq;
        @XmlElement(name = "priority")
        private String priority;

    }


}
