package org.dabs.back.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.dabs.back.entity.User;
import org.dabs.back.exception.AppointmentNotFoundException;
import org.dabs.back.exception.InvalidAppointmentException;
import org.dabs.back.model.bind.AddAppointmentModel;
import org.dabs.back.model.view.AppointmentTableModel;
import org.dabs.back.service.AppointmentService;
import org.dabs.back.validation.UpdateValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentRestController {

	private AppointmentService appointmentService;
	private SmartValidator validator;

	@Autowired
	public AppointmentRestController(AppointmentService appointmentService, SmartValidator validator) {
		this.appointmentService = appointmentService;
		this.validator = validator;
	}

	/**
	 * 
	 * @param principal
	 * @param request
	 * @param pageable
	 * @return
	 */
	@GetMapping("/")
	public ResponseEntity<List<AppointmentTableModel>> getAppointments(Authentication principal,
			HttpServletRequest request, @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

		long userId = ((User) (principal).getPrincipal()).getId();
		List<AppointmentTableModel> appointmentViewModels = null;

		if (request.isUserInRole("ROLE_PATIENT")) {
			appointmentViewModels = this.appointmentService.getAppointmentPatient(userId, from, to);
		} else if (request.isUserInRole("ROLE_DOCTOR")) {
			appointmentViewModels = this.appointmentService.getAppointmentDoctor(userId, from, to);
		}
		return ResponseEntity.ok(appointmentViewModels);
	}

	/**
	 * 
	 * @param addAppointmentModel
	 * @param bindingResult
	 * @param principal
	 * @param request
	 * @return
	 * @throws BindException
	 */
	@PostMapping("/save")
	public ResponseEntity<AddAppointmentModel> addAppointment(
			@Valid @ModelAttribute AddAppointmentModel addAppointmentModel, BindingResult bindingResult,
			Authentication principal, HttpServletRequest request) throws BindException {

		if (addAppointmentModel.getId() != null) {
			updateApp(addAppointmentModel, bindingResult, principal, request);
		} else {
			addNewApp(addAppointmentModel, bindingResult, principal, request);
		}

		return ResponseEntity.ok(addAppointmentModel);
	}

	@DeleteMapping(value = "/remove/{id}")
	public ResponseEntity<Long> deleteAppointment(@PathVariable Long id) {
		appointmentService.deleteAppointment(id);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	/**
	 * 
	 * @param addAppointmentModel
	 * @param bindingResult
	 * @param principal
	 * @param request
	 * @throws BindException
	 */
	private void addNewApp(AddAppointmentModel addAppointmentModel, BindingResult bindingResult,
			Authentication principal, HttpServletRequest request) throws BindException {

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		appointmentService.save(addAppointmentModel);
	}

	/**
	 * 
	 * @param addAppointmentModel
	 * @param bindingResult
	 * @param principal
	 * @param request
	 * @throws BindException
	 */
	private void updateApp(AddAppointmentModel addAppointmentModel, BindingResult bindingResult,
			Authentication principal, HttpServletRequest request) throws BindException {
		validator.validate(addAppointmentModel, bindingResult, UpdateValidation.class);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		appointmentService.update(addAppointmentModel);
	}

	/**
	 * 
	 * @param appointmentId
	 * @param principal
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/{appointmentId}")
	public ResponseEntity<AddAppointmentModel> getAppointment(@PathVariable long appointmentId,
			Authentication principal, Model model, HttpServletRequest request) {

		AddAppointmentModel appointmentModel = appointmentService.getFormDataId(appointmentId);
		return ResponseEntity.ok(appointmentModel);
	}

	/**
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleValidationExceptions(BindException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(AppointmentNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> catchAppointmentNotFoundException(AppointmentNotFoundException appnf) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", "Appointment Not Found");
		return errors;
	}

	/**
	 * 
	 * @return
	 */
	@ExceptionHandler(InvalidAppointmentException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> invalidAppointment(InvalidAppointmentException inv) {
		Map<String, String> errors = new HashMap<>();
		errors.put("message", inv.getMessage());
		return errors;
	}
}
