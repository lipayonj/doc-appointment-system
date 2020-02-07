package org.dabs.back.repository;

import org.dabs.back.entity.WeekSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekScheduleRepository extends JpaRepository<WeekSchedule, Long> {
}
