package org.dabs.back.model.bind;

import java.util.Date;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.dabs.back.validation.IsTimeFromAndToInterval;
import org.dabs.back.validation.UpdateValidation;
import org.springframework.format.annotation.DateTimeFormat;

@IsTimeFromAndToInterval
public class AddAppointmentModel {

	@NotNull(groups = { UpdateValidation.class })
	private Long id;

	@Size(max = 256, message = "Invalid description length")
	private String description;

	@NotNull
	private Long patientId;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message = "Date Should be Future Date")
	private Date date;

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	private Date dateFrom;

	@NotNull
	@DateTimeFormat(pattern = "HH:mm")
	private Date dateTo;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
