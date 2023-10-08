package com.employeemanagement.demo.logs;

import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * Utility class for logging
 *
 * @author 2144388
 */
@Component
public class LoggerUtil {

	private String beginExecutePrimaryStartsWith = "####################  Begin Execute - ";

	private String endExecutePrimaryStartsWith = "####################  End Execute - ";

	private String primaryEndsWith = "  ####################";

	private String beginExecuteSecondaryStartsWith = "*****  Begin Execute - ";

	private String endExecuteSecondaryStartsWith = "*****  End Execute - ";

	private String secondaryEndsWith = "  *****";

	@Getter
	private String validationSuccess = "Result - Validation Succesful";

	/**
	 * Builds log message for begin execute of primary process
	 * 
	 * @param processName - name of primary process
	 * @return String with log message
	 */
	public String buildBeginExecutePrimary(String processName) {
		return beginExecutePrimaryStartsWith.concat(processName.concat(primaryEndsWith));
	}

	/**
	 * Builds log message for end execute of primary process
	 * 
	 * @param processName - name of primary process
	 * @return String with log message
	 */
	public String buildEndExecutePrimary(String processName) {
		return endExecutePrimaryStartsWith.concat(processName.concat(primaryEndsWith));
	}

	/**
	 * Builds log message for begin execute of secondary process
	 * 
	 * @param processName - name of secondary process
	 * @return String with log message
	 */
	public String buildBeginExecuteSecondary(String processName) {
		return beginExecuteSecondaryStartsWith.concat(processName.concat(secondaryEndsWith));
	}

	/**
	 * Builds log message for end execute of secondary process
	 * 
	 * @param processName - name of secondary process
	 * @return String with log message
	 */
	public String buildEndExecuteSecondary(String processName) {
		return endExecuteSecondaryStartsWith.concat(processName.concat(secondaryEndsWith));
	}

}
