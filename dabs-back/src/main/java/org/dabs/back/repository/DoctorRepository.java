package org.dabs.back.repository;

import java.util.List;

import org.dabs.back.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	
    Doctor findOneByUserId(long userId);

    List<Doctor> findAllByOrderByFirstNameAscLastName();
}
