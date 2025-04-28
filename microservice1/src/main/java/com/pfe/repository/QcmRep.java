package com.pfe.repository;

import com.pfe.entity.Qcm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QcmRep extends CrudRepository<Qcm, Long> {
}
