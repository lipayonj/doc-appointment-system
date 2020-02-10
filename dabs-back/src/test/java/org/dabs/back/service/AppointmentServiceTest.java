package org.dabs.back.service;

import java.sql.Date;
import java.sql.Time;

import org.dabs.back.exception.AppointmentNotFoundException;
import org.dabs.back.exception.InvalidAppointmentException;
import org.dabs.back.model.bind.AddAppointmentModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AppointmentServiceTest {

	private static final long VALID_ID = 1;
	private static final long INVALID_ID = -1;

	private static final String DESCRIPTION = "Pain in the back";

	@Autowired
	private AppointmentService appointmentService;

	@Test(expected = AppointmentNotFoundException.class)
	public void test_findById_invalidAppointment_shouldThrowException() throws Exception {
		this.appointmentService.getById(INVALID_ID);
	}

	@Test
	public void test_findById_ValidAppointment_success() throws Exception {
		Assert.assertNotNull(appointmentService.getById(VALID_ID));
	}

	@Test
	public void test_save_insert_validAppointment_success() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setDate(Date.valueOf("2020-02-05"));
		appointment.setDateFrom(Time.valueOf("09:00:00"));
		appointment.setDateTo(Time.valueOf("10:00:00"));
		appointment.setPatientId(1l);

		Assert.assertNotNull(appointmentService.save(appointment));
	}

	@Test(expected = InvalidAppointmentException.class)
	public void test_save_invalidAppointment_overbooked() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setDate(Date.valueOf("2020-02-03"));
		appointment.setDateFrom(Time.valueOf("13:00:00"));
		appointment.setDateTo(Time.valueOf("12:00:00"));
		appointment.setPatientId(1l);

		appointmentService.save(appointment);
	}

	@Test(expected = InvalidAppointmentException.class)
	public void test_save_invalidAppointment_unavailableAdd() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setDate(Date.valueOf("2020-02-03"));
		appointment.setDateFrom(Time.valueOf("09:00:00"));
		appointment.setDateTo(Time.valueOf("10:00:00"));
		appointment.setPatientId(1l);

		appointmentService.save(appointment);
	}

	@Test(expected = InvalidAppointmentException.class)
	public void test_save_invalidAppointment_outOfSchedule() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setDate(Date.valueOf("2020-02-05"));
		appointment.setDateFrom(Time.valueOf("07:00:00"));
		appointment.setDateTo(Time.valueOf("10:00:00"));
		appointment.setDescription(DESCRIPTION);
		appointment.setPatientId(1l);

		appointmentService.update(appointment);
	}

	@Test
	public void test_update_Appointment_success() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setId(2L);
		appointment.setDate(Date.valueOf("2020-02-04"));
		appointment.setDateFrom(Time.valueOf("09:00:00"));
		appointment.setDateTo(Time.valueOf("13:00:00"));
		appointment.setDescription(DESCRIPTION);
		appointment.setPatientId(1L);

		Assert.assertNotNull(appointmentService.update(appointment));
	}

	@Test(expected = InvalidAppointmentException.class)
	public void test_update_invalidAppointment_unavailableUpdate() throws Exception {

		AddAppointmentModel appointment = new AddAppointmentModel();
		appointment.setId(2L);
		appointment.setDate(Date.valueOf("2020-02-04"));
		appointment.setDateFrom(Time.valueOf("09:00:00"));
		appointment.setDateTo(Time.valueOf("11:00:00"));
		appointment.setDescription(DESCRIPTION);
		appointment.setPatientId(1L);

		appointmentService.update(appointment);
	}

	@Test
	public void test_getAppointmentPatient_success() throws Exception {
		Assert.assertNotNull(appointmentService.getAppointmentPatient(2, null, null));
	}

	@Test
	public void test_getAppointmentDoctor_success() throws Exception {
		Assert.assertNotNull(appointmentService.getAppointmentDoctor(1, null, null));
	}

	@Test
	public void test_deleteAppointment_success() throws Exception {
		appointmentService.deleteAppointment(5L);
	}

	@Test(expected = AppointmentNotFoundException.class)
	public void test_deleteAppointment_error() throws Exception {
		appointmentService.deleteAppointment(-1L);
	}

	@Test
	public void test_getFormDataId_success() throws Exception {
		Assert.assertNotNull(appointmentService.getFormDataId(1L));
	}

	@Test(expected = AppointmentNotFoundException.class)
	public void test_getFormDataId_error() throws Exception {
		appointmentService.getFormDataId(-1L);
	}

}
