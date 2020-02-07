package org.dabs.back.model.view;

import java.util.Date;


public class AppointmentViewModel {
	
    private long id;

    private Date date;
    
    private Date dateFrom;
    
    private Date dateTo;

    private String type;

    private String description;
    
    private PatientBasicViewModel patientBasicViewModel;
    
    private DoctorSelectViewModel doctorSelectViewModel ;
    

    public PatientBasicViewModel getPatientBasicViewModel() {
		return patientBasicViewModel;
	}

	public void setPatientBasicViewModel(PatientBasicViewModel patientBasicViewModel) {
		this.patientBasicViewModel = patientBasicViewModel;
	}

	public DoctorSelectViewModel getDoctorSelectViewModel() {
		return doctorSelectViewModel;
	}

	public void setDoctorSelectViewModel(DoctorSelectViewModel doctorSelectViewModel) {
		this.doctorSelectViewModel = doctorSelectViewModel;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
}
