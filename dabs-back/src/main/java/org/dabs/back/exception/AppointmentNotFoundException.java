package org.dabs.back.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such appointment.")
public class AppointmentNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 317419412821779310L;
}
