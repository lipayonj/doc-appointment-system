package org.dabs.back.service;

import org.dabs.back.entity.WeekSchedule;
import org.dabs.back.model.bind.EditWeekScheduleModel;

public interface WeekScheduleService {
	
    EditWeekScheduleModel getById(long id);

    WeekSchedule createDefault();

    void save(EditWeekScheduleModel editWeekScheduleModel);
}
