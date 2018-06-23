package eu.xyan.demo.simplediff.algorithm.implementation;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;
import eu.xyan.demo.simplediff.algorithm.DiffAlgorithmProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml")
@SpringBootTest
public class LinearDiffAlgorithmImplTest {

    @Autowired
    private DiffAlgorithmProvider diffAlgorithmProvider;

    @Test
    public void testRegisteringToProvider() {
        Assert.assertNotNull(diffAlgorithmProvider.getDiffAlgorithm(AlgorithmType.LINEAR));
    }

}
