/*
 * File    : LongestProjectCoworkers.java
 * Project : ProjectPairsColleges
 * Package : app
 * Created : Feb 29, 2020
 * Author  : Nikola Nikolov
 */
package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import data.*;
import util.*;

/**
 * Main class that finds employee pairs that worked together for a longest period of days in a common project.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class LongestProjectCoworkers
{
    private static DateTimeFormatter[] DATE_FORMATERS = new DateTimeFormatter[]{
                                                                                DateTimeFormatter.ofPattern("uuuu-MM-dd"),
                                                                                DateTimeFormatter.ofPattern("uuuu.MM.dd"),
                                                                                DateTimeFormatter.ofPattern("uuuu/MM/dd"),
                                                                                DateTimeFormatter.ofPattern("dd-MM-uuuu"),
                                                                                DateTimeFormatter.ofPattern("dd.MM.uuuu"),
                                                                                DateTimeFormatter.ofPattern("dd/MM/uuuu"),
                                                                                DateTimeFormatter.ofPattern("MM-dd-uuuu"),
                                                                                DateTimeFormatter.ofPattern("MM.dd.uuuu"),
                                                                                DateTimeFormatter.ofPattern("MM/dd/uuuu"),
                                                                                DateTimeFormatter.ofPattern("dd-MMM-uuuu"),
                                                                                DateTimeFormatter.ofPattern("dd.MMM.uuuu"),
                                                                                DateTimeFormatter.ofPattern("dd/MMM/uuuu"),};

    private static List<EmployeeActivity> activities = null;

    /**
     * TODO: comment method main.
     * 
     * @param args
     */
    public static void main(String[ ] args)
    {
        validateArgs(args);

        String inputFilePath = args[0];
        readData(inputFilePath);

        //Map of projectID to Map of employeeID to List of periods employee has worked in project.
        Map<Long, Map<Long, List<Period>>> projectMap = groupActivities();

        Map<Long, CommonActivity> maxCommonActivityList = findMaxProjectCoWorkDays(projectMap);

        maxCommonActivityList.values().forEach(ca -> {
            System.out.println(ca.getProjectID() + " - " + ca.getEmployeeID1() + " - " + ca.getEmployeeID2() + " - " + ca.getWorkedDays());
        });
    }
    
    private static Map<Long, CommonActivity> findMaxProjectCoWorkDays(Map<Long, Map<Long, List<Period>>> projectMap) {
        Map<Long, CommonActivity> maxActivityMap = new HashMap<Long, CommonActivity>();

        Set<Long> projectIDs = projectMap.keySet();
        projectIDs.forEach(projectID -> {
            Map<Long, List<Period>> employeeMap = projectMap.get(projectID);

            Long[] employeeIDs = employeeMap.keySet().toArray(new Long[0]);
            for (int i = 0; i < employeeIDs.length-1; i++)
            {
                Long employeeID = employeeIDs[i];
                List<Period> empPeriods = employeeMap.get(employeeID);
                for (int k = i+1; k < employeeIDs.length; k++)
                {
                    Long anotherEmployeeID = employeeIDs[k];
                    long commonWorkDays = 0;
                    List<Period> otherEmpPeriods = employeeMap.get(anotherEmployeeID);
                    for(Period period : empPeriods) {
                        for(Period otherPeriod : otherEmpPeriods) {
                            commonWorkDays += getOverlapDays(period, otherPeriod);
                        }
                    }

                    if (maxActivityMap.get(projectID) == null || maxActivityMap.get(projectID).getWorkedDays() < commonWorkDays) {
                        maxActivityMap.put(projectID, 
                            new CommonActivity(projectID, employeeID, anotherEmployeeID, commonWorkDays));
                    }
                }
            }
       });

        return maxActivityMap;
    }
    
    private static Map<Long, Map<Long, List<Period>>> groupActivities() {
        Map<Long, Map<Long, List<Period>>> projectsMap = new HashMap<Long, Map<Long, List<Period>>>();
        if(activities != null && !activities.isEmpty()) {
            activities.forEach(activity -> {
                Long prID = activity.getProjectID();
                Map<Long, List<Period>> employeeMap = null;
                List<Period> periods = null;

                if (projectsMap.get(prID) == null) {
                    periods = new ArrayList<Period>();
                    periods.add(activity.getPeriod());

                    employeeMap = new HashMap<Long, List<Period>>();
                    employeeMap.put(activity.getEmployeeID(), periods);

                    projectsMap.put(prID, employeeMap);
                } else {
                    employeeMap = projectsMap.get(prID) != null ? projectsMap.get(prID) : new HashMap<Long, List<Period>>();

                    Long empID = activity.getEmployeeID();
                    if(employeeMap.get(empID) == null) {
                        periods = new ArrayList<Period>();
                        periods.add(activity.getPeriod());
                        employeeMap.put(empID, periods);
                    } else {
                        employeeMap.get(empID).add(activity.getPeriod());
                    }

                    projectsMap.put(prID, employeeMap);
                }
            });
        }

        return projectsMap;
    }

    private static void readData(String inputFilePath) {
        try(Stream<String> str = Files.lines(Paths.get(inputFilePath)))
        {
            activities = new ArrayList<EmployeeActivity>();
            str.forEach(line -> {
                String[] lineData = line.split(", ");
                LocalDate dateFrom = null;
                LocalDate dateTo = null;

                String dateStr = "";
                try
                {
                    dateStr = !(lineData[2] == null || lineData[2].isBlank()) ? lineData[2] : ""; 
                    dateFrom = parseDate(dateStr, false);
                    
                    dateStr = !(lineData[2] == null || lineData[3].isBlank()) ? lineData[3] : ""; 
                    dateTo = parseDate(dateStr, true);
                } catch (Exception e)
                {
                    // TODO: Process exception here.
                    e.printStackTrace();
                }
                EmployeeActivity activity = new EmployeeActivity(Long.parseLong(lineData[0], 10), Long.parseLong(lineData[1], 10), 
                    new Period(dateFrom, dateTo));
                activities.add(activity);
            });           
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static Long getOverlapDays(Period period1, Period period2) {
        LocalDate start1 = period1.getDateFrom();
        LocalDate end1 = period1.getDateTo();
        
        LocalDate start2 = period2.getDateFrom();
        LocalDate end2 = period2.getDateTo();

        LocalDate maxStartDate =  DateUtil.maxDate(start1, start2);  
        LocalDate minEndDate = DateUtil.minDate(end1, end2);  

        Long daysBetween = ChronoUnit.DAYS.between(maxStartDate, minEndDate);

        return (daysBetween < 0 ? 0 : daysBetween);
    }
    
    private static LocalDate parseDate(String dateStr, boolean isNullable)
        throws DateTimeParseException
    {
        if (isNullable && "NULL".equals(dateStr))
        {
            return LocalDate.now();
        }

        for (DateTimeFormatter dtf : DATE_FORMATERS)
        {
            try
            {
                return LocalDate.parse(dateStr, dtf);
            } catch (DateTimeParseException e)
            {
                // ignore, until all available parsers fails
            }
        }

        throw new DateTimeParseException(
            "Failed to parse date: " + dateStr + " with all available parsers.",
            dateStr, 0);
    }

    private static void validateArgs(String args[])
    {
        if(args== null || args.length < 1)
        {
            System.out.println("Please specify argument: input_file_path");
            System.exit(2);
        } else if (args.length > 1) {
            System.out.println("More than 1 arguments specified:");
            for (int i = 1; i < args.length; i++)
            {
                System.out.println(args[i]);
            }
            System.out.println("Will be neglected");
        }

        String filePath = args[0];
        if(filePath == null || filePath.isEmpty())
        {
            System.out.println("Empty or null file path!");
            System.exit(4);
        }

        File file = new File(filePath);
        if (!file.exists())
        {
            System.out.println("File does not exist!");
            System.exit(5);
        }
        
        if (file.isDirectory())
        {
            System.out.println("File path is a dyrectory!");
            System.exit(6);
        }
    }
}
