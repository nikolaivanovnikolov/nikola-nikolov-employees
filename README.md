# nikola-nikolov-employees
Find couple of empleyees that has longest worked together in a project.

java version "11.0.2" 2019-01-15 LTS
Java(TM) SE Runtime Environment 18.9 (build 11.0.2+9-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.2+9-LTS, mixed mode)

Run the program from the console with:

>java -jar nikola-nikolov-employees.jar eployees_project_pairs.txt

Output is in format:
ProjectID - EmployyeID1 - EmployeeID2 - CommonWorkDays

>10 - 145 - 220 - 152
>11 - 234 - 143 - 7
>12 - 145 - 143 - 650

If input data file is not specified:

>java -jar nikola-nikolov-employees.jar

no results is returned and grid in UI is empty.

Swing UI allows file to be chosed from file syste.
When different file is chosed, grid is updated.
