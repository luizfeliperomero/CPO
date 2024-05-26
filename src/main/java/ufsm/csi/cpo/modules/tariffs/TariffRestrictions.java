package ufsm.csi.cpo.modules.tariffs;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ufsm.csi.cpo.modules.cdrs.ReservationRestrictionType;

import java.time.DayOfWeek;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TariffRestrictions {
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private Number minKwh;
    private Number maxKwh;
    private Number minCurrent;
    private Number maxCurrent;
    private Number minPower;
    private Number maxPower;
    private int minDuration;
    private int maxDuration;
    private List<DayOfWeek> dayOfWeek;
    private ReservationRestrictionType reservation;
}
