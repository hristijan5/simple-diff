package eu.xyan.demo.simplediff.repository;

import eu.xyan.demo.simplediff.domain.DataPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPairRepository extends CrudRepository<DataPair, Long> {
}
