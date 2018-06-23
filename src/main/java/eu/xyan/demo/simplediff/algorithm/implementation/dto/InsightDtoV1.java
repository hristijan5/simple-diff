package eu.xyan.demo.simplediff.algorithm.implementation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Ver.1 of the computed difference insights - part of the
 * @see DiffDtoV1
 */
@Data
@AllArgsConstructor
public class InsightDtoV1 implements Serializable {

    private static final long serialVersionUID = -4285555069016677176L;

    private Long offset;

    private Long length;

}
