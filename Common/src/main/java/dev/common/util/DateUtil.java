package dev.common.util;

import org.springframework.stereotype.Component;
import java.sql.Date;

@Component
public class DateUtil {
    public Date formatDateToDD_MM_YYYY(java.util.Date date){
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }

    public Date getFormattedToday(){
        return formatDateToDD_MM_YYYY(new java.util.Date());
    }
}