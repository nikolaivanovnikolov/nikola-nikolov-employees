/*
 * File    : EmployeeActivity.java
 * Project : ProjectPairsColleges
 * Package : data
 * Created : Feb 29, 2020
 * Author  : Nikola Nikolov
 */
package data;

/**
 * Store employee activity <code>period</code> in project with <code>projectID</code>.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class EmployeeActivity
{

    private long employeeID;
    private long projectID;
    private Period period;

    /**
     * Constructor for EmployeeActivity.
     * 
     */
    public EmployeeActivity()
    {
        super();
    }

    /**
     * Constructor for EmployeeActivity.
     * 
     * @param employeeID
     * @param projеctID
     * @param period
     */
    public EmployeeActivity(long employeeID, long projectID, Period period)
    {
        super();
        this.employeeID = employeeID;
        this.projectID = projectID;
        this.period = period;
    }

    /**
     * Get the <code>employeeID</code>.
     * 
     * @return The employeeID.
     */
    public long getEmployeeID()
    {
        return employeeID;
    }

    /**
     * Set the <code>employeeID</code>.
     * 
     * @param employeeID
     *            New employeeID.
     */
    public void setEmployeeID(long employeeID)
    {
        this.employeeID = employeeID;
    }

    /**
     * Get the <code>projectID</code>.
     * 
     * @return The projectID.
     */
    public long getProjectID()
    {
        return projectID;
    }

    /**
     * Set the <code>projectID</code>.
     * 
     * @param projectID
     *            New projectID.
     */
    public void setProjectID(long projectID)
    {
        this.projectID = projectID;
    }

    /**
     * Get the <code>period</code>.
     * 
     * @return The period.
     */
    public Period getPeriod()
    {
        return period;
    }

    /**
     * Set the <code>period</code>.
     * 
     * @param period
     *            New period.
     */
    public void setPeriod(Period period)
    {
        this.period = period;
    }
}
