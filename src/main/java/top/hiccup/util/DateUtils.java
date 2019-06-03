package top.hiccup.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 线程安全的日期时间工具类
 *
 * @author wenhy
 * @date 2019/6/3
 */
public class DateUtils {

    private static ThreadLocal<DateFormat> threadLocal = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    /**
     * 每次都new一个SimpleDateFormat对象
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String pattern) throws ParseException {
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateStr);
    }

    /**
     * 使用ThreadLocal
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseWithThreadLocal(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    /**
     * 使用JDK8中的日期格式工具类DateTimeFormatter
     *
     * @param dateStr
     * @return
     */
    public static LocalDate parseWithJDK8(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDate.parse(dateStr, formatter);
    }
}
