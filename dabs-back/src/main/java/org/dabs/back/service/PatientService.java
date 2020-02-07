package org.dabs.back.service;

import java.util.List;

import org.dabs.back.entity.Patient;
import org.dabs.back.model.bind.EditPatientModel;
import org.dabs.back.model.bind.PatientRegistrationModel;
import org.dabs.back.model.view.PatientBasicViewModel;
import org.dabs.back.model.view.PatientViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
	
	void create(PatientRegistrationModel registrationModel);

	void save(EditPatientModel editPatientModel);

	PatientViewModel getById(long id);

	Patient getByUserId(long userId);

	EditPatientModel getEditModelByUserId(long userId);

	List<PatientBasicViewModel> getPatientsByDoctorId(long doctorId);

	Patient getPatientById(long id);

	Page<PatientViewModel> getPatientsByDoctorId(Pageable pageable, long id);
}
