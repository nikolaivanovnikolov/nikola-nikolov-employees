/*
 * File    : LongestProjectCoworkers.java
 * Project : ProjectPairsColleges
 * Package : app
 * Created : Feb 29, 2020
 * Author  : Nikola Nikolov
 */
package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

import data.CommonActivity;
import data.EmployeeActivity;
import data.Period;
import ui.UIGridViewer;
import util.DateUtil;
import util.StringUtil;

/**
 * Main class that finds employee pairs that worked together for a longest
 * period of days in a common project.
 * 
 * @author <a href="mailto:n.nikolov@prolet.org">Nikola Nikolov</a>
 */
public class LongestProjectCoworkers
{
    private static String               DELIMITER      = ",";
    private static String               LINE_FORMAT    = "EmpID, ProjectID, DateFrom, DateTo";

    private static DateTimeFormatter[ ] DATE_FORMATERS = new DateTimeFormatter[ ]
        {
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
          DateTimeFormatter.ofPattern("dd/MMM/uuuu"), };

    /**
     * TODO: comment method main.
     * 
     * @param args
     */
    public static void main(String[ ] args)
    {
        List<CommonActivity> maxCommonActivities = new ArrayList<CommonActivity>();

        if(args != null && args.length > 0) {
            String inputFilePath = args[0];
            final List<EmployeeActivity> activities = loadDataFromFile(inputFilePath);
            maxCommonActivities = findMaxCommonActivities(activities);
        }

        UIGridViewer.createUIGrid(maxCommonActivities);
    }

    public static List<CommonActivity> findMaxCommonActivities(List<EmployeeActivity> activities)
    {

        // Map of projectID to Map of employeeID to List of periods employee has
        // worked in project.
        Map<Long, Map<Long, List<Period>>> projectMap = groupActivities(activities);

        Map<Long, CommonActivity> maxCommonActivityList = findMaxProjectCoWorkDays(projectMap);

        List<CommonActivity> maxCommonActivities = new ArrayList<CommonActivity>();
        maxCommonActivityList.values().forEach(ca -> {
            maxCommonActivities.add(ca);
            System.out.println(ca.getProjectID() + " - " + ca.getEmployeeID1() + " - " + ca.getEmployeeID2() + " - "
                    + ca.getWorkedDays());
        });

        return maxCommonActivities;
    }

    private static Map<Long, CommonActivity> findMaxProjectCoWorkDays(Map<Long, Map<Long, List<Period>>> projectMap)
    {
        Map<Long, CommonActivity> maxActivityMap = new HashMap<Long, CommonActivity>();

        Set<Long> projectIDs = projectMap.keySet();
        projectIDs.forEach(projectID -> {
            Map<Long, List<Period>> employeeMap = projectMap.get(projectID);

            Long[ ] employeeIDs = employeeMap.keySet().toArray(new Long[0]);
            for (int i = 0; i < employeeIDs.length - 1; i++)
            {
                Long employeeID = employeeIDs[i];
                List<Period> empPeriods = employeeMap.get(employeeID);
                for (int k = i + 1; k < employeeIDs.length; k++)
                {
                    Long anotherEmployeeID = employeeIDs[k];
                    long commonWorkDays = 0;
                    List<Period> otherEmpPeriods = employeeMap.get(anotherEmployeeID);
                    for (Period period : empPeriods)
                    {
                        for (Period otherPeriod : otherEmpPeriods)
                        {
                            commonWorkDays += getOverlapDays(period, otherPeriod);
                        }
                    }

                    if (maxActivityMap.get(projectID) == null
                            || maxActivityMap.get(projectID).getWorkedDays() < commonWorkDays)
                    {
                        maxActivityMap.put(projectID,
                            new CommonActivity(projectID, employeeID, anotherEmployeeID, commonWorkDays));
                    }
                }
            }
        });

        return maxActivityMap;
    }

    private static Map<Long, Map<Long, List<Period>>> groupActivities(List<EmployeeActivity> activities)
    {
        Map<Long, Map<Long, List<Period>>> projectsMap = new HashMap<Long, Map<Long, List<Period>>>();
        if (activities != null && !activities.isEmpty())
        {
            activities.forEach(activity -> {
                Long prID = activity.getProjectID();
                Map<Long, List<Period>> employeeMap = null;
                List<Period> periods = null;

                if (projectsMap.get(prID) == null)
                {
                    periods = new ArrayList<Period>();
                    periods.add(activity.getPeriod());

                    employeeMap = new HashMap<Long, List<Period>>();
                    employeeMap.put(activity.getEmployeeID(), periods);

                    projectsMap.put(prID, employeeMap);
                } else
                {
                    employeeMap = projectsMap.get(prID) != null
                            ? projectsMap.get(prID)
                            : new HashMap<Long, List<Period>>();

                    Long empID = activity.getEmployeeID();
                    if (employeeMap.get(empID) == null)
                    {
                        periods = new ArrayList<Period>();
                        periods.add(activity.getPeriod());
                        employeeMap.put(empID, periods);
                    } else
                    {
                        employeeMap.get(empID).add(activity.getPeriod());
                    }

                    projectsMap.put(prID, employeeMap);
                }
            });
        }

        return projectsMap;
    }

    public static List<EmployeeActivity> loadDataFromFile(String path)
    {
        if (StringUtil.isEmpty(path, true))
        {
            System.err.println("Could not load employees data. Path to CSV file is empty.");
            return Collections.emptyList();
        }

        final File file = new File(path);
        if (!file.exists() || !file.isFile())
        {
            System.err.println(
                "Provided path:" + path + " to employees intervals data point to non existing file or is not a file.");
            return Collections.emptyList();
        }
        
        return loadDataFromFile(file);
    }
    
    
    public static List<EmployeeActivity> loadDataFromFile(File file)
    {
        final List<EmployeeActivity> dataLines = new ArrayList<EmployeeActivity>();
        try (final Scanner scanner = new Scanner(file);)
        {
            while (scanner.hasNextLine())
            {
                dataLines.add(readEmployeeActivity(scanner.nextLine()));
            }
        } catch (FileNotFoundException e)
        {
            System.err.println("Provided path:" + file.getAbsolutePath() + " to employees intervals data point to non existing file.");
            return Collections.emptyList();
        }

        return dataLines;
    }

    private static EmployeeActivity readEmployeeActivity(String line)
    {
        try (Scanner lineScanner = new Scanner(line))
        {
            lineScanner.useDelimiter(DELIMITER);
            final List<String> lineElements = new ArrayList<String>();
            while (lineScanner.hasNext())
            {
                lineElements.add(lineScanner.next());
            }

            if (lineElements.size() != 4)
            {
                System.err.println("Not enough elements in line: " + line);
                System.err.println("Line should follow format: " + LINE_FORMAT);
                return null;
            }

            final EmployeeActivity dataLine = new EmployeeActivity();
            try
            {
                dataLine.setEmployeeID(Integer.parseInt(lineElements.get(0).trim()));
            } catch (NumberFormatException | NullPointerException e)
            {
                System.err.println("EmployeeID is not a number in line: " + line);
                return null;
            }

            try
            {
                dataLine.setProjectID(Integer.parseInt(lineElements.get(1).trim()));
            } catch (NumberFormatException | NullPointerException e)
            {
                System.err.println("ProjectID is not a number in line: " + line);
                return null;
            }

            LocalDate dateFrom;
            try
            {
                dateFrom = parseDate(lineElements.get(2).trim(), false);
            } catch (DateTimeParseException | NullPointerException e)
            {
                System.err.println("DateFrom is not in recognizable formats in line: " + line);
                return null;
            }

            LocalDate dateTo;
            try
            {
                dateTo = parseDate(lineElements.get(3).trim(), true);
            } catch (DateTimeParseException | NullPointerException e)
            {
                System.err.println("DateTo is not in recognizable formats in line: " + line);
                return null;
            }

            dataLine.setPeriod(new Period(dateFrom, dateTo));

            return dataLine;
        }
    }

    private static List<EmployeeActivity> readData(String inputFilePath)
    {
        final List<EmployeeActivity> activities = new ArrayList<EmployeeActivity>();
        try (Stream<String> str = Files.lines(Paths.get(inputFilePath)))
        {
            str.forEach(line -> {
                String[ ] lineData = line.split(", ");
                LocalDate dateFrom = null;
                LocalDate dateTo = null;

                String dateStr = "";
                try
                {
                    dateStr = !(lineData[2] == null || lineData[2].isBlank()) ? lineData[2] : "";
                    dateFrom = parseDate(dateStr, false);

                    dateStr = !(lineData[3] == null || lineData[3].isBlank()) ? lineData[3] : "";
                    dateTo = parseDate(dateStr, true);
                } catch (Exception e)
                {
                    // TODO: Process exception here.
                    e.printStackTrace();
                }
                EmployeeActivity activity = new EmployeeActivity(Long.parseLong(lineData[0], 10),
                    Long.parseLong(lineData[1], 10), new Period(dateFrom, dateTo));
                activities.add(activity);
            });
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return activities;
    }

    private static Long getOverlapDays(Period period1, Period period2)
    {
        LocalDate start1 = period1.getDateFrom();
        LocalDate end1 = period1.getDateTo();

        LocalDate start2 = period2.getDateFrom();
        LocalDate end2 = period2.getDateTo();

        LocalDate maxStartDate = DateUtil.maxDate(start1, start2);
        LocalDate minEndDate = DateUtil.minDate(end1, end2);

        Long daysBetween = ChronoUnit.DAYS.between(maxStartDate, minEndDate);

        return (daysBetween < 0 ? 0 : daysBetween);
    }

    private static LocalDate parseDate(String dateStr, boolean isNullable) throws DateTimeParseException
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

        throw new DateTimeParseException("Failed to parse date: " + dateStr + " with all available parsers.", dateStr,
            0);
    }
}
