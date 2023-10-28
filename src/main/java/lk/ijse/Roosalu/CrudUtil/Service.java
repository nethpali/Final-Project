package lk.ijse.Roosalu.CrudUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Service {
    private static SimpleDateFormat dayFormat;
    private static SimpleDateFormat timeFormat;

    public static String setDate(){
        dayFormat = new SimpleDateFormat("MMMM dd, YYYY");
        String date= dayFormat.format(Calendar.getInstance().getTime());
        return date;
    }
    public static String setTime(){
        timeFormat = new SimpleDateFormat("hh:mm a");
        String time=timeFormat.format(Calendar.getInstance().getTime());
        return time;
    }


}
