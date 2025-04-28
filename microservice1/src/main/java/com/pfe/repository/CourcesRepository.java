package com.pfe.repository;

import com.pfe.entity.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourcesRepository extends JpaRepository<Courses,Long> {
}
