package fr.lirmm.agroportal.ontologymappingharvester.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Util {

    public static String getDateTime(){
        return  new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }


}
