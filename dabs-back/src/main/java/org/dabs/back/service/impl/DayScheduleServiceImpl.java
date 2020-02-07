package org.dabs.back.service.impl;

import java.sql.Time;

import org.dabs.back.entity.DaySchedule;
import org.dabs.back.model.bind.DayScheduleModel;
import org.dabs.back.model.bind.EditDayScheduleModel;
import org.dabs.back.repository.DayScheduleRepository;
import org.dabs.back.service.DayScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DayScheduleServiceImpl implements DayScheduleService {
    private DayScheduleRepository dayScheduleRepository;

    private ModelMapper modelMapper;

    @Autowired
    public DayScheduleServiceImpl(DayScheduleRepository dayScheduleRepository, ModelMapper modelMapper) {
        this.dayScheduleRepository = dayScheduleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(DayScheduleModel dayScheduleModel) {
        DaySchedule daySchedule = this.modelMapper.map(dayScheduleModel, DaySchedule.class);

        this.dayScheduleRepository.saveAndFlush(daySchedule);
    }

    @Override
    public void save(EditDayScheduleModel editDayScheduleModel) {
        DaySchedule daySchedule = this.dayScheduleRepository.getOne(editDayScheduleModel.getId());
        // Convert from string "hh:mm" to time(hh:mm:ss) // welcome to Java
        String startTimeFormat = editDayScheduleModel.getStartTimeStr() + (editDayScheduleModel.getStartTimeStr().length() == 5 ? ":00" : "");
        String endTimeFormat = editDayScheduleModel.getEndTimeStr() + (editDayScheduleModel.getEndTimeStr().length() == 5 ? ":00" : "");

        daySchedule.setStartTime(Time.valueOf(startTimeFormat));
        daySchedule.setEndTime(Time.valueOf(endTimeFormat));

        this.dayScheduleRepository.saveAndFlush(daySchedule);
    }
}
