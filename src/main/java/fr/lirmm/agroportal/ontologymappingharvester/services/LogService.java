package fr.lirmm.agroportal.ontologymappingharvester.services;

import fr.lirmm.agroportal.ontologymappingharvester.utils.ManageProperties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.util.Properties;

public class LogService {


    public Logger stdoutLogger;
    public Logger errorLogger;
    public Logger statisticsLogger;
    public Logger externalLogger;
    public Logger totalizationLogger;
    public Logger summaryLogger;
    public Logger phase1Logger;

    /**
     * Initialize LOGGERS and APPENDERS
     * @param command
     * @param currentOntologyName
     * @param repositoryPath
     */
    public void setupLogProperties(String command, String currentOntologyName, String repositoryPath){


        if(repositoryPath.lastIndexOf(File.separator)!=repositoryPath.length()-1){
            repositoryPath = repositoryPath+File.separator;
        }

        Properties logProperties =  new Properties();

        String path = ManageProperties.loadPropertyValue("externalproperties");

        logProperties.setProperty("log4j.rootLogger","TRACE");
        logProperties.setProperty("log4j.logger.error","TRACE,error");
        logProperties.setProperty("log4j.logger.statistics","TRACE,statistics");
        logProperties.setProperty("log4j.logger.stdout","TRACE, file, console");
        logProperties.setProperty("log4j.logger.external","TRACE, external_reference");
        logProperties.setProperty("log4j.logger.totals","TRACE, tots");
        logProperties.setProperty("log4j.logger.summary","TRACE, sum");
        logProperties.setProperty("log4j.logger.cleaning","TRACE, phase1");

        logProperties.setProperty("log4j.appender.file", "org.apache.log4j.varia.NullAppender");
        logProperties.setProperty("log4j.appender.console", "org.apache.log4j.varia.NullAppender");
        logProperties.setProperty("log4j.appender.statistics", "org.apache.log4j.varia.NullAppender");

        logProperties.setProperty("log4j.appender.error.File", path+"/OMHT_harvest_tool_error.log");
        logProperties.setProperty("log4j.appender.error", "org.apache.log4j.RollingFileAppender");
        logProperties.setProperty("log4j.appender.error.MaxFileSize", "10MB");
        logProperties.setProperty("log4j.appender.error.Threshold", "ERROR");
        logProperties.setProperty("log4j.appender.error.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.error.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t (%F) - %m%n");

        logProperties.setProperty("log4j.appender.external_reference.File", path+"/OMHT_external_references.log");
        logProperties.setProperty("log4j.appender.external_reference", "org.apache.log4j.FileAppender");
        logProperties.setProperty("log4j.appender.external_reference.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.external_reference.layout.ConversionPattern","%m%n");

        logProperties.setProperty("log4j.appender.tots.File", path+"/OMHT_matchs_totalization.xls");
        logProperties.setProperty("log4j.appender.tots", "org.apache.log4j.FileAppender");
        logProperties.setProperty("log4j.appender.tots.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.tots.layout.ConversionPattern","%m%n");

        logProperties.setProperty("log4j.appender.sum.File", path+"/OMHT_summary_matchs.xls");
        logProperties.setProperty("log4j.appender.sum", "org.apache.log4j.FileAppender");
        logProperties.setProperty("log4j.appender.sum.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.sum.layout.ConversionPattern","%m%n");

        logProperties.setProperty("log4j.appender.phase1.File", path+"/OMHT_external_matches_phase_1_to_be_curated.xls");
        logProperties.setProperty("log4j.appender.phase1", "org.apache.log4j.FileAppender");
        logProperties.setProperty("log4j.appender.phase1.layout",  "org.apache.log4j.PatternLayout");
        logProperties.setProperty("log4j.appender.phase1.layout.ConversionPattern","%m%n");


        if(command.indexOf("p")>-1) {
            logProperties.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
            logProperties.setProperty("log4j.appender.console.layout",  "org.apache.log4j.PatternLayout");
            logProperties.setProperty("log4j.appender.console.layout.ConversionPattern", "%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] %t - %m%n");
        }

//teste again
        if(command.indexOf("l")>-1){
            logProperties.setProperty("log4j.appender.file.File", ""+repositoryPath+currentOntologyName.toUpperCase()+".log");
            logProperties.setProperty("log4j.appender.file", "org.apache.log4j.FileAppender");
            logProperties.setProperty("log4j.appender.file.layout",  "org.apache.log4j.PatternLayout");
            logProperties.setProperty("log4j.appender.file.layout.ConversionPattern","%d{yyyy/MM/dd HH:mm:ss.SSS} [%5p] - %m%n");
        }

        if(command.indexOf("s")>-1){
            logProperties.setProperty("log4j.appender.statistics.File", ""+repositoryPath+currentOntologyName.toUpperCase()+".sts");
            logProperties.setProperty("log4j.appender.statistics", "org.apache.log4j.FileAppender");
            logProperties.setProperty("log4j.appender.statistics.layout",  "org.apache.log4j.PatternLayout");
            logProperties.setProperty("log4j.appender.statistics.layout.ConversionPattern","%m%n");
        }


        PropertyConfigurator.configure(logProperties);

        stdoutLogger = Logger.getLogger("stdout");
        errorLogger = Logger.getLogger("error");
        statisticsLogger = Logger.getLogger("statistics");
        externalLogger = Logger.getLogger("external");
        totalizationLogger = Logger.getLogger("totals");
        summaryLogger = Logger.getLogger("summary");
        phase1Logger = Logger.getLogger("cleaning");

    }


}
