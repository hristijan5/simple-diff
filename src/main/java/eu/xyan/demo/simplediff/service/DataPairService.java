package eu.xyan.demo.simplediff.service;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;
import eu.xyan.demo.simplediff.algorithm.DiffAlgorithmProvider;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;
import eu.xyan.demo.simplediff.domain.DataPair;
import eu.xyan.demo.simplediff.exception.NoDataPairException;
import eu.xyan.demo.simplediff.repository.DataPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataPairService {

    private final DataPairRepository dataPairRepository;

    private final DiffAlgorithmProvider diffAlgorithmProvider;

    /**
     * Saves the provided base64 data to the DB
     *
     * @param id         the Id of the data pair to save - this Id is later used for computing the differences
     * @param base64Data the Base64 encoded data
     * @param rightData  boolean stating if it is the right (or left) part of the DataPair
     * @return the DataPair object that was saved or updated in the DB
     */
    public DataPair saveData(Long id, String base64Data, boolean rightData) {
        Optional<DataPair> dataPairOptional = dataPairRepository.findById(id);
        DataPair dataPair = dataPairOptional.orElse(new DataPair());

        // We overwrite the existing value of the provided data
        dataPair.setId(id);
        if (rightData) {
            dataPair.setRightData(base64Data);
        } else {
            dataPair.setLeftData(base64Data);
        }

        return dataPairRepository.save(dataPair);
    }

    /**
     * @param algorithmType What algorithm should be used for computing the difference between data
     * @param id            The Id of the DataPair that is used to compute the data difference
     * @return The JSON result of the computed difference
     * @throws NoDataPairException Thrown when there is no left and right data pairs to compare
     */
    public DiffDtoV1 computeDiff(AlgorithmType algorithmType, Long id) throws NoDataPairException {
        Optional<DataPair> dataPairOptional = dataPairRepository.findById(id);
        DataPair dataPair = dataPairOptional.orElseThrow(() -> new NoDataPairException(id));

        return diffAlgorithmProvider.getDiffAlgorithm(algorithmType).computeDiff(dataPair.getLeftData(), dataPair
                .getRightData());
    }

}
