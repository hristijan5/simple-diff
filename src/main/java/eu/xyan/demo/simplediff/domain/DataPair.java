package eu.xyan.demo.simplediff.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 */
@Entity
@Table
@Data
@NoArgsConstructor
public class DataPair implements Serializable {

    private static final long serialVersionUID = -4282668069016677176L;

    @Id
    private Long id;

    @Lob
    private String leftData;

    @Lob
    private String rightData;

}
