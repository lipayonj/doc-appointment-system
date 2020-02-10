package org.dabs.back.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.dabs.back.model.bind.AddAppointmentModel;

public class IsTimeFromAndToIntervalValidator implements ConstraintValidator<IsTimeFromAndToInterval, Object> {

	@Override
	public void initialize(IsTimeFromAndToInterval isPasswordsMatching) {}

	@Override
	public boolean isValid(Object model, ConstraintValidatorContext context) {
		boolean valid = model instanceof AddAppointmentModel;
		if (valid) {
			AddAppointmentModel appointmentModel = (AddAppointmentModel) model;
			valid = valid && appointmentModel.getDateFrom() != null && appointmentModel.getDateTo() != null
					&& (appointmentModel.getDateFrom().before(appointmentModel.getDateTo()));
		}
		if (!valid) {
			context.buildConstraintViolationWithTemplate("Time from should be before time to").addPropertyNode("dateFrom")
					.addConstraintViolation().disableDefaultConstraintViolation();
		}
		return valid;
	}
}
