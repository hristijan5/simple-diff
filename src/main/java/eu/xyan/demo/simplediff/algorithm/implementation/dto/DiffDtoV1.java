package eu.xyan.demo.simplediff.algorithm.implementation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.xyan.demo.simplediff.algorithm.implementation.enumeration.EqualityStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.io.Serializable;
import java.util.List;

/**
 * Ver.1 of the Transfer object representing the difference findings
 */
@Data
@Builder
public class DiffDtoV1 implements Serializable {

    private static final long serialVersionUID = -4282668044416677176L;

    private EqualityStatus equalityStatus;

    // If empty or null this json key will not be sent
    @Singular
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<InsightDtoV1> insights;

}
