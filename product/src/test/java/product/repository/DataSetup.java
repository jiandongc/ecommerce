package product.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import product.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DataSetup extends AbstractRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

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

        Category drny = Category.builder().name("豆奶 乳制品 奶茶 椰奶").code("drny").parent(yrcc).build();
        categoryRepository.save(drny);

        Category cmzy = Category.builder().name("冲饮 麦片 芝麻糊 柚子蜜").code("cmzy").parent(yrcc).build();
        categoryRepository.save(cmzy);

        Category cck = Category.builder().name("茶叶 茶包 咖啡").code("cck").parent(yrcc).build();
        categoryRepository.save(cck);
    }

    private void lsCategory() {

        Category ls = Category.builder().name("零食").code("ls").build();
        categoryRepository.save(ls);

        Category xps = Category.builder().name("休闲 膨化 薯片").code("xps").parent(ls).build();
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

        addProduct(msxdm,
                "1017004481",
                Arrays.asList("1017004481", "1017004481-1"),
                "韩国HAITAI海太 蜂蜜黄油薯片 60g",
                100,
                Arrays.asList("/images/1017004481.webp", "/images/1017004481-1.webp", "/images/1017004481-2.webp", "/images/1017004481-3.webp"),
                Arrays.asList(BigDecimal.valueOf(2.29), BigDecimal.valueOf(1.99))
        );

        addProduct(msxdm,
                "1017016711",
                Arrays.asList("1017016711"),
                "哎哟咪 小梅的零食 山药薄片 酱香牛排味 90g",
                1,
                Arrays.asList("/images/1017016711.webp"),
                Arrays.asList(BigDecimal.valueOf(2.59))
        );

        addProduct(msxdm,
                "3017008651",
                Arrays.asList("3017008651"),
                "【日本直邮】卡乐B薯条三兄弟 北海道最具人气伴手礼 10包入",
                100,
                Arrays.asList("/images/3017008651.webp"),
                Arrays.asList(BigDecimal.valueOf(16))
        );

        addProduct(msxdm,
                "1017000211",
                Arrays.asList("1017000211"),
                "台湾维力 张君雅小妹妹 点心面 日式串烧烧烤味 80g 新老包装随发",
                1,
                Arrays.asList("/images/1017000211.webp"),
                Arrays.asList(BigDecimal.valueOf(1.39))
        );

        addProduct(msxdm,
                "1017003871",
                Arrays.asList("1017003871"),
                "百事LAY'S乐事 薯片 翡翠黄瓜味 桶装 104g",
                100,
                Arrays.asList("/images/1017003871.webp"),
                Arrays.asList(BigDecimal.valueOf(3.29))
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

    private void addProduct(Category category, String code, List<String> skus, String name, Integer stockQuantity, List<String> imageUrls, List<BigDecimal> prices) {
        Product product = Product.builder()
                .category(category)
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
        productRepository.save(product);


    }
}
