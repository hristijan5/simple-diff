package eu.xyan.demo.simplediff.service;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.InsightDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.enumeration.EqualityStatus;
import eu.xyan.demo.simplediff.domain.DataPair;
import eu.xyan.demo.simplediff.exception.NoDataPairException;
import eu.xyan.demo.simplediff.repository.DataPairRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml")
@SpringBootTest
public class DataPairServiceTest {

    @Autowired
    private DataPairService dataPairService;

    @Autowired
    private DataPairRepository dataPairRepository;

    @Before
    public void cleanDB() {
        // make sure DB is clean before every test
        dataPairRepository.deleteAll();
    }

    @Test
    public void saveData() {
        // Left data
        String encodedLeftData = getBase64Data("left data");

        dataPairService.saveData(1L, encodedLeftData, false);
        Assert.assertTrue(dataPairRepository.existsById(1L));

        Optional<DataPair> dataPair = dataPairRepository.findById(1L);
        Assert.assertTrue(dataPair.isPresent());
        Assert.assertEquals(dataPair.get().getLeftData(), encodedLeftData);
        Assert.assertNull(dataPair.get().getRightData());

        // Right data
        String encodedRightData = getBase64Data("right data");

        dataPairService.saveData(1L, encodedRightData, true);
        dataPair = dataPairRepository.findById(1L);
        Assert.assertTrue(dataPair.isPresent());
        Assert.assertEquals(dataPair.get().getLeftData(), encodedLeftData);
        Assert.assertEquals(dataPair.get().getRightData(), encodedRightData);

        // Update check
        dataPairService.saveData(1L, encodedLeftData, true);
        dataPair = dataPairRepository.findById(1L);
        Assert.assertTrue(dataPair.isPresent());
        Assert.assertEquals(dataPair.get().getLeftData(), encodedLeftData);
        Assert.assertEquals(dataPair.get().getRightData(), encodedLeftData);
    }

    @Test
    public void computeDiff() throws NoDataPairException {
        // Equal data
        String encodedLeftData = getBase64Data("left data");
        dataPairService.saveData(1L, encodedLeftData, false);
        dataPairService.saveData(1L, encodedLeftData, true);

        DiffDtoV1.DiffDtoV1Builder diffDtoV1Builder = DiffDtoV1.builder().equalityStatus(EqualityStatus.EQUAL);
        Assert.assertEquals(dataPairService.computeDiff(AlgorithmType.LINEAR, 1L), diffDtoV1Builder.build());

        // Non equal data
        encodedLeftData = getBase64Data("the left data");
        dataPairService.saveData(1L, encodedLeftData, false);
        Assert.assertEquals(dataPairService.computeDiff(AlgorithmType.LINEAR, 1L), diffDtoV1Builder.equalityStatus
                (EqualityStatus.NOT_EQUAL_SIZE).build());

        // Same size non equal data
        encodedLeftData = getBase64Data("the great left data");
        String encodedRightData = getBase64Data("das great righ datE");
        dataPairService.saveData(1L, encodedLeftData, false);
        dataPairService.saveData(1L, encodedRightData, true);

        // Check diff insights
        List<InsightDtoV1> insights = new ArrayList<>();
        insights.add(new InsightDtoV1(1L, 3L));     // das
        insights.add(new InsightDtoV1(11L, 4L));    // righ
        insights.add(new InsightDtoV1(19L, 1L));    // E

        Assert.assertEquals(dataPairService.computeDiff(AlgorithmType.LINEAR, 1L), diffDtoV1Builder.equalityStatus
                (EqualityStatus.NOT_EQUAL).insights(insights).build());
    }

    @Test(expected = NoDataPairException.class)
    public void computeDiffWithNoExistingId() throws NoDataPairException {
        DiffDtoV1 diffDto = DiffDtoV1.builder().equalityStatus(EqualityStatus.EQUAL).build();
        Assert.assertEquals(dataPairService.computeDiff(AlgorithmType.LINEAR, 2L), diffDto);
    }

    private String getBase64Data(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}
