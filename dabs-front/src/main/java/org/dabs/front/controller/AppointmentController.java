package org.dabs.front.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dabs.back.entity.Doctor;
import org.dabs.back.entity.Patient;
import org.dabs.back.entity.User;
import org.dabs.back.exception.AppointmentNotFoundException;
import org.dabs.back.exception.InvalidAppointmentException;
import org.dabs.back.model.bind.AddAppointmentModel;
import org.dabs.back.model.view.AppointmentViewModel;
import org.dabs.back.model.view.DoctorSelectViewModel;
import org.dabs.back.model.view.PatientBasicViewModel;
import org.dabs.back.service.AppointmentService;
import org.dabs.back.service.DoctorService;
import org.dabs.back.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
	private AppointmentService appointmentService;

	private DoctorService doctorService;

	private PatientService patientService;

	@Autowired
	public AppointmentController(AppointmentService appointmentService, DoctorService doctorService,
			PatientService patientService) {
		this.appointmentService = appointmentService;
		this.doctorService = doctorService;
		this.patientService = patientService;
	}

	@GetMapping("/patient")
	public String getPatientAppointmentHomePage() {
		return "appointment/appointments";
	}

	@GetMapping("/doctor")
	public String getDoctorAppointmentHomePage() {
		return "appointment/appointments";
	}
	
	@GetMapping("/{appointmentId}")
	public String getAppointment(@PathVariable long appointmentId, Authentication principal, Model model, HttpServletRequest request) {
		long userId = ((User) (principal).getPrincipal()).getId();
		
		AppointmentViewModel appointmentViewModel = null;
		if (request.isUserInRole("ROLE_DOCTOR")) {
			Doctor doctor = this.doctorService.getByUserId(userId);
			appointmentViewModel = this.appointmentService.getById(appointmentId);

			// Is this appointment to this doctor
			if (appointmentViewModel.getDoctorSelectViewModel().getId() != doctor.getId()) {
				return "redirect:/appointment/doctor";
			}

		} else if (request.isUserInRole("ROLE_PATIENT")) {
			Patient patient = this.patientService.getByUserId(userId);
			appointmentViewModel = this.appointmentService.getById(appointmentId);

			// Is this appointment to this patient
			if (appointmentViewModel.getPatientBasicViewModel().getId() != patient.getId()) {
				return "redirect:/appointment/patient";
			}
		}
		
		model.addAttribute("appointmentViewModel", appointmentViewModel);
		return "appointment/appointment";
	}

	@ExceptionHandler(AppointmentNotFoundException.class)
	public String catchAppointmentNotFoundException() {
		return "error/appointment-not-found";
	}

	@ExceptionHandler(InvalidAppointmentException.class)
	public String catchInvalidAppointment() {
		return "error/invalid-appointment-date";
	}

	/**
	 * 
	 * @param principal
	 * @param addAppointmentModel
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/add")
	public String addNewAppointment(Principal principal, @ModelAttribute AddAppointmentModel addAppointmentModel,
			Model model, HttpServletRequest request) {

		long userId = ((User) ((Authentication) principal).getPrincipal()).getId();

		if (request.isUserInRole("ROLE_DOCTOR")) {
			DoctorSelectViewModel doctorSelectViewModel = this.doctorService.getModelByUserId(userId);
			model.addAttribute("doctorSelectViewModel", doctorSelectViewModel);

			List<PatientBasicViewModel> doctorPatients = this.patientService
					.getPatientsByDoctorId(doctorSelectViewModel.getId());
			model.addAttribute("doctorPatients", doctorPatients);

		} else if (request.isUserInRole("ROLE_PATIENT")) {
			Patient patient = this.patientService.getByUserId(userId);
			addAppointmentModel.setPatientId(patient.getId());
			DoctorSelectViewModel doctorSelectViewModel = this.doctorService
					.getModelByUserId(patient.getDoctor().getUser().getId());
			model.addAttribute("doctorSelectViewModel", doctorSelectViewModel);
		}

		return "appointment/addApp";
	}
	
	/**
	 * 
	 * @param principal
	 * @param addAppointmentModel
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/edit")
	public String editAppointment(Principal principal, @ModelAttribute AddAppointmentModel addAppointmentModel,
			Model model, HttpServletRequest request) {

		long userId = ((User) ((Authentication) principal).getPrincipal()).getId();

		if (request.isUserInRole("ROLE_DOCTOR")) {
			DoctorSelectViewModel doctorSelectViewModel = this.doctorService.getModelByUserId(userId);
			model.addAttribute("doctorSelectViewModel", doctorSelectViewModel);

			// Not sure if patient is editable
			List<PatientBasicViewModel> doctorPatients = this.patientService
					.getPatientsByDoctorId(doctorSelectViewModel.getId());
			model.addAttribute("doctorPatients", doctorPatients);

		} else if (request.isUserInRole("ROLE_PATIENT")) {
			Patient patient = this.patientService.getByUserId(userId);
			DoctorSelectViewModel doctorSelectViewModel = this.doctorService
					.getModelByUserId(patient.getDoctor().getUser().getId());
			model.addAttribute("doctorSelectViewModel", doctorSelectViewModel);
		}

		return "appointment/editApp";
	}
}
