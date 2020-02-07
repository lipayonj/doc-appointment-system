package org.dabs.front.controller;

import javax.validation.Valid;

import org.dabs.back.entity.User;
import org.dabs.back.model.bind.DoctorRegistrationModel;
import org.dabs.back.model.bind.EditDoctorModel;
import org.dabs.back.model.view.DoctorViewModel;
import org.dabs.back.service.DoctorService;
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
public class DoctorController {
	private DoctorService doctorService;

	@Autowired
	public DoctorController(DoctorService doctorService) {
		this.doctorService = doctorService;
	}

	@GetMapping("/doctors/{id}")
	public String getDoctor(@PathVariable long id, Model model) {
		DoctorViewModel doctorViewModel = this.doctorService.getViewModelById(id);

		model.addAttribute("doctorViewModel", doctorViewModel);

		return "doctor/doctor";
	}

	@GetMapping({ "/", "/doctors" })
	public String getDoctors(Model model, @PageableDefault(size = 8) Pageable pageable) {
		Page<DoctorViewModel> doctors = this.doctorService.getAll(pageable);
		model.addAttribute("doctors", doctors);

		return "doctor/doctors";
	}

	@GetMapping("/register-doctor")
	public String getDoctorRegister(@ModelAttribute DoctorRegistrationModel doctorRegistrationModel, Model model) {
		return "doctor/register";
	}

	@PostMapping("/register-doctor")
	public String registerDoctor(@Valid @ModelAttribute DoctorRegistrationModel doctorRegistrationModel,
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "doctor/register";
		}
		this.doctorService.create(doctorRegistrationModel);
		return "redirect:/";
	}

	@GetMapping("/doctor/edit")
	public String getEditDoctor(Model model, Authentication principal) {

		long userId = ((User) principal.getPrincipal()).getId();
		EditDoctorModel editDoctorModel = this.doctorService.getEditModelByUserId(userId);

		model.addAttribute("editDoctorModel", editDoctorModel);

		return "doctor/edit";
	}

	@PostMapping("/doctor/edit")
	public String editDoctor(@Valid @ModelAttribute EditDoctorModel editDoctorModel, BindingResult bindingResult,
			Authentication principal, Model model) {
		if (bindingResult.hasErrors()) {
			return "doctor/edit";
		}

		long userId = ((User) principal.getPrincipal()).getId();
		EditDoctorModel editDoctorModelId = this.doctorService.getEditModelByUserId(userId);
		editDoctorModel.setId(editDoctorModelId.getId());

		this.doctorService.save(editDoctorModel);

		return "redirect:/";
	}
}
