package org.dabs.back.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.dabs.back.entity.Doctor;
import org.dabs.back.entity.User;
import org.dabs.back.entity.WeekSchedule;
import org.dabs.back.model.bind.DoctorRegistrationModel;
import org.dabs.back.model.bind.EditDoctorModel;
import org.dabs.back.model.bind.UserRegistrationModel;
import org.dabs.back.model.view.DoctorSelectViewModel;
import org.dabs.back.model.view.DoctorViewModel;
import org.dabs.back.repository.DoctorRepository;
import org.dabs.back.service.DoctorService;
import org.dabs.back.service.UserService;
import org.dabs.back.service.WeekScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {
	private DoctorRepository doctorRepository;

	private ModelMapper modelMapper;

	private UserService userService;

	private WeekScheduleService weekScheduleService;

	@Autowired
	public DoctorServiceImpl(DoctorRepository doctorRepository, ModelMapper modelMapper, UserService userService,
			WeekScheduleService weekScheduleService) {
		this.doctorRepository = doctorRepository;
		this.modelMapper = modelMapper;
		this.userService = userService;
		this.weekScheduleService = weekScheduleService;
	}

	@Override
	public void create(DoctorRegistrationModel registrationModel) {
		User user = this.createDoctorUser(registrationModel);
		WeekSchedule weekSchedule = this.weekScheduleService.createDefault();

		Doctor doctor = this.modelMapper.map(registrationModel, Doctor.class);
		doctor.setUser(user);
		doctor.setWeekSchedule(weekSchedule);

		this.doctorRepository.saveAndFlush(doctor);
	}

	@Override
	public void save(EditDoctorModel editDoctorModel) {
		Doctor currentDoctor = this.doctorRepository.findOneByUserId(editDoctorModel.getId());
		Doctor doctor = this.modelMapper.map(editDoctorModel, Doctor.class);

		doctor.setWeekSchedule(currentDoctor.getWeekSchedule());
		doctor.setPatients(currentDoctor.getPatients());
		doctor.setUser(currentDoctor.getUser());

		this.doctorRepository.saveAndFlush(doctor);
	}

	@Override
	public Doctor getById(long id) {
		return this.doctorRepository.getOne(id);
	}

	@Override
	public DoctorViewModel getViewModelById(long id) {
		Doctor doctor = this.doctorRepository.getOne(id);

		return this.modelMapper.map(doctor, DoctorViewModel.class);
	}
	
	@Override
	public List<DoctorSelectViewModel> getAllSelect() {
		List<Doctor> doctors = this.doctorRepository.findAllByOrderByFirstNameAscLastName();
		List<DoctorSelectViewModel> doctorSelectViewModels = new ArrayList<>();
		for (Doctor doctor : doctors) {
			DoctorSelectViewModel doctorSelectViewModel = this.modelMapper.map(doctor, DoctorSelectViewModel.class);
			doctorSelectViewModels.add(doctorSelectViewModel);
		}

		return doctorSelectViewModels;
	}

	@Override
	public Doctor getByUserId(long userId) {
		return this.doctorRepository.findOneByUserId(userId);
	}

	@Override
	public EditDoctorModel getEditModelByUserId(long userId) {
		Doctor doctor = this.doctorRepository.findOneByUserId(userId);

		return this.modelMapper.map(doctor, EditDoctorModel.class);
	}

	@Override
	public DoctorSelectViewModel getModelByUserId(long userId) {
		Doctor doctor = this.doctorRepository.findOneByUserId(userId);

		return this.modelMapper.map(doctor, DoctorSelectViewModel.class);
	}

	@Override
	public Page<DoctorViewModel> getAll(Pageable pageable) {
		Page<Doctor> doctors = this.doctorRepository.findAll(pageable);
		List<DoctorViewModel> doctorViewModels = new ArrayList<>();
		for (Doctor doctor : doctors) {
			DoctorViewModel doctorViewModel = this.modelMapper.map(doctor, DoctorViewModel.class);
			doctorViewModels.add(doctorViewModel);
		}
		return (Page<DoctorViewModel>) new PageImpl<DoctorViewModel>(doctorViewModels, pageable, doctors.getTotalElements());
	}

	private User createDoctorUser(DoctorRegistrationModel registrationModel) {
		UserRegistrationModel userRegistrationModel = this.modelMapper.map(registrationModel,
				UserRegistrationModel.class);
		String DEFAULT_DOCTOR_ROLE = "ROLE_DOCTOR";
		userRegistrationModel.setAdditionalRole(DEFAULT_DOCTOR_ROLE);
		return this.userService.register(userRegistrationModel);
	}
}
