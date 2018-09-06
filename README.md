# What is SQL Coder ?
SQL Coder is a tools for generating java bean according to data model.
# How to package SQL Coder ?
Here assume you have installed JDK(>=1.8) and Maven(>=3.0).
* You should clone source code to local machine.
```git clone https://github.com/achilles-liu/sql-coder.git```
* You should invoke the command to package as follows
`maven clean package -Dmaven.skip.test=true`
then the executable file(.exe) will be found in the target directory.
# How to use SQL Coder?
## Feature Instruction
![Feature](https://github.com/achilles-liu/sql-coder/blob/master/src/main/resources/png/SQLCoder_Feature.jpg)
### User Input Area
You should input basic information to connect database.In general,you must input host,database's schema,user's name and password to log on the specified database.
### System Prompt Area
this are will show all system operation state.For example,it will be shown in which has connected to database successfully or not.
### Database Information
Since the database has been connected successfully,here will show table list of the specified database.If you want to review the ddl script of table,you can double click the corresponding table's name.
### DDL Information
Here will show the DDL detail which you have selected table.
### Java Bean Information
If you have selected the table and inputted the package name,the Java Bean Information will been shown in.
### Operation Area
* You can generate Java Bean to click `generate` button
* You can download the Java Bean Class to click `download Java Bean` button
* You can clear DDL Information and Java Bean Information to click `clean` button
## Usage
### Database Connection
![Connection](https://github.com/achilles-liu/sql-coder/blob/master/src/main/resources/png/SQLCoder_Conn.jpg)
### DDL Script
The DDL script will be generated,if you double-click the specified table's name as follows,
![DDL](https://github.com/achilles-liu/sql-coder/blob/master/src/main/resources/png/SQLCoder_DDL.jpg)
### Generate Java Bean
After input package's name,the Java Bean will be generated to click `generate` button,
![JAVABean](https://github.com/achilles-liu/sql-coder/blob/master/src/main/resources/png/SQLCoder_JavaBean.jpg)
### Download Java Bean Class
After you select the specified directory,the java class file will be downloaded to click `download Java Bean` button.
