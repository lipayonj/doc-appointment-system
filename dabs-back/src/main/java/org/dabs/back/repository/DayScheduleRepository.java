package org.dabs.back.repository;

import org.dabs.back.entity.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
}
