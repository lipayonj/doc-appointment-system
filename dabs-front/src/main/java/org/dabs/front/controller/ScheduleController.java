package org.dabs.front.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.dabs.back.entity.Doctor;
import org.dabs.back.entity.Patient;
import org.dabs.back.entity.User;
import org.dabs.back.model.bind.EditWeekScheduleModel;
import org.dabs.back.service.DoctorService;
import org.dabs.back.service.PatientService;
import org.dabs.back.service.WeekScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    private WeekScheduleService weekScheduleService;

    private DoctorService doctorService;

    private PatientService patientService;

    @Autowired
    public ScheduleController(WeekScheduleService weekScheduleService, DoctorService doctorService,
                              PatientService patientService) {
        this.weekScheduleService = weekScheduleService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/edit")
    public String getEditSchedule(Principal principal, Model model, HttpServletRequest request) {
        long weekScheduleId = getWeekScheduleId((Authentication) principal, request);
        EditWeekScheduleModel editWeekScheduleModel = this.weekScheduleService.getById(weekScheduleId);

        model.addAttribute("editWeekScheduleModel", editWeekScheduleModel);

        return "schedule/edit";
    }

    @PostMapping("/edit")
    public String editSchedule(@Valid @ModelAttribute EditWeekScheduleModel editWeekScheduleModel,
                               BindingResult bindingResult, Principal principal, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "schedule/edit";
        }

        long weekScheduleId = getWeekScheduleId((Authentication) principal, request);

        editWeekScheduleModel.setId(weekScheduleId);
        EditWeekScheduleModel editWeekScheduleModelIds = this.weekScheduleService.getById(weekScheduleId);
        for (int i = 0; i < editWeekScheduleModel.getEditDayScheduleModels().size(); i++) {
            editWeekScheduleModel
                    .getEditDayScheduleModels()
                    .get(i).setId(editWeekScheduleModelIds
                    .getEditDayScheduleModels().get(i).getId());
        }

        this.weekScheduleService.save(editWeekScheduleModel);

        return "redirect:/schedule/edit";
    }

    private long getWeekScheduleId(Authentication principal, HttpServletRequest request) {
        long userId = ((User) principal.getPrincipal()).getId();
        if (request.isUserInRole("ROLE_DOCTOR")) {
            Doctor doctor = this.doctorService.getByUserId(userId);
            return doctor.getWeekSchedule().getId();
        } else if (request.isUserInRole("ROLE_PATIENT")) {
            Patient patient = this.patientService.getByUserId(userId);
            return patient.getDoctor().getWeekSchedule().getId();
        }

        return 0;
    }
}
