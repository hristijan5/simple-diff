package eu.xyan.demo.simplediff.algorithm.exception;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;

/**
 * Exception thrown when there is no implementation of the provided
 *
 * @see AlgorithmType
 */
public class AlgorithmNotFoundException extends RuntimeException {

    public AlgorithmNotFoundException(AlgorithmType algorithmType) {
        super("Algorithm for type " + algorithmType + " not found!");
    }
}
