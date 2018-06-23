package eu.xyan.demo.simplediff.algorithm;

import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;

/**
 * Algorithm that will be used for computing the difference. Extend this Interface and register your implementation
 * to the
 *
 * @see DiffAlgorithmProvider
 */
public interface DiffAlgorithm {

    AlgorithmType getType();

    DiffDtoV1 computeDiff(String left, String right);

}
