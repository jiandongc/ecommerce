package product.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import product.Application;
import product.repository.*;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment= DEFINED_PORT)
public abstract class AbstractControllerTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected BrandRepository brandRepository;

    @Autowired
    protected ImageTypeRepository imageTypeRepository;

    @Autowired
    protected KeyRepository keyRepository;

    @Autowired
    protected AttributeRepository attributeRepository;

    @Autowired
    protected ProductGroupRepository productGroupRepository;

    protected void cleanUp(){
        productGroupRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        brandRepository.deleteAll();
        imageTypeRepository.deleteAll();
        attributeRepository.deleteAll();
        keyRepository.deleteAll();
    }

}
