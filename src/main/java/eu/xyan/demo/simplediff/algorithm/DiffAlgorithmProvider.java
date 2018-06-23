package eu.xyan.demo.simplediff.algorithm;

import eu.xyan.demo.simplediff.algorithm.exception.AlgorithmNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * The provider of the algorithm to be used for computing the difference. Register the implementation of own
 * algorithm here.
 */
@Service
public class DiffAlgorithmProvider {

    private final Map<AlgorithmType, DiffAlgorithm> diffAlgorithmRegistry = new HashMap<>();

    /**
     * Retrieve registered algorithms for difference computing
     *
     * @param algorithmType The algorithm type to retrieve
     * @return The registered implementation of the AlgorithType
     * @see eu.xyan.demo.simplediff.algorithm.AlgorithmType
     */
    public DiffAlgorithm getDiffAlgorithm(AlgorithmType algorithmType) {
        return diffAlgorithmRegistry.computeIfAbsent(algorithmType, aType -> {
            throw new AlgorithmNotFoundException(aType);
        });
    }

    /**
     * Register own implementation of the difference computing algorithm
     *
     * @param algorithmType The type of the algorith that is registered
     * @param diffAlgorithm The implementation of the
     * @see eu.xyan.demo.simplediff.algorithm.DiffAlgorithm
     */
    public void register(AlgorithmType algorithmType, DiffAlgorithm diffAlgorithm) {
        // Replace the existing algorithm provider. We could throw exception if same provider was already registered
        diffAlgorithmRegistry.put(algorithmType, diffAlgorithm);
    }
}
