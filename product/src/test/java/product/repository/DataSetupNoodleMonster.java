package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataSetupNoodleMonster extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductGroupRepository productGroupRepository;

    @Autowired
    private VatRepository vatRepository;

    @Test
    @Rollback(false)
    public void initialise() {
//        vat();
//
//        cookingNoodlesCategory();
//        instanceNoodlesCategory();
//        mateCategory();
        brands();

//        instanceNoodlesSamyang();
//        instanceNoodlesNongshim();
//        instanceNoodlesNissin();
//        instanceNoodlesMasterkong();
//        instanceNoodlesTongyi();
//
//        noodlesZeng();
//        noodlesGuangyou();
//        noodlesJinpaiganliu();
//        noodlesOttogi();
//
//        mateBeverage();

    }

    private void vat(){
        vatRepository.save(Vat.builder().name("uk-standard").rate(20).build());
        vatRepository.save(Vat.builder().name("uk-reduced-rate").rate(5).build());
        vatRepository.save(Vat.builder().name("uk-zero-rate").rate(0).build());
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
//        Brand samyang = Brand.builder().name("三養食品").code("samyang").startDate(LocalDate.now()).imageUrl("/images/brand/samyang.png").description("三养食品是一个韩国食品品牌，创立于1961年，所属公司韩国三养集团。2014年，因火鸡面超辣口感，在中国成为网红泡面之一。2017年，从韩国政府获得“1亿美元出口之塔”奖。2018年4月15日，三养食品成3X3黄金联赛伙伴").build();
//        brandRepository.save(samyang);
//
//        Brand nongshim = Brand.builder().name("農心").code("nongshim").startDate(LocalDate.now()).imageUrl("/images/brand/nongshim.jpg").description("株式会社农心成立于1965年，是以制造方便面、膨化食品（饼干）及其它食品加工为主导产业的韩国大型食品生产集团。").build();
//        brandRepository.save(nongshim);
//
//        Brand nissin = Brand.builder().name("日清食品").code("nissin").startDate(LocalDate.now()).imageUrl("/images/brand/nissin.png").description("日清食品是一间日本食品公司，以推出各种即食食品著名，是总部位于大阪府大阪市淀川区的日本食品加工公司。由开发了世界最早的方便面Chicken Ramen的安藤百福创办。").build();
//        brandRepository.save(nissin);
//
//        Brand tongyi = Brand.builder().name("統一").code("tongyi").startDate(LocalDate.now()).imageUrl("/images/brand/tongyi.jpg").description("统一企业正式成立于中国台湾地区台南市永康区,其总部位于台南市永康区。公司产品主要有饮料和方便面。").build();
//        brandRepository.save(tongyi);
//
//        Brand masterKong = Brand.builder().name("康师傅").code("masterkong").startDate(LocalDate.now()).imageUrl("/images/brand/masterkong.jpg").description("康师傅自1992年研发生产出第一包方便面后，迅速成长为国内乃至全球最大的方便面生产销售企业。").build();
//        brandRepository.save(masterKong);
//
//        Brand xiangpiaopiao = Brand.builder().name("香飘飘").code("xiangpiaopiao").startDate(LocalDate.now()).imageUrl("/images/brand/xiangpiaopiao.jpg").description("香飘飘是香飘飘食品股份有限公司旗下杯装奶茶品牌，成立于2005年，专业从事奶茶产品的研发、生产和销售。").build();
//        brandRepository.save(xiangpiaopiao);
//
//        Brand zeng = Brand.builder().name("曾拌面").code("zeng").startDate(LocalDate.now()).imageUrl("/images/brand/zeng.jpg").description("主持过大小美食节目的曾国城，嚐遍各国美食，独独衷爱「面」，累积了30年闻面香、观面相、品面嚐的心得，更心心念念的是如何让大家吃到值得期待的面。于是，「曾拌面」在他的坚持之下诞生了。曾拌面改良关庙面的波浪面体，手工日晒、自然风乾，让它更弹牙的同时，充分吸附酱汁。「一个人可以吃得很简单，但绝对不随便」是曾拌面的初衷，也是希望传达的心意。").build();
//        brandRepository.save(zeng);
//
//        Brand jinpaiganliu = Brand.builder().name("金牌干溜").code("jinpaiganliu").startDate(LocalDate.now()).imageUrl("/images/brand/jinpaiganliu.jpg").description(" 麦香四溢、韧性劲道. 重庆特色、味道醇正. 细腻口感、饱含嚼劲. 选材挑剔、舌尖美味.").build();
//        brandRepository.save(jinpaiganliu);
//
//        Brand guangyou = Brand.builder().name("光友薯业").code("guangyou").startDate(LocalDate.now()).imageUrl("/images/brand/guangyou.png").description("1960年12月，四川省三台县邹家大院诞生了一个小男孩，从小吃红薯长大，与红薯结下了不解之缘。这个“小男孩”就是“方便粉丝发明人”、“方便粉丝专家”邹光友。").build();
//        brandRepository.save(guangyou);
//
//        Brand ottogi = Brand.builder().name("OTTOGI").code("ottogi").startDate(LocalDate.now()).imageUrl("/images/brand/ottogi.jpeg").description("OTTOGI韓國不倒翁成立於1969年,從那時起不倒翁公司就已經著眼於改善韓國大眾的飲食習慣成長為追求食品的更高品質、更富營養、更加高級的專業化食品公司。").build();
//        brandRepository.save(ottogi);

//        Brand laomanoodle = Brand.builder().name("老媽拌麵").code("laomanoodle").startDate(LocalDate.now()).imageUrl("/images/brand/laomanoodle.png").description("老媽拌麵，運用好食材與對料理的熱愛，嚴選台南日曬關廟麵與其Q彈不軟爛的特性，搭配自家熬製醬包，僅要六分鐘就能享受到快速、隨興、能帶給家人美味的食物。因此廣受消費者喜愛，短五年內全球便銷售了一億包。海外銷售更是遍布美國、加拿大、英國、中國、香港、澳洲、新加坡等地。<br/><br/>有華人的地方就有老媽拌麵。").build();
//        brandRepository.save(laomanoodle);

//        Brand kiki = Brand.builder().name("KiKi拌麵").code("kiki").startDate(LocalDate.now()).imageUrl("/images/brand/kiki.png").description("KiKi拌麵由由台灣知名品牌「KiKi食品雜貨」研發及創立，舒淇代言。為了做出最好吃的乾拌麵，研發者們反覆試吃過的麵條應該超過100種，最後「台南手工日曬麵」在各個評分項目中勝出。<br/><br/>台南是個日曬充足的美食寶庫，自早年就是古法製麵曬麵的產地，透過天然陽光日曬風乾，即能達到天然防腐功能，不同於一般麵線需要加入大量鹽巴防腐，因此，做過日光浴的手工麵沒有你看不懂名字的添加物，僅以麵粉和水手工製作，絕對安全健康。<br/><br/>製麵過程中，最重要的就是日曬。先將手工折成扇形的麵體一一擺放在鏤空的竹篩上，經過兩天充滿正能量的強烈日照，每兩小時要翻面一次，讓麵線都能均勻受熱，吃得苦中苦，才能蛻變成為麵上麵-口感Q彈、久煮不爛、絕對天然又好吃。").build();
//        brandRepository.save(kiki);

//        Brand weilih = Brand.builder().name("維力食品").code("weilih").startDate(LocalDate.now()).imageUrl("/images/brand/weilih.png").description("").build();
//        brandRepository.save(weilih);

//        Brand itsuki = Brand.builder().name("五木食品").code("itsuki").startDate(LocalDate.now()).imageUrl("/images/brand/itsuki.jpg").description("").build();
//        brandRepository.save(itsuki);

//        Brand haohuanluo = Brand.builder().name("好欢螺").code("haohuanluo").startDate(LocalDate.now()).imageUrl("/images/brand/haohuanluo.png").description("").build();
//        brandRepository.save(haohuanluo);

        Brand wang = Brand.builder().name("Wang Korea").code("wang").startDate(LocalDate.now()).imageUrl("/images/brand/wang.png").description("").build();
        brandRepository.save(wang);


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
                Arrays.asList("1017004482"),
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

        productRepository.findByCategoryCode("ins-noodles-korea").forEach(product ->
            productGroupRepository.add(1L, "tier", product.getId())
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

    private void noodlesZeng(){
        Category category = categoryRepository.findByCode("noodles-taiwan").get();
        Brand brand = brandRepository.findByCode("zeng").get();

        addProduct(
                category,
                brand,
                "1017004541",
                Arrays.asList("1017004541"),
                "曾拌面 青葱椒麻拌面 4包入 464g 台湾手工面",
                100,
                Arrays.asList("/images/products/1017004541.webp", "/images/products/1017004541-1.webp"),
                Arrays.asList(BigDecimal.valueOf(10.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004542",
                Arrays.asList("1017004542"),
                "曾拌面 胡麻酱香拌面 4包入 508g 台湾手工面",
                100,
                Arrays.asList("/images/products/1017004542.webp", "/images/products/1017004542-1.webp"),
                Arrays.asList(BigDecimal.valueOf(10.99), BigDecimal.valueOf(8.79)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );
    }

    private void noodlesGuangyou(){
        Category category = categoryRepository.findByCode("noodles-china").get();
        Brand brand = brandRepository.findByCode("guangyou").get();

        addProduct(
                category,
                brand,
                "1017004551",
                Arrays.asList("1017004551"),
                "光友 非油炸 绵阳米粉 牛肉米粉 4包入 540g",
                100,
                Arrays.asList("/images/products/1017004551.webp", "/images/products/1017004551-1.webp", "/images/products/1017004551-2.webp"),
                Arrays.asList(BigDecimal.valueOf(5.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004552",
                Arrays.asList("1017004552"),
                "光友 重庆小面 麻辣面 桶装 105g",
                100,
                Arrays.asList("/images/products/1017004552.webp", "/images/products/1017004552-1.webp", "/images/products/1017004552-2.webp"),
                Arrays.asList(BigDecimal.valueOf(1.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004553",
                Arrays.asList("1017004553"),
                "光友 非油炸 自热酸辣粉 手工鲜粉 230g",
                100,
                Arrays.asList("/images/products/1017004553.webp", "/images/products/1017004553-1.webp"),
                Arrays.asList(BigDecimal.valueOf(4.39)),
                null,
                null
        );

    }

    private void noodlesJinpaiganliu(){
        Category category = categoryRepository.findByCode("noodles-china").get();
        Brand brand = brandRepository.findByCode("jinpaiganliu").get();

        addProduct(
                category,
                brand,
                "1017004561",
                Arrays.asList("1017004561"),
                "金牌干溜 金牌酸辣粉 四川特产 300g",
                100,
                Arrays.asList("/images/products/1017004561.webp", "/images/products/1017004561-1.webp", "/images/products/1017004561-2.webp"),
                Arrays.asList(BigDecimal.valueOf(4.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004571",
                Arrays.asList("1017004571"),
                "金牌干溜 重庆小面 180g",
                100,
                Arrays.asList("/images/products/1017004571.webp", "/images/products/1017004571-1.webp"),
                Arrays.asList(BigDecimal.valueOf(4.39)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );
    }

    private void noodlesOttogi(){
        Category category = categoryRepository.findByCode("noodles-korea").get();
        Brand brand = brandRepository.findByCode("ottogi").get();

        addProduct(
                category,
                brand,
                "1017004581",
                Arrays.asList("1017004581"),
                "韩国OTTOGI不倒翁 炸酱面 北京风味 5包入 675g",
                100,
                Arrays.asList("/images/products/1017004581.webp", "/images/products/1017004581-1.webp", "/images/products/1017004581-2.webp"),
                Arrays.asList(BigDecimal.valueOf(4.99)),
                Arrays.asList(
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                null
        );

        addProduct(
                category,
                brand,
                "1017004582",
                Arrays.asList("1017004582"),
                "韩国OTTOGI 不倒翁芝士拉面 4连包",
                1,
                Arrays.asList("/images/products/1017004582.webp", "/images/products/1017004582-1.webp"),
                Arrays.asList(BigDecimal.valueOf(4.99), BigDecimal.valueOf(3.99)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                null
        );
    }

    private void addProduct(Category category, Brand brand, String code, List<String> skus, String name,
                            Integer stockQuantity, List<String> imageUrls, List<BigDecimal> prices,
                            List<ProductTag> productTags, List<ProductAttribute> productAttributes) {

        List<Vat> vats = vatRepository.findAll();
        Random random = new Random();
        Product product = Product.builder()
                .category(category)
                .brand(brand)
                .code(code)
                .name(name)
                .vat(vats.get(random.nextInt(vats.size())))
                .startDate(LocalDate.now())
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
