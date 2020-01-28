package review.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import review.Application;
import review.domain.Answer;
import review.domain.Feedback;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= NONE)
public class FeedbackRepositoryTest {

    @Autowired
    protected FeedbackRepository feedbackRepository;

    @Test
    public void setup(){
        feedbackRepository.save(
            Feedback.builder()
                    .text("想去脸上的淡斑，这个效果怎么样还是推荐其它款的？")
                    .vote(0)
                    .creationTime(LocalDateTime.now().minusDays(1L))
                    .answers(Arrays.asList(Answer.builder()
                            .text("亲，pola跟fancl 的都不错哦，另外一款就是 （FANCL新版再生亮白营养素 美白淡斑）这个 您可以甄选一下哦～").creationTime(LocalDateTime.now()).build()))
                    .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("为什么60粒的有货，180的没有啊哈哈哈哈")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(1L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，3个月份有货哦！").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("請問目前下單送來的是2018版的嗎")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(1L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("是专柜最新版的哦~").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("这一袋里有几粒？")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(2L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲 一个月的量的话是60粒，三个月的量的话是 180粒的哦，").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("Pola美白我哺乳期能吃吗")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(2L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，柜姐说是没事，但是还是问问您那边的医生比较权威哦！").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("已经来邮件说到货了，可是页面还是显示买不了")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(3L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，那应该是一上架就卖没了。。今天会到一批货哦，您可以在看一下。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("来货了吗")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(3L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，目前厂家供货不稳定，数量不多。通常一上架就卖没了。建议您可以登记缺货，然后到货上架后会自动发送消息给您的。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("感觉吃这个美白丸之后，就开始长痘痘了，停吃就不长了，有什么解决办法么？")
                        .vote(0)
                        .creationTime(LocalDateTime.now().minusDays(4L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这个可能跟您的体质不符合，这种情况还是建议亲把量给减了，试试看什么效果。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("吃这个美白丸，和健美三泉，还需不需要额外补充胶原蛋白呢？")
                        .vote(1)
                        .creationTime(LocalDateTime.now().minusDays(4L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，一般来说是有必要的，亲可以考虑选购pola家的胶原蛋白系列的产品。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("这个真的能祛斑，喝中药不能吃，是不是说喝中药那几天不要吃？")
                        .vote(1)
                        .creationTime(LocalDateTime.now().minusDays(4L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，因为中药的成份是什么在不知道的情况下怕跟美白丸的药性相冲，所以建议亲您在喝中药的时候不建议您吃任何医生指定外的药物哦～").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("想问下寄国内要多久，和酵素一起吃可以吗")
                        .vote(2)
                        .creationTime(LocalDateTime.now().minusDays(5L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，我们的备货时间为3-7天，邮寄时间为4-6天，您如果只是买美白丸的话第二天就可以发货，现在60粒的还有几个现货，可以一起吃的。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("请问这个大概什么时候能再上架呢？")
                        .vote(2)
                        .creationTime(LocalDateTime.now().minusDays(5L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲我们也不是很清楚厂家什么时候会有货～有的话我们就会马上上架了呢").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("薰衣草味是不会再补货了么？那无香还有吗？")
                        .vote(2)
                        .creationTime(LocalDateTime.now().minusDays(5L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，薰衣草的话是厂家那边说不生产了，所以现在哪里都没有货，如果哪天厂家说有继续生产的话，我们会及时的上架的哦，五香的有货哦，您可以直接下单的亲～").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("你好，请问你们有替换的贴片卖吗？谢谢")
                        .vote(2)
                        .creationTime(LocalDateTime.now().minusDays(5L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这款没有看到有替换片卖的哦，那个自带贴片是可以反复使用的哦～").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("你好，请问这个在美国使用需要电源转换器吗？")
                        .vote(3)
                        .creationTime(LocalDateTime.now().minusDays(6L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这个是用电池的哦！").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("如果是剖腹产，是什么时候开始使用呢？")
                        .vote(4)
                        .creationTime(LocalDateTime.now().minusDays(7L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这个是根据个人的骨盆来决定的，一般是在生完后就可以使用的。如果是剖腹产的话可能得一周之后才能用，主要还是看您的刀口疼痛的程度。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );


        feedbackRepository.save(
                Feedback.builder()
                        .text("这个有修复骨盆功能吗？还有这个产品的尺寸？")
                        .vote(5)
                        .creationTime(LocalDateTime.now().minusDays(7L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("您问的这几款都是有的呢～ 作用和尺寸商城也都有写 M 腰围67cm~73cm 臀围86cm~96cm L腰围73cm~79cm 臀围89cm~99cm LL 腰围78cm~86cm臀围91cm~103cm 3L 腰围86cm~94cm 臀围94cm~106cm").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("電极片可有另外買嗎？")
                        .vote(5)
                        .creationTime(LocalDateTime.now().minusDays(7L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，我们帮你询问下，如果有的话，会第一时间帮您编辑上架哦！").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("这个直径是14.2mm吗？")
                        .vote(6)
                        .creationTime(LocalDateTime.now().minusDays(8L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这款商品的商品介绍里的图片里有画，有一部分是14.2，有一部分是14.0的，图片中美瞳DIA字样的").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("你好想请问一下显色直径是多少呢？")
                        .vote(6)
                        .creationTime(LocalDateTime.now().minusDays(8L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这个是着色直径：13.2mm的哦").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("这个里面是只有一对的吗，是只能用一次吗，是说只用一次之后5-7天角质就自动脱落的吗，这五到七天之内就不再使用了对吗？")
                        .vote(7)
                        .creationTime(LocalDateTime.now().minusDays(8L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，这里面是有两对的可以使用两次，然后您这个一天用一次可以连续用两条，用完5-7天后会慢慢自动脱落，淡然这中间继续使用的话，效果会更好，").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("没有太明白，是用一对，当天敷一小时，一周以后就会有成效？上面说要坚持使用七天是什么意思？")
                        .vote(8)
                        .creationTime(LocalDateTime.now().minusDays(9L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("亲，您好，这个是一对一对用的哦，然后连续使用5天以上效果会很明显哦。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

        feedbackRepository.save(
                Feedback.builder()
                        .text("请问这个使用了之后可以运动吗就是在上面所提到的5-7天内可以运动吗")
                        .vote(9)
                        .creationTime(LocalDateTime.now().minusDays(9L))
                        .answers(Arrays.asList(Answer.builder()
                                .text("可以的呢亲，正常运动。").creationTime(LocalDateTime.now()).build()))
                        .build()
        );

    }
}