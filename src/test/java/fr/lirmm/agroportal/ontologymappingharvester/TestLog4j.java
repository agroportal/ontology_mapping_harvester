package fr.lirmm.agroportal.ontologymappingharvester;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;


public class TestLog4j {


    //private static Logger logger = LogManager.getLogger(TestLog4j.class);

    public static void main(String args[]){

     Properties logProperties =  new Properties();

     String command = "";


     logProperties.setProperty("log4j.rootLogger","TRACE");
     logProperties.setProperty("log4j.logger.error","TRACE,error");
     logProperties.setProperty("log4j.logger.statistics","TRACE,statistics");
     logProperties.setProperty("log4j.logger.stdout","TRACE, file, console");

     logProperties.setProperty("log4j.appender.file", "org.apache.log4j.varia.NullAppender");
     logProperties.setProperty("log4j.appender.console", "org.apache.log4j.varia.NullAppender");
     logProperties.setProperty("log4j.appender.statistics", "org.apache.log4j.varia.NullAppender");

     logProperties.setProperty("log4j.appender.error.File", "/home/abrahao/data/repository/harvest_tool_error.log");
     logProperties.setProperty("log4j.appender.error", "org.apache.log4j.RollingFileAppender");
     logProperties.setProperty("log4j.appender.error.MaxFileSize", "10MB");
     logProperties.setProperty("log4j.appender.error.Threshold", "ERROR");
     logProperties.setProperty("log4j.appender.error.layout",  "org.apache.log4j.PatternLayout");
     logProperties.setProperty("log4j.appender.error.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");


     if(command.indexOf("p")>-1) {
      logProperties.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
      logProperties.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
      logProperties.setProperty("log4j.appender.console.ConversionPattern", "%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");
     }


     if(command.indexOf("l")>-1){
      logProperties.setProperty("log4j.appender.file.File", "/home/abrahao/data/repository/my_example2.log");
      logProperties.setProperty("log4j.appender.file", "org.apache.log4j.FileAppender");
      logProperties.setProperty("log4j.appender.file.layout",  "org.apache.log4j.PatternLayout");
      logProperties.setProperty("log4j.appender.file.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] (%F) - %m%n");
     }

     if(command.indexOf("s")>-1){
      logProperties.setProperty("log4j.appender.statistics.File", "/home/abrahao/data/repository/my_example2_sts.log");
      logProperties.setProperty("log4j.appender.statistics", "org.apache.log4j.FileAppender");
      logProperties.setProperty("log4j.appender.statistics.layout",  "org.apache.log4j.PatternLayout");
      logProperties.setProperty("log4j.appender.statistics.layout.ConversionPattern","%m%n");
     }


     PropertyConfigurator.configure(logProperties);

     Logger stdoutLogger = Logger.getLogger("stdout");
     Logger errorLogger = Logger.getLogger("error");
     Logger statisticsLogger = Logger.getLogger("statistics");


     errorLogger.error("This is an ERROR message.");

     stdoutLogger.fatal("This is a FATAL ELCIO message.");
     stdoutLogger.error("This is an ERROR message.");
     stdoutLogger.warn("This is a WARN message.");
     stdoutLogger.info("This is an INFO message.");
     stdoutLogger.debug("This is a DEBUG message.");
     stdoutLogger.trace("This is a TRACE message.");

     statisticsLogger.debug("STS This is a debug message");
     statisticsLogger.info("STS This is an info message");
     statisticsLogger.warn("STS This is a warn message");
     statisticsLogger.error("STS This is an error message");
     statisticsLogger.fatal("STS This is a fatal message");


    }
}
