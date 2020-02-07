package org.dabs.back.model.view;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AppointmentTableModel {

	private int rowNo;
	
	private Long id;

	private String description;

	private Long patientId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;

	@DateTimeFormat(pattern = "HH:mm")
	private Date dateFrom;

	@DateTimeFormat(pattern = "HH:mm")
	private Date dateTo;
	
	private String patientName;
	
	private String doctorName;
	
	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

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
