package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DataSetupNoodleMonster extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @Rollback(false)
    public void initialise() {
        cookingNoodlesCategory();
        instanceNoodlesCategory();
        mateCategory();
        brands();

        instanceNoodlesSamyang();
        instanceNoodlesNongshim();
        instanceNoodlesNissin();
        instanceNoodlesMasterkong();
        instanceNoodlesTongyi();

        mateBeverage();

    }

    private void cookingNoodlesCategory() {
        Category noodles = Category.builder().name("煮食面").description("Noodles").code("noodles").build();
        categoryRepository.save(noodles);

        Category china = Category.builder().name("中國大陸 China").code("noodles-china").parent(noodles).build();
        categoryRepository.save(china);

        Category taiwan = Category.builder().name("臺灣 Taiwan").code("noodles-taiwan").parent(noodles).build();
        categoryRepository.save(taiwan);

        Category hongkong = Category.builder().name("香港 Hong Kong").code("noodles-hongkong").parent(noodles).build();
        categoryRepository.save(hongkong);

        Category korea = Category.builder().name("韓國 Korea").code("noodles-korea").parent(noodles).build();
        categoryRepository.save(korea);

        Category japan = Category.builder().name("日本 Japan").code("noodles-japan").parent(noodles).build();
        categoryRepository.save(japan);
    }

    private void instanceNoodlesCategory() {
        Category instanceNoodles = Category.builder().name("即食面").description("Instant Noodles").code("ins-noodles").build();
        categoryRepository.save(instanceNoodles);

        Category china = Category.builder().name("中國大陸 China").code("ins-noodles-china").parent(instanceNoodles).build();
        categoryRepository.save(china);

        Category taiwan = Category.builder().name("臺灣 Taiwan").code("ins-noodles-taiwan").parent(instanceNoodles).build();
        categoryRepository.save(taiwan);

        Category hongkong = Category.builder().name("香港 Hong Kong").code("ins-noodles-hongkong").parent(instanceNoodles).build();
        categoryRepository.save(hongkong);

        Category korea = Category.builder().name("韓國 Korea").code("ins-noodles-korea").parent(instanceNoodles).build();
        categoryRepository.save(korea);

        Category japan = Category.builder().name("日本 Japan").code("ins-noodles-japan").parent(instanceNoodles).build();
        categoryRepository.save(japan);
    }

    private void mateCategory() {
        Category mate = Category.builder().name("面條伴侶").description("Noodles mate").code("noodles-mate").build();
        categoryRepository.save(mate);

        Category beverage = Category.builder().name("飲品 Beverage").description("Beverage").code("beverage").parent(mate).build();
        categoryRepository.save(beverage);

        Category luwei = Category.builder().name("滷味 Braised dishes").description("Braised dishes").code("luwei").parent(mate).build();
        categoryRepository.save(luwei);
    }

    private void brands(){
        Brand samyang = Brand.builder().name("三養食品").code("samyang").startDate(LocalDate.now()).imageUrl("/images/brand/samyang.png").description("三养食品是一个韩国食品品牌，创立于1961年，所属公司韩国三养集团。2014年，因火鸡面超辣口感，在中国成为网红泡面之一。2017年，从韩国政府获得“1亿美元出口之塔”奖。2018年4月15日，三养食品成3X3黄金联赛伙伴").build();
        brandRepository.save(samyang);

        Brand nongshim = Brand.builder().name("農心").code("nongshim").startDate(LocalDate.now()).imageUrl("/images/brand/nongshim.jpg").description("株式会社农心成立于1965年，是以制造方便面、膨化食品（饼干）及其它食品加工为主导产业的韩国大型食品生产集团。").build();
        brandRepository.save(nongshim);

        Brand nissin = Brand.builder().name("日清食品").code("nissin").startDate(LocalDate.now()).imageUrl("/images/brand/nissin.png").description("日清食品是一间日本食品公司，以推出各种即食食品著名，是总部位于大阪府大阪市淀川区的日本食品加工公司。由开发了世界最早的方便面Chicken Ramen的安藤百福创办。").build();
        brandRepository.save(nissin);

        Brand tongyi = Brand.builder().name("統一").code("tongyi").startDate(LocalDate.now()).imageUrl("/images/brand/tongyi.jpg").description("统一企业正式成立于中国台湾地区台南市永康区,其总部位于台南市永康区。公司产品主要有饮料和方便面。").build();
        brandRepository.save(tongyi);

        Brand masterKong = Brand.builder().name("康师傅").code("masterkong").startDate(LocalDate.now()).imageUrl("/images/brand/masterkong.jpg").description("康师傅自1992年研发生产出第一包方便面后，迅速成长为国内乃至全球最大的方便面生产销售企业。").build();
        brandRepository.save(masterKong);

        Brand xiangpiaopiao = Brand.builder().name("香飘飘").code("xiangpiaopiao").startDate(LocalDate.now()).imageUrl("/images/brand/xiangpiaopiao.jpg").description("香飘飘是香飘飘食品股份有限公司旗下杯装奶茶品牌，成立于2005年，专业从事奶茶产品的研发、生产和销售。").build();
        brandRepository.save(xiangpiaopiao);
    }

    private void instanceNoodlesSamyang(){
        Category category = categoryRepository.findByCode("ins-noodles-korea").get();
        Brand brand = brandRepository.findByCode("samyang").get();

        addProduct(
                category,
                brand,
                "1017004481",
                Arrays.asList("1017004481"),
                "韩国SAMYANG三养 奶油芝士火鸡面 粉色限定新口味 5包入 650g",
                100,
                Arrays.asList("/images/products/1017004481.webp", "/images/products/1017004481-1.webp"),
                Arrays.asList(BigDecimal.valueOf(6.99), BigDecimal.valueOf(5.59)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004482",
                Arrays.asList("1017004481"),
                "韩国SAMYANG三养 超辣鸡肉味拌面 5包入 700g",
                100,
                Arrays.asList("/images/products/1017004482.webp", "/images/products/1017004482-1.webp"),
                Arrays.asList(BigDecimal.valueOf(5.99), BigDecimal.valueOf(4.79)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004483",
                Arrays.asList("1017004483"),
                "韩国 SAMYANG三养 超辣咖喱火鸡面 桶装 105g",
                100,
                Arrays.asList("/images/products/1017004483.webp", "/images/products/1017004483-1.webp", "/images/products/1017004483-2.webp"),
                Arrays.asList(BigDecimal.valueOf(2.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004484",
                Arrays.asList("1017004484"),
                "韩国 SAMYANG三养 韩版超辣汤面 鸡肉味 碗装 120g",
                100,
                Arrays.asList("/images/products/1017004484.webp", "/images/products/1017004484-1.webp", "/images/products/1017004484-2.webp", "/images/products/1017004484-3.webp"),
                Arrays.asList(BigDecimal.valueOf(2.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004485",
                Arrays.asList("1017004485"),
                "韩国 SAMYANG三养 超辣火鸡汤面 145g*5",
                100,
                Arrays.asList("/images/products/1017004485.webp", "/images/products/1017004485-1.webp", "/images/products/1017004485-2.webp", "/images/products/1017004485-3.webp"),
                Arrays.asList(BigDecimal.valueOf(6.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );
    }

    private void instanceNoodlesNongshim(){
        Category category = categoryRepository.findByCode("ins-noodles-korea").get();
        Brand brand = brandRepository.findByCode("nongshim").get();

        addProduct(
                category,
                brand,
                "1017004491",
                Arrays.asList("1017004491"),
                "韩国NONGSHIM农心 红色辛拉面 杯装 75g",
                100,
                Arrays.asList("/images/products/1017004491.webp", "/images/products/1017004491-1.webp", "/images/products/1017004491-2.webp"),
                Arrays.asList(BigDecimal.valueOf(1.59), BigDecimal.valueOf(1.29)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004492",
                Arrays.asList("1017004492"),
                "韩国NONGSHIM农心 速食辣泡菜杯面 75g",
                100,
                Arrays.asList("/images/products/1017004492.webp", "/images/products/1017004492-1.webp"),
                Arrays.asList(BigDecimal.valueOf(1.49)),
                null,
                null
        );

    }

    private void instanceNoodlesNissin(){
        Category nissinHongkong = categoryRepository.findByCode("ins-noodles-hongkong").get();
        Category nissinJapan = categoryRepository.findByCode("ins-noodles-japan").get();
        Brand brand = brandRepository.findByCode("nissin").get();

        addProduct(
                nissinHongkong,
                brand,
                "1017004501",
                Arrays.asList("1017004501"),
                "日本NISSIN日清 出前一丁 即食汤面 黑蒜油猪骨汤味 碗装 106g",
                100,
                Arrays.asList("/images/products/1017004501.webp", "/images/products/1017004501-1.webp"),
                Arrays.asList(BigDecimal.valueOf(2.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                nissinHongkong,
                brand,
                "1017004502",
                Arrays.asList("1017004502"),
                "日本NISSIN日清 UFO 飞碟炒面 四川鱼香肉丝风味 116g",
                100,
                Arrays.asList("/images/products/1017004502.webp", "/images/products/1017004502-1.webp", "/images/products/1017004502-2.webp"),
                Arrays.asList(BigDecimal.valueOf(2.59), BigDecimal.valueOf(2.00)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );

        addProduct(
                nissinJapan,
                brand,
                "1017004503",
                Arrays.asList("1017004503"),
                "日本NISSIN日清 欧风芝士咖喱风味杯面 85g",
                100,
                Arrays.asList("/images/products/1017004503.webp", "/images/products/1017004503-1.webp", "/images/products/1017004503-2.webp"),
                Arrays.asList(BigDecimal.valueOf(4.50), BigDecimal.valueOf(3.60)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );

        addProduct(
                nissinJapan,
                brand,
                "1017004504",
                Arrays.asList("1017004504"),
                "日本日清NISSIN 网红泡饭 小鸡杯面 海鲜鲜虾拉面口味泡饭 90克",
                100,
                Arrays.asList("/images/products/1017004504.webp", "/images/products/1017004504-1.webp"),
                Arrays.asList(BigDecimal.valueOf(5.99), BigDecimal.valueOf(4.80)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );

        addProduct(
                nissinJapan,
                brand,
                "1017004505",
                Arrays.asList("1017004505"),
                "日本NISSIN日清 北海道限定 牛奶海鲜杯面 81g",
                100,
                Arrays.asList("/images/products/1017004505.webp", "/images/products/1017004505-1.webp", "/images/products/1017004505-2.webp"),
                Arrays.asList(BigDecimal.valueOf(5.99)),
                null,
                null
        );

        addProduct(
                nissinJapan,
                brand,
                "1017004506",
                Arrays.asList("1017004506"),
                "NISSIN日清 元祖鸡汁拉面 汤面网红方便面 速食早餐碗面 85g",
                100,
                Arrays.asList("/images/products/1017004506.webp", "/images/products/1017004506-1.webp"),
                Arrays.asList(BigDecimal.valueOf(3.20)),
                null,
                null
        );
    }

    private void instanceNoodlesMasterkong(){
        Category category = categoryRepository.findByCode("ins-noodles-china").get();
        Brand brand = brandRepository.findByCode("masterkong").get();

        addProduct(
                category,
                brand,
                "1017004511",
                Arrays.asList("1017004511"),
                "康师傅 日式豚骨面 方便面 量贩装 五包入 510g",
                100,
                Arrays.asList("/images/products/1017004511.webp", "/images/products/1017004511-1.webp", "/images/products/1017004511-2.webp"),
                Arrays.asList(BigDecimal.valueOf(6.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004512",
                Arrays.asList("1017004512"),
                "康师傅 金汤虾球面 方便面 量贩装 5包入 520g",
                100,
                Arrays.asList("/images/products/1017004512.webp", "/images/products/1017004512-1.webp", "/images/products/1017004512-2.webp"),
                Arrays.asList(BigDecimal.valueOf(6.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004513",
                Arrays.asList("1017004513"),
                "康师傅 蘑菇鲜蔬面 5包入 500g 量贩装",
                100,
                Arrays.asList("/images/products/1017004513.webp", "/images/products/1017004513-1.webp", "/images/products/1017004513-2.webp"),
                Arrays.asList(BigDecimal.valueOf(6.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );
    }

    private void instanceNoodlesTongyi(){
        Category category = categoryRepository.findByCode("ins-noodles-taiwan").get();
        Brand brand = brandRepository.findByCode("tongyi").get();

        addProduct(
                category,
                brand,
                "1017004521",
                Arrays.asList("1017004521"),
                "台湾统一 来一桶 红椒牛肉面 110g",
                100,
                Arrays.asList("/images/products/1017004521.webp", "/images/products/1017004521-1.webp"),
                Arrays.asList(BigDecimal.valueOf(1.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004521",
                Arrays.asList("1017004522"),
                "台湾统一 小浣熊干脆面 新奥尔良烤翅味 46g",
                100,
                Arrays.asList("/images/products/1017004522.webp", "/images/products/1017004522-1.webp"),
                Arrays.asList(BigDecimal.valueOf(0.89)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );
    }

    private void mateBeverage(){
        Category category = categoryRepository.findByCode("beverage").get();
        Brand brand = brandRepository.findByCode("xiangpiaopiao").get();

        addProduct(
                category,
                brand,
                "1017004531",
                Arrays.asList("1017004531"),
                "香港兰芳园 正宗港式丝袜奶茶 开盖即饮 280ml",
                100,
                Arrays.asList("/images/products/1017004531.webp", "/images/products/1017004531-1.webp"),
                Arrays.asList(BigDecimal.valueOf(1.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004532",
                Arrays.asList("1017004532"),
                "香港兰芳园 太妃榛果 鸳鸯奶茶 季节限定款 280ml",
                100,
                Arrays.asList("/images/products/1017004532.webp", "/images/products/1017004532-1.webp"),
                Arrays.asList(BigDecimal.valueOf(2.99), BigDecimal.valueOf(2.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build(),
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004533",
                Arrays.asList("1017004533"),
                "香港兰芳园 正宗港式鸳鸯咖啡奶茶 280ml",
                100,
                Arrays.asList("/images/products/1017004533.webp", "/images/products/1017004533-1.webp"),
                Arrays.asList(BigDecimal.valueOf(2.99), BigDecimal.valueOf(2.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build(),
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );
    }

    private void addProduct(Category category, Brand brand, String code, List<String> skus, String name,
                            Integer stockQuantity, List<String> imageUrls, List<BigDecimal> prices,
                            List<ProductTag> productTags, List<ProductAttribute> productAttributes) {

        Product product = Product.builder()
                .category(category)
                .brand(brand)
                .code(code)
                .name(name)
                .build();
        for (int j = 0; j < skus.size(); j++) {
            Sku sku = Sku.builder().sku(skus.get(j)).stockQuantity(stockQuantity).build();
            sku.addPrice(Price.builder().startDate(LocalDate.now()).price(prices.get(0).subtract(BigDecimal.valueOf(j))).build());
            for (int i = 1; i < prices.size(); i++) {
                sku.addPrice(Price.builder().startDate(LocalDate.now()).price(prices.get(i).subtract(BigDecimal.valueOf(j))).endDate(LocalDate.now().plusDays(100)).discountRate("20%").build());
            }
            product.addSku(sku);
        }

        for (int i = 0; i < imageUrls.size(); i++) {
            product.addImage(Image.builder().url(imageUrls.get(i)).ordering(i).build());
        }

        if (productTags != null) {
            productTags.forEach(product::addTag);
        }

        if(productAttributes != null){
            productAttributes.forEach(product::addAttribute);
        }


        productRepository.save(product);


    }
}
