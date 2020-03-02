/*
 * File    : DateUtil.java
 * Project : nikola-nikolov-employees
 * Package : util
 * Created : Mar 1, 2020
 * Author  : Nikola Nikolov
 */
package util;

import java.time.LocalDate;

/**
 * TODO: comment class DateUtil.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class DateUtil
{
    public static LocalDate minDate(LocalDate d1, LocalDate d2)
    {
        if (d1.isBefore(d2))
        {
            return d1;
        }
        return d2;
    }


    public static LocalDate maxDate(LocalDate d1, LocalDate d2)
    {
        if (d1.isAfter(d2))
        {
            return d1;
        }
        return d2;
    }

}