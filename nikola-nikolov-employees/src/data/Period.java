/*
 * File    : Period.java
 * Project : ProjectPairsColleges
 * Package : data
 * Created : Mar 1, 2020
 * Author  : Nikola Nikolov
 */
package data;

import java.time.LocalDate;

/**
 * Class for period of time between <code>dateFrom</code> and <code>dateTo</code> dates.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class Period
{
    private LocalDate dateFrom;
    private LocalDate dateTo;

    /**
     * Constructor for Period.
     * 
     * @param dateFrom
     * @param dateTo
     */
    public Period(LocalDate dateFrom, LocalDate dateTo)
    {
        super();
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    /**
     * Get the <code>dateFrom</code>.
     * 
     * @return The dateFrom.
     */
    public LocalDate getDateFrom()
    {
        return dateFrom;
    }

    /**
     * Set the <code>dateFrom</code>.
     * 
     * @param dateFrom
     *            New dateFrom.
     */
    public void setDateFrom(LocalDate dateFrom)
    {
        this.dateFrom = dateFrom;
    }

    /**
     * Get the <code>dateTo</code>.
     * 
     * @return The dateTo.
     */
    public LocalDate getDateTo()
    {
        return dateTo;
    }

    /**
     * Set the <code>dateTo</code>.
     * 
     * @param dateTo
     *            New dateTo.
     */
    public void setDateTo(LocalDate dateTo)
    {
        this.dateTo = dateTo;
    }
}
