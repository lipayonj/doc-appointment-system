package org.dabs.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Invalid appointment")
public class InvalidAppointmentException extends RuntimeException {

	private static final long serialVersionUID = 2503274528794960736L;
	
	public InvalidAppointmentException(String msg) {
		super(msg);
	}
}
