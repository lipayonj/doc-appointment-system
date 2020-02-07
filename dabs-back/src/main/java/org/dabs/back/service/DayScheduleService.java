package org.dabs.back.service;

import org.dabs.back.model.bind.DayScheduleModel;
import org.dabs.back.model.bind.EditDayScheduleModel;

public interface DayScheduleService {
	
    void create(DayScheduleModel dayScheduleModel);

    void save(EditDayScheduleModel editDayScheduleModel);
}
