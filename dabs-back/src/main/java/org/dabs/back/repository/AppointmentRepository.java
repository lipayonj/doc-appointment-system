package org.dabs.back.repository;

import java.util.Date;
import java.util.List;

import org.dabs.back.entity.Appointment;
import org.dabs.back.model.bind.AddAppointmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	/**
	 * 
	 * @param model
	 * @return
	 */
	@Modifying(clearAutomatically = true)
	@Query(" update Appointment as a set "
			// + " a.patient.id = :#{#model.patientId}, "
			+ " a.date = :#{#model.date}, " + " a.dateFrom = :#{#model.dateFrom}, " + " a.dateTo = :#{#model.dateTo}, "
			+ " a.description = :#{#model.description} " + " where a.id = :#{#model.id} ")
	int updateAppointment(AddAppointmentModel model);

	/**
	 * 
	 * @param patientId
	 * @param start
	 * @param end
	 * @param pageable
	 * @return
	 */
	@Query("SELECT A FROM Appointment as A WHERE A.patient.user.id = :patientId "
			+ " AND (coalesce(:start,null) is null OR A.date >= :start) AND (coalesce(:end,null) is null OR A.date <= :end)")
	List<Appointment> findAppointmentForPatientBetween(@Param("patientId") Long patientId, @Param("start") Date start,
			@Param("end") Date end);

	/**
	 * 
	 * @param doctorId
	 * @param start
	 * @param end
	 * @param pageable
	 * @return
	 */
	@Query("SELECT A FROM Appointment as A WHERE A.doctor.user.id = :doctorId "
			+ " AND (coalesce(:start,null) is null OR A.date >= :start) AND (coalesce(:end,null) is null OR A.date <= :end)")
	List<Appointment> findAppointmentForDoctorBetween(@Param("doctorId") Long doctorId, @Param("start") Date start,
			@Param("end") Date end);

	/**
	 * 
	 * @param patientId
	 * @param date
	 * @return
	 */
	int countByPatientIdAndDate(Long patientId, Date date);

	/**
	 * 
	 * @param patientId
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	int countByDoctorIdAndIdNotAndDateAndDateFromGreaterThanEqualAndDateToLessThanEqual(Long doctorId, 
			Long patientId, Date date, Date from, Date to);
	
	/**
	 * 
	 * @param patientId
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	int countByDoctorIdAndDateAndDateFromGreaterThanEqualAndDateToLessThanEqual(Long doctorId, Date date, Date from, Date to);
}
