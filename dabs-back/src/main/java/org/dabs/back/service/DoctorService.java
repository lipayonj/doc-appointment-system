package org.dabs.back.service;

import java.util.List;

import org.dabs.back.entity.Doctor;
import org.dabs.back.model.bind.DoctorRegistrationModel;
import org.dabs.back.model.bind.EditDoctorModel;
import org.dabs.back.model.view.DoctorSelectViewModel;
import org.dabs.back.model.view.DoctorViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
	
    void create(DoctorRegistrationModel registrationModel);

    void save(EditDoctorModel editDoctorModel);

    Doctor getById(long id);

    DoctorViewModel getViewModelById(long id);

    Doctor getByUserId(long userId);

    EditDoctorModel getEditModelByUserId(long userId);

    DoctorSelectViewModel getModelByUserId(long userId);

    List<DoctorSelectViewModel> getAllSelect();

    Page<DoctorViewModel> getAll(Pageable pageable);
}
