package org.dabs.front.controller;

import java.util.List;

import javax.validation.Valid;

import org.dabs.back.entity.Doctor;
import org.dabs.back.entity.User;
import org.dabs.back.model.bind.EditPatientModel;
import org.dabs.back.model.bind.PatientRegistrationModel;
import org.dabs.back.model.view.DoctorSelectViewModel;
import org.dabs.back.model.view.PatientViewModel;
import org.dabs.back.service.DoctorService;
import org.dabs.back.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PatientController {
    private PatientService patientService;

    private DoctorService doctorService;

    @Autowired
    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/patient/{id}")
    public String getPatient(@PathVariable long id, Model model) {
        PatientViewModel patientViewModel = this.patientService.getById(id);

        model.addAttribute("patientViewModel", patientViewModel);

        return "patient/patient";
    }

    @GetMapping("/doctor/patients")
    public String getDoctorPatients(Model model, @PageableDefault(size = 8) Pageable pageable, Authentication principal) {
        long userId = ((User) principal.getPrincipal()).getId();

        Page<PatientViewModel> patients = this.patientService.getPatientsByDoctorId(pageable, userId);
        model.addAttribute("patients", patients);

        return "patient/patients";
    }

    @GetMapping("/register-patient")
    public String getPatientRegister(@ModelAttribute PatientRegistrationModel patientRegistrationModel, Model model) {
        List<DoctorSelectViewModel> doctors = this.doctorService.getAllSelect();
        model.addAttribute("doctors", doctors);

        return "patient/register";
    }

    @PostMapping("/register-patient")
    public String registerPatient(@Valid @ModelAttribute PatientRegistrationModel patientRegistrationModel, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<DoctorSelectViewModel> doctors = this.doctorService.getAllSelect();
            model.addAttribute("doctors", doctors);

            return "patient/register";
        }

        this.patientService.create(patientRegistrationModel);

        return "redirect:/";
    }

    @GetMapping("/patient/edit")
    public String getEditPatient(Model model, Authentication principal) {
        long userId = ((User) principal.getPrincipal()).getId();

        EditPatientModel editPatientModel = this.patientService.getEditModelByUserId(userId);

        model.addAttribute("editPatientModel", editPatientModel);

        return "patient/edit";
    }

    @PostMapping("/patient/edit")
    public String editPatient(@Valid @ModelAttribute EditPatientModel editPatientModel, BindingResult bindingResult, Authentication principal) {
        if(bindingResult.hasErrors()){
            return "patient/edit";
        }

        long userId = ((User) principal.getPrincipal()).getId();
        EditPatientModel editPatientModelId = this.patientService.getEditModelByUserId(userId);
        editPatientModel.setId(editPatientModelId.getId());

        this.patientService.save(editPatientModel);

        return "redirect:/";
    }
}
