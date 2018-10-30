/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autokeep.auto;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;


public class ExcaptionHandler{
	private String errorTimeStamp;
	private String customMeassage;
	private String errorMessage;
	private String stackTrace;
	
	public ExcaptionHandler(String customMeassage,Exception exception) {
		this.errorTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
		this.customMeassage = customMeassage;
		this.errorMessage = exception.getMessage();
		this.stackTrace = getExceptionStackTrace(exception);
		
		writeToErrorLog();
	}

	private String getExceptionStackTrace(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		String stackTrace = stringWriter.toString();
		return stackTrace;
	}

	public void writeToErrorLog() {
		DatabaseConnector DBconnector = DatabaseConnector.getDbConnectorInstance();
		String query = "insert into ErrorLog(ErrorTime,ErrorMessage,ErrorDetails,StackTrcae) values(?,?,?,?)";
		Queue<Object> parameters = new LinkedList<>();
		
		parameters.add(errorTimeStamp);
		parameters.add(customMeassage);
		parameters.add(errorMessage);
		parameters.add(stackTrace);
		
		try {
			DBconnector.executeSqlStatement(query,parameters);
		} catch (SQLException e) {
			String fileDirectory = System.getProperty("user.dir") + "\\Error_"+LocalDate.now()+".log";
			try {
				try(FileWriter fileWriter = new FileWriter(fileDirectory)){
					fileWriter.write(getExceptionStackTrace(e));
				}				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}