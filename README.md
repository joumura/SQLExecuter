# SQLExecuter

## usage:
```
   ./SQLExecuter.sh select.sql update.sql ...
   
   echo SELECT CURRENT DATE FROM SYSIBM.SYSDUMMY1; | SQLExecuter.bat in:
```

## preparation...
1. crypt your DB password !!!
```
    java -jar SQLExecuter-x.x.x.jar pass:<your password>
```
  write it into SQLExecuter.propertites.

2. set the JDBC Driver into classpath of SQLExecuter.bat(.sh) and SQLExecuter.propertites.
