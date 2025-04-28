package com.pfe.repository;

import com.pfe.entity.CourseFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseFileRepository extends CrudRepository<CourseFile, Long> {

}
