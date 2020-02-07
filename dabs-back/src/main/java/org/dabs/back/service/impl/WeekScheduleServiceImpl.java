package org.dabs.back.service.impl;

import java.sql.Time;
import java.time.DayOfWeek;

import org.dabs.back.entity.DaySchedule;
import org.dabs.back.entity.WeekSchedule;
import org.dabs.back.model.bind.DayScheduleModel;
import org.dabs.back.model.bind.EditDayScheduleModel;
import org.dabs.back.model.bind.EditWeekScheduleModel;
import org.dabs.back.repository.WeekScheduleRepository;
import org.dabs.back.service.DayScheduleService;
import org.dabs.back.service.WeekScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeekScheduleServiceImpl implements WeekScheduleService {
	private WeekScheduleRepository weekScheduleRepository;

	private ModelMapper modelMapper;

	private DayScheduleService dayScheduleService;

	@Autowired
	public WeekScheduleServiceImpl(WeekScheduleRepository weekScheduleRepository, ModelMapper modelMapper,
			DayScheduleService dayScheduleService) {
		this.weekScheduleRepository = weekScheduleRepository;
		this.modelMapper = modelMapper;
		this.dayScheduleService = dayScheduleService;
	}

	@Override
	public WeekSchedule createDefault() {
		WeekSchedule weekSchedule = this.weekScheduleRepository.saveAndFlush(new WeekSchedule());

		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			DayScheduleModel dayScheduleModel = new DayScheduleModel();
			if (dayOfWeek == DayOfWeek.SUNDAY) {
				dayScheduleModel.setDayOfWeek(dayOfWeek.toString());
				dayScheduleModel.setStartTime(Time.valueOf("00:00:00"));
				dayScheduleModel.setEndTime(Time.valueOf("00:00:00"));
				dayScheduleModel.setWeekSchedule(weekSchedule);
			} else {
				dayScheduleModel.setDayOfWeek(dayOfWeek.toString());
				dayScheduleModel.setStartTime(Time.valueOf("09:00:00"));
				dayScheduleModel.setEndTime(Time.valueOf("17:00:00"));
				dayScheduleModel.setWeekSchedule(weekSchedule);
			}

			this.dayScheduleService.create(dayScheduleModel);
		}

		return weekSchedule;
	}

	@Override
	public EditWeekScheduleModel getById(long id) {
		WeekSchedule weekSchedule = this.weekScheduleRepository.findById(id).orElse(new WeekSchedule());

		EditWeekScheduleModel editWeekScheduleModel = this.modelMapper.map(weekSchedule, EditWeekScheduleModel.class);
		for (DaySchedule daySchedule : weekSchedule.getDaySchedules()) {
			EditDayScheduleModel editDayScheduleModel = this.modelMapper.map(daySchedule, EditDayScheduleModel.class);
			editDayScheduleModel.setStartTimeStr(daySchedule.getStartTime().toString());
			editDayScheduleModel.setEndTimeStr(daySchedule.getEndTime().toString());
			editWeekScheduleModel.getEditDayScheduleModels().add(editDayScheduleModel);
		}

		return editWeekScheduleModel;
	}

	@Override
	public void save(EditWeekScheduleModel editWeekScheduleModel) {
		WeekSchedule weekSchedule = this.weekScheduleRepository.getOne(editWeekScheduleModel.getId());
		weekSchedule.setAppointmentDuration(editWeekScheduleModel.getAppointmentDuration());
		this.weekScheduleRepository.saveAndFlush(weekSchedule);

		for (EditDayScheduleModel editDayScheduleModel : editWeekScheduleModel.getEditDayScheduleModels()) {
			this.dayScheduleService.save(editDayScheduleModel);
		}
	}
}
