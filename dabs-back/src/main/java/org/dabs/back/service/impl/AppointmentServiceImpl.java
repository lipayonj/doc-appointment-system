package org.dabs.back.service.impl;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.dabs.back.entity.Appointment;
import org.dabs.back.entity.DayOfWeek;
import org.dabs.back.entity.DaySchedule;
import org.dabs.back.entity.Patient;
import org.dabs.back.exception.AppointmentNotFoundException;
import org.dabs.back.exception.InvalidAppointmentException;
import org.dabs.back.model.bind.AddAppointmentModel;
import org.dabs.back.model.view.AppointmentTableModel;
import org.dabs.back.model.view.AppointmentViewModel;
import org.dabs.back.model.view.DoctorSelectViewModel;
import org.dabs.back.model.view.PatientBasicViewModel;
import org.dabs.back.repository.AppointmentRepository;
import org.dabs.back.repository.PatientRepository;
import org.dabs.back.service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

	private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

	private AppointmentRepository appointmentRepository;

	private PatientRepository patientRepository;

	private ModelMapper modelMapper;

	@Autowired
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository, ModelMapper modelMapper,
			PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
		this.appointmentRepository = appointmentRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Appointment save(AddAppointmentModel appointment) {
		
		appointment.setId(null);
		Patient patient = patientRepository.getOne(appointment.getPatientId());
		
		checkIfAvailableAdd(appointment, patient);
		checkifOverBooked(appointment);
		checkIfWithInSchedule(appointment, patient);
		
		Appointment newApp = modelMapper.map(appointment, Appointment.class);
		newApp.setDoctor(patient.getDoctor());
		newApp.setPatient(patient);

		return this.appointmentRepository.save(newApp);
	}

	@Override
	public int update(AddAppointmentModel appointmentModel) {
		
		Patient patient = patientRepository.getOne(appointmentModel.getPatientId());
		checkIfAvailableUpdate(appointmentModel, patient);
		checkIfWithInSchedule(appointmentModel, patient);

		return this.appointmentRepository.updateAppointment(appointmentModel);
	}

	/**
	 * 
	 * @param appointmentModel
	 */
	private void checkIfAvailableAdd(AddAppointmentModel appointmentModel, Patient patient) {
		
		int count = appointmentRepository.checkIfAvailableToAdd(patient.getDoctor().getId(),
				appointmentModel.getDate(), appointmentModel.getDateFrom(), appointmentModel.getDateTo());
		if (count > 0) {
			throw new InvalidAppointmentException("Conflicted with other appointments");
		}
	}

	/**
	 * 
	 * @param appointmentModel
	 */
	private void checkIfAvailableUpdate(AddAppointmentModel appointmentModel, Patient patient) {
		int count = appointmentRepository.checkIfAvailableToUpdate(patient.getDoctor().getId(),
				appointmentModel.getId(), appointmentModel.getDate(), appointmentModel.getDateFrom(),
				appointmentModel.getDateTo());
		if (count > 0) {
			throw new InvalidAppointmentException("Conflict with other appointments");
		}
	}

	/**
	 * Check if OverBooked
	 * 
	 * @param appointment
	 */
	private void checkifOverBooked(AddAppointmentModel appointment) {

		// check if over booked
		int overBooked = appointmentRepository.countByPatientIdAndDate(appointment.getPatientId(),
				appointment.getDate());
		if (overBooked > 0) {
			throw new InvalidAppointmentException("Overbooking not allowed");
		}
	}

	/**
	 * Check If WithIn Doctors Schedule
	 * 
	 * @param appointment
	 */
	private void checkIfWithInSchedule(AddAppointmentModel appointment, Patient patient) {

		Set<DaySchedule> doctorsched = patient.getDoctor().getWeekSchedule().getDaySchedules();

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
		DayOfWeek dayofweek = DayOfWeek.valueOf(dateFormat.format(appointment.getDate()).toUpperCase());

		Time timeFrom = new Time(appointment.getDateFrom().getTime());
		Time timeTo = new Time(appointment.getDateTo().getTime());

		for (DaySchedule sched : doctorsched) {
			if (sched.getDayOfWeek() == dayofweek) {
				if (timeFrom.before(sched.getStartTime()) || timeTo.after(sched.getEndTime())) {
					logger.info("Invalid Appointment Date");
					throw new InvalidAppointmentException("Appointment time not with in doctors schedule");
				}
			}
		}
	}

	@Override
	public List<AppointmentTableModel> getAppointmentPatient(long patientId, Date from, Date to) {

		List<Appointment> page = appointmentRepository.findAppointmentForPatientBetween(patientId, from, to);

		List<AppointmentTableModel> viewModels = new ArrayList<>();
		AppointmentTableModel model;
		int row = 1;
		for (Appointment app : page) {
			model = new AppointmentTableModel();
			model.setRowNo(row++);
			model.setId(app.getId());
			model.setDate(app.getDate());
			model.setDateFrom(app.getDateFrom());
			model.setDateTo(app.getDateTo());
			model.setDescription(app.getDescription());
			model.setPatientName(app.getDoctor().getFirstName() + " " + app.getDoctor().getLastName());
			viewModels.add(model);
		}
		return viewModels;
	}

	@Override
	public List<AppointmentTableModel> getAppointmentDoctor(long patientId, Date from, Date to) {

		List<Appointment> page = appointmentRepository.findAppointmentForDoctorBetween(patientId, from, to);
		int row = 1;
		List<AppointmentTableModel> viewModels = new ArrayList<>();
		AppointmentTableModel model;
		for (Appointment app : page) {
			model = new AppointmentTableModel();
			model.setRowNo(row++);
			model.setId(app.getId());
			model.setDate(app.getDate());
			model.setDateFrom(app.getDateFrom());
			model.setDateTo(app.getDateTo());
			model.setDescription(app.getDescription());
			model.setPatientName(app.getPatient().getFirstName() + " " + app.getPatient().getLastName());
			viewModels.add(model);
		}
		return viewModels;
	}

	@Override
	public AddAppointmentModel getFormDataId(long id) {
		Optional<Appointment> appointment = this.appointmentRepository.findById(id);
		if (!appointment.isPresent()) {
			throw new AppointmentNotFoundException();
		}
		return modelMapper.map(appointment.get(), AddAppointmentModel.class);
	}

	@Override
	public AppointmentViewModel getById(long id) {
		Optional<Appointment> appointment = this.appointmentRepository.findById(id);
		if (!appointment.isPresent()) {
			logger.info("Appointment id: %s not found", id);
			throw new AppointmentNotFoundException();
		}

		return mapAppointmentViewModel(appointment.get());
	}

	private AppointmentViewModel mapAppointmentViewModel(Appointment appointment) {
		AppointmentViewModel appointmentViewModel = this.modelMapper.map(appointment, AppointmentViewModel.class);
		PatientBasicViewModel patientBasicViewModel = this.modelMapper.map(appointment.getPatient(),
				PatientBasicViewModel.class);
		appointmentViewModel.setPatientBasicViewModel(patientBasicViewModel);
		DoctorSelectViewModel doctorSelectViewModel = this.modelMapper.map(appointment.getDoctor(),
				DoctorSelectViewModel.class);
		appointmentViewModel.setDoctorSelectViewModel(doctorSelectViewModel);
		return appointmentViewModel;
	}

	@Override
	public void deleteAppointment(Long id) {
		if (appointmentRepository.existsById(id)) {
			appointmentRepository.deleteById(id);
		} else {
			logger.warn("Appointment id: %s not found", id);
			throw new AppointmentNotFoundException();
		}
	}
}
