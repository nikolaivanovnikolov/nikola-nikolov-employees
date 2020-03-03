/*
 * File    : CommonActivity.java
 * Project : ProjectPairsColleges
 * Package : data
 * Created : Mar 1, 2020
 * Author  : Nikola Nikolov
 */
package data;

/**
 * Stores the days(<code>workedDays</code>) that employee with ID <code>employeeID1</code>
 * and employee with ID <code>employeeID2</code> worked together in 
 * project with ID <code>projectID</code>.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class CommonActivity
{

    private long projectID;
    private long employeeID1;
    private long employeeID2;
    private long workedDays;

    /**
     * Constructor for CommonActivity.
     * 
     */
    public CommonActivity()
    {
        super();
    }

    /**
     * Constructor for CommonActivity.
     * 
     * @param projectID
     * @param employeeID1
     * @param employeeID2
     * @param workedDays
     */
    public CommonActivity(
        long projectID,
        long employeeID1,
        long employeeID2,
        long workedDays)
    {
        super();
        this.projectID = projectID;
        this.employeeID1 = employeeID1;
        this.employeeID2 = employeeID2;
        this.workedDays = workedDays;
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
     * Get the <code>employeeID1</code>.
     * 
     * @return The employeeID1.
     */
    public long getEmployeeID1()
    {
        return employeeID1;
    }

    /**
     * Set the <code>employeeID1</code>.
     * 
     * @param employeeID1
     *            New employeeID1.
     */
    public void setEmployeeID1(long employeeID1)
    {
        this.employeeID1 = employeeID1;
    }

    /**
     * Get the <code>employeeID2</code>.
     * 
     * @return The employeeID2.
     */
    public long getEmployeeID2()
    {
        return employeeID2;
    }

    /**
     * Set the <code>employeeID2</code>.
     * 
     * @param employeeID2
     *            New employeeID2.
     */
    public void setEmployeeID2(long employeeID2)
    {
        this.employeeID2 = employeeID2;
    }

    /**
     * Get the <code>workedDays</code>.
     * 
     * @return The workedDays.
     */
    public long getWorkedDays()
    {
        return workedDays;
    }

    /**
     * Set the <code>workedDays</code>.
     * 
     * @param workedDays
     *            New workedDays.
     */
    public void setWorkedDays(long workedDays)
    {
        this.workedDays = workedDays;
    }
}
