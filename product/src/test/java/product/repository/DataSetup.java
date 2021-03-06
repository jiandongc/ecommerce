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

public class DataSetup extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @Rollback(false)
    public void initialise() {
        // 零食
        lsCategory();
        // 饮料
        ylCategory();

    }

    private void ylCategory() {
        Category yl = Category.builder().name("饮料").code("yl").build();
        categoryRepository.save(yl);

        Category yrcc = Category.builder().name("饮料 乳品 冲调 茶类").code("yrcc").parent(yl).build();
        categoryRepository.save(yrcc);

        Category ysqg = Category.builder().name("饮料 酸梅汤 汽水 果汁").code("ysqg").parent(yrcc).build();
        categoryRepository.save(ysqg);

        Brand vitasoy = Brand.builder()
                .name("VITASOY维他奶")
                .code("vitasoy")
                .imageUrl("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/brand/brand-vitasoy.png")
                .description("维他奶（英文：Vitasoy）1940年至今，是香港家喻户晓的饮料品牌，该豆奶饮料自1940年开始在香港生产，现时生产厂房遍及中国大陆、香港、澳大利亚和美国。\n" +
                "维他奶包括果汁、牛奶、茶类饮品，汽水、蒸馏水及豆腐等产品的品牌。")
                .startDate(LocalDate.now())
                .build();
        brandRepository.save(vitasoy);

        addProduct(
                ysqg,
                vitasoy,
                "1110003496",
                Arrays.asList("1110003496"),
                "香港VITA维他 柠檬茶 6盒装 1500ml",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003496.webp", "https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003496-1.webp"),
                Arrays.asList(BigDecimal.valueOf(4.09)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Collections.emptyList()
        );

        Category drny = Category.builder().name("豆奶 乳制品 奶茶 椰奶").code("drny").parent(yrcc).build();
        categoryRepository.save(drny);

        Brand royalboat = Brand.builder().name("泰国ROYAL BOAT").code("royalboat").startDate(LocalDate.now()).build();
        brandRepository.save(royalboat);

        addProduct(
                drny,
                royalboat,
                "1110003493",
                Arrays.asList("1110003493"),
                "泰国ROYAL BOAT 南洋泰式奶茶 480ml",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003493.webp"),
                Arrays.asList(BigDecimal.valueOf(1.04)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Collections.emptyList()
        );

        Category cmzy = Category.builder().name("冲饮 麦片 芝麻糊 柚子蜜").code("cmzy").parent(yrcc).build();
        categoryRepository.save(cmzy);

        Brand nf = Brand.builder().name("南方").code("nf").startDate(LocalDate.now()).build();
        brandRepository.save(nf);

        addProduct(
                cmzy,
                nf,
                "1110003495",
                Arrays.asList("1110003495"),
                "南方 黑芝麻糊 无糖 560g",
                100,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003495.webp"),
                Arrays.asList(BigDecimal.valueOf(7.79)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Collections.emptyList()
        );

        Category cck = Category.builder().name("茶叶 茶包 咖啡").code("cck").parent(yrcc).build();
        categoryRepository.save(cck);

        Brand sdb = Brand.builder().name("三顿半").code("sdb").startDate(LocalDate.now()).build();
        brandRepository.save(sdb);

        addProduct(
                cck,
                sdb,
                "1110003494",
                Arrays.asList("1110003494"),
                "【全网最低价】三顿半 1-6号超即溶精品咖啡冷萃冷泡拿铁纯黑咖啡 24颗装 72g",
                2,
                Arrays.asList("https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003494.webp", "https://s3-eu-west-1.amazonaws.com/cawaii.co.uk/images/1110003494-1.webp"),
                Arrays.asList(BigDecimal.valueOf(37.99), BigDecimal.valueOf(34.99)),
                Arrays.asList(
                        ProductTag.builder().tag("促销").startDate(LocalDate.now()).colorHex("#F0C14B").build(),
                        ProductTag.builder().tag("新款到货").startDate(LocalDate.now()).colorHex("#C14BF0").build()
                ),
                Collections.emptyList()
        );
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
