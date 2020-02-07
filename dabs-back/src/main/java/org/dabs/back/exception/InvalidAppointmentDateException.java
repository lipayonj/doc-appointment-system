package org.dabs.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid appointment date.")
public class InvalidAppointmentDateException extends RuntimeException {
	private static final long serialVersionUID = 2503274528794960736L;
}
