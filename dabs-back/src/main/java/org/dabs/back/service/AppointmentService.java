package org.dabs.back.service;

import java.util.Date;
import java.util.List;

import org.dabs.back.model.bind.AddAppointmentModel;
import org.dabs.back.model.view.AppointmentTableModel;
import org.dabs.back.model.view.AppointmentViewModel;

public interface AppointmentService {
    void save(AddAppointmentModel addAppointmentModel);

    AppointmentViewModel getById(long id);

	AddAppointmentModel getFormDataId(long id);

	void update(AddAppointmentModel addAppointmentModel);

	void deleteAppointment(Long id);

	List<AppointmentTableModel> getAppointmentPatient(long patientId, Date from, Date to);
	
	List<AppointmentTableModel> getAppointmentDoctor(long doctorId, Date from, Date to);
}
