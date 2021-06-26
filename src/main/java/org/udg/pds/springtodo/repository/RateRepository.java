package org.udg.pds.springtodo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Rate;

@Component
public interface RateRepository extends CrudRepository<Rate, Long> {
}
