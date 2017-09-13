package product.repository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import product.Application;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= NONE)
@Transactional
public abstract class AbstractRepositoryTest {

}
