package eu.xyan.demo.simplediff.algorithm.implementation;

import eu.xyan.demo.simplediff.algorithm.AlgorithmType;
import eu.xyan.demo.simplediff.algorithm.DiffAlgorithm;
import eu.xyan.demo.simplediff.algorithm.DiffAlgorithmProvider;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.dto.InsightDtoV1;
import eu.xyan.demo.simplediff.algorithm.implementation.enumeration.EqualityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Base64;

/**
 * Linear (byte by byte) implementation of the algorithm that computes the difference between bytes.
 */
@Component
@RequiredArgsConstructor
public class LinearDiffAlgorithmImpl implements DiffAlgorithm {

    private final DiffAlgorithmProvider diffAlgorithmProvider;

    @PostConstruct
    void registerToProvider() {
        diffAlgorithmProvider.register(getType(), this);
    }

    /**
     * The type of the algorithm that this class implements. It has to be added to the
     *
     * @return The AlgorithmType this implementation represents
     * @see AlgorithmType
     */
    @Override
    public AlgorithmType getType() {
        return AlgorithmType.LINEAR;
    }

    /**
     * The difference computation between the two (left and right) parts of data.
     *
     * @param left  The left data (base64 encoded) part
     * @param right The right data (base64 encoded) part
     * @return The difference response (JSON)
     */
    @Override
    public DiffDtoV1 computeDiff(String left, String right) {
        DiffDtoV1.DiffDtoV1Builder diffDtoBuilder = DiffDtoV1.builder();

        byte[] leftBytes = decodeBase64(left);
        byte[] rightBytes = decodeBase64(right);

        // Diff the binary data
        if (Arrays.equals(leftBytes, rightBytes)) {
            diffDtoBuilder.equalityStatus(EqualityStatus.EQUAL);
        } else if (leftBytes.length == rightBytes.length) {
            diffDtoBuilder.equalityStatus(EqualityStatus.NOT_EQUAL);
            setDiffInsights(diffDtoBuilder, leftBytes, rightBytes);

        } else {
            diffDtoBuilder.equalityStatus(EqualityStatus.NOT_EQUAL_SIZE);
        }

        return diffDtoBuilder.build();
    }

    /**
     * Linear difference computation by comparing byte by byte of the provided bytes.
     *
     * @param diffDtoBuilder The builder used fo generating {@link DiffDtoV1} object
     * @param leftBytes      The left data (in bytes) used for comparison
     * @param rightBytes     The right data (in bytes) used for comparison
     */
    private void setDiffInsights(DiffDtoV1.DiffDtoV1Builder diffDtoBuilder, byte[] leftBytes, byte[] rightBytes) {
        // Set the insights
        long offset = 0;
        long length = 0;
        for (int i = 0; i < leftBytes.length; i++) {
            if (leftBytes[i] != rightBytes[i]) {
                if (length == 0) {
                    offset = i;
                }
                length += 1;
            } else {
                if (length != 0) {
                    // Use offset + 1 to handle the zero indexed byte array
                    diffDtoBuilder.insight(new InsightDtoV1(offset + 1, length));
                    length = 0;
                }
            }
        }   // End for
        // In case of different last byte
        if (length != 0) {
            diffDtoBuilder.insight(new InsightDtoV1(offset + 1, length));
        }
    }

    // It may throw exception when decoding invalid data
    // Handled and passed to the FE by the error handler
    private byte[] decodeBase64(String encodedBase64Data) {
        if (null != encodedBase64Data) {
            return Base64.getDecoder().decode(encodedBase64Data);
        } else {
            return new byte[0];
        }
    }

}
