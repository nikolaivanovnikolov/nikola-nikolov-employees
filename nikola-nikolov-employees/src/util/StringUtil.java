/*
 * File    : StringUtil.java
 * Project : nikola-nikolov-employees
 * Package : util
 * Created : Mar 1, 2020
 * Author  : Nikola Nikolov
 */
package util;

/**
 * TODO: comment class StringUtil.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class StringUtil
{

    public static boolean isEmpty(String str, boolean trim)
    {
        return str == null || str.isEmpty() || (trim && str.trim().isEmpty());
    }
}
