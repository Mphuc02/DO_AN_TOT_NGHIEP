package dev.common.util;

import org.springframework.stereotype.Component;
import java.sql.Date;

@Component
public class DateUtil {
    public Date formatDateToDD_MM_YYYY(java.util.Date date){
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return new Date(date.getTime());
    }

    public Date getFormattedToday(){
        return formatDateToDD_MM_YYYY(new java.util.Date());
    }
}