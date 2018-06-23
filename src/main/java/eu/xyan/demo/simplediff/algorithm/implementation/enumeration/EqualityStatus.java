package eu.xyan.demo.simplediff.algorithm.implementation.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Equality status sent to the response within
 *
 * @see eu.xyan.demo.simplediff.algorithm.implementation.dto.DiffDtoV1
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum EqualityStatus {

    EQUAL, NOT_EQUAL_SIZE, NOT_EQUAL;

    private static Map<String, EqualityStatus> namesMap = new HashMap<>(3);

    static {
        namesMap.put("equal", EQUAL);
        namesMap.put("not equal size", NOT_EQUAL_SIZE);
        namesMap.put("not equal", NOT_EQUAL);
    }

    @JsonCreator
    public static EqualityStatus forValue(String value) {
        return namesMap.get(value);
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, EqualityStatus> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null; // or fail
    }
}
