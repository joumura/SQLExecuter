@echo off
rem    SQLExecuter
rem 
rem usage:
rem    ./SQLExecuter.sh select.sql update.sql ...
rem    echo SELECT CURRENT DATE FROM SYSIBM.SYSDUMMY1; | SQLExecuter.bat in:
rem 
rem  As preparation... crypt your DB password !!!
rem    java -jar SQLExecuter-x.x.x.jar pass:<your password>
rem 
rem  write it into SQLExecuter.propertites.

rem set the JDBC Driver into classpath
java -cp SQLExecuter-0.0.3.jar;db2jcc4-10.1.jar;db2jcc_license_cu-9.7.jar;db2jcc_license_cisuz-9.7.jar jp.co.tokyo_gas.cirius_fw.application.Main %*
