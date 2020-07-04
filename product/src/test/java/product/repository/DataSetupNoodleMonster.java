package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
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
//        cookingNoodles();
//        instanceNoodles();
//        mate();

        brands();

    }

    private void cookingNoodles() {
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

    private void instanceNoodles() {
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

    private void mate() {
        Category mate = Category.builder().name("面條伴侶").description("Noodles mate").code("noodles-mate").build();
        categoryRepository.save(mate);

        Category beverage = Category.builder().name("飲品 Beverage").description("Beverage").code("beverage").parent(mate).build();
        categoryRepository.save(beverage);

        Category luwei = Category.builder().name("滷味 Braised dishes").description("Braised dishes").code("luwei").parent(mate).build();
        categoryRepository.save(luwei);
    }

    private void brands(){
//        Brand samyang = Brand.builder().name("三養食品").code("samyang").startDate(LocalDate.now()).imageUrl("/images/brand/samyang.png").build();
//        brandRepository.save(samyang);

//        Brand nongshim = Brand.builder().name("農心").code("nongshim").startDate(LocalDate.now()).imageUrl("/images/brand/nongshim.jpg").build();
//        brandRepository.save(nongshim);

//        Brand nissin = Brand.builder().name("日清食品").code("nissin").startDate(LocalDate.now()).imageUrl("/images/brand/nissin.png").build();
//        brandRepository.save(nissin);
//
//        Brand tongyi = Brand.builder().name("統一").code("tongyi").startDate(LocalDate.now()).imageUrl("/images/brand/tongyi.jpg").build();
//        brandRepository.save(tongyi);

        Brand masterKong = Brand.builder().name("康师傅").code("masterkong").startDate(LocalDate.now()).imageUrl("/images/brand/masterkong.jpg").build();
        brandRepository.save(masterKong);


    }

    private void lsCategory() {

        Category ls = Category.builder().name("零食").code("ls").build();
        categoryRepository.save(ls);

        Category xps = Category.builder().name("休闲 膨化 薯片").code("xps").parent(ls).build();
        xps.addCategoryAttribute(CategoryAttribute.builder().key("Salty").ordering(0).build());
        categoryRepository.save(xps);

        Category bgt = Category.builder().name("饼干 糕点 甜品").code("bgt").parent(ls).build();
        categoryRepository.save(bgt);

        Category rhjm = Category.builder().name("肉类 海味 坚果 蜜饯").code("rhjm").parent(ls).build();
        categoryRepository.save(rhjm);

        Category yrwq = Category.builder().name("硬糖 软糖 味觉糖 巧克力").code("yrwq").parent(ls).build();
        categoryRepository.save(yrwq);

        Category yzq = Category.builder().name("月饼 粽子 青团").code("yzq").parent(ls).build();
        categoryRepository.save(yzq);


        //饼干 糕点 甜品
        Category bcks = Category.builder().name("饼干 脆棒 烤馍片 酥饼").code("bcks").parent(bgt).build();
        categoryRepository.save(bcks);

        Category fms = Category.builder().name("凤梨酥 麻薯 沙琪玛").code("fms").parent(bgt).build();
        categoryRepository.save(fms);

        Category dmpt = Category.builder().name("蛋糕 面包 派 铜锣烧").code("dmpt").parent(bgt).build();
        categoryRepository.save(dmpt);

        //月饼 粽子 青团
        Category yb = Category.builder().name("月饼限时特价").code("yb").parent(yzq).build();
        categoryRepository.save(yb);

        Category zz = Category.builder().name("粽子").code("zz").parent(yzq).build();
        categoryRepository.save(zz);

        Category qt = Category.builder().name("青团").code("qt").parent(yzq).build();
        categoryRepository.save(qt);

        //休闲 膨化 薯片
        Category msxdm = Category.builder().name("膨化 薯片 虾条 点心面 米果").code("msxdm").parent(xps).build();
        categoryRepository.save(msxdm);

        Brand haitai = Brand.builder().name("HAITAI海太").code("haitai").startDate(LocalDate.now()).build();
        brandRepository.save(haitai);

        addProduct(
                msxdm,
                haitai,
                "1017004481",
                Arrays.asList("1017004481", "1017004481-1"),
                "韩国HAITAI海太 蜂蜜黄油薯片 60g",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017004481.webp", "https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017004481-1.webp", "https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017004481-2.webp", "https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017004481-3.webp"),
                Arrays.asList(BigDecimal.valueOf(2.29), BigDecimal.valueOf(1.99)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("light").build())
        );

        Brand aiyomi = Brand.builder().name("小梅的零食").code("aiyomi").startDate(LocalDate.now()).build();
        brandRepository.save(aiyomi);

        addProduct(
                msxdm,
                aiyomi,
                "1017016711",
                Arrays.asList("1017016711"),
                "哎哟咪 小梅的零食 山药薄片 酱香牛排味 90g",
                1,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017016711.webp"),
                Arrays.asList(BigDecimal.valueOf(2.59)),
                Arrays.asList(
                        ProductTag.builder().tag("学生最爱").startDate(LocalDate.now()).colorHex("#4BF0C1").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("none").build())
        );

        Brand calbee = Brand.builder().name("卡乐B").code("calbee").startDate(LocalDate.now()).build();
        brandRepository.save(calbee);

        addProduct(
                msxdm,
                calbee,
                "3017008651",
                Arrays.asList("3017008651"),
                "【日本直邮】卡乐B薯条三兄弟 北海道最具人气伴手礼 10包入",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/3017008651.webp"),
                Arrays.asList(BigDecimal.valueOf(16)),
                Arrays.asList(ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("none").build())
        );

        addProduct(
                msxdm,
                null,
                "1017000211",
                Arrays.asList("1017000211"),
                "台湾维力 张君雅小妹妹 点心面 日式串烧烧烤味 80g 新老包装随发",
                1,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017000211.webp"),
                Arrays.asList(BigDecimal.valueOf(1.39)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build()
                ),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("strong").build())
        );

        Brand lays = Brand.builder()
                .name("Lay's乐事")
                .code("lay-s")
                .imageUrl("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/brand/lays.png")
                .description("乐事（Lay's，台灣初使用波卡（POCA）品牌、於英国與爱尔兰称为Walkers、埃及称为Chipsy、越南称为Poca、以色列称为Tapuchips、墨西哥称为Sabritas），" +
                        "是一种马铃薯片系列的商品名，也是一个创立于1938年的马铃薯片的品牌。自1965年起乐事薯片作为百事公司所拥有的菲多利的子品牌销售。其他菲多利旗下的商品包含多力多滋、波乐、奇多等。")
                .startDate(LocalDate.now())
                .build();
        brandRepository.save(lays);

        addProduct(
                msxdm,
                lays,
                "1017003871",
                Arrays.asList("1017003871"),
                "百事LAY'S乐事 薯片 翡翠黄瓜味 桶装 104g",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017003871.webp"),
                Arrays.asList(BigDecimal.valueOf(3.29)),
                Arrays.asList(
                        ProductTag.builder().tag("学生最爱").startDate(LocalDate.now()).colorHex("#4BF0C1").build()
                ),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("strong").build())
        );

        addProduct(
                msxdm,
                lays,
                "1017003872",
                Arrays.asList("1017003872"),
                "Lay's Classic Potato Chips - 15.75oz",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1017003872.webp"),
                Arrays.asList(BigDecimal.valueOf(2.29), BigDecimal.valueOf(1.99)),
                Arrays.asList(
                        ProductTag.builder().tag("学生最爱").startDate(LocalDate.now()).colorHex("#4BF0C1").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Arrays.asList(ProductAttribute.builder().key("Salty").value("strong").build())
        );


        Category xdmg = Category.builder().name("威化 蛋卷 麻花 锅巴").code("xdmg").parent(xps).build();
        categoryRepository.save(xdmg);


        // 肉类 海味 坚果 蜜饯
        Category rrrl = Category.builder().name("肉类 肉肠 肉干 卤味").code("rrrl").parent(rhjm).build();
        categoryRepository.save(rrrl);

        Category hyh = Category.builder().name("海味小食 鱼肠 海苔").code("hyh").parent(rhjm).build();
        categoryRepository.save(hyh);

        Category jhgc = Category.builder().name("坚果 花生 瓜子 炒货").code("jhgc").parent(rhjm).build();
        categoryRepository.save(jhgc);

        Category smds = Category.builder().name("蔬果干 蜜饯 豆制品 素肉").code("smds").parent(rhjm).build();
        categoryRepository.save(smds);

        //硬糖 软糖 味觉糖 巧克力
        Category ggg = Category.builder().name("果冻 果冻爽 龟苓膏").code("ggg").parent(yrwq).build();
        categoryRepository.save(ggg);
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
            sku.addAttribute(SkuAttribute.builder().key("Size").value(Integer.toString(j)).build());
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
