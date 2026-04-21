package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utility class used to generate Log4j logger.
 * 
 * We can generate logs in a text or a html file.
 * 
 * @author EL HAJAM Ayoub - HERON Sajid - BOUSSALEM Nassim
 */
public class LoggerUtility {
	private static final String TEXT_LOG_CONFIG = "src/log/log4j-text.properties";
	private static final String HTML_LOG_CONFIG = "src/log/log4j-html.properties";

	/**
	 * Configure l'outil de traçage et de reporting HTML/Texte selon un lexique de type de fichier.
	 * 
	 * @param logClass la classe métier nécessitant l'écho de log
	 * @param logFileType format souhaité (html ou text)
	 * @return logger prêt
	 */
	public static Logger getLogger(Class<?> logClass, String logFileType) {
		if (logFileType.equals("text")) {
			PropertyConfigurator.configure(TEXT_LOG_CONFIG);
		} else if (logFileType.equals("html")) {
			PropertyConfigurator.configure(HTML_LOG_CONFIG);
		} else {
			throw new IllegalArgumentException("Unknown log file type !");
		}

		String className = logClass.getName();
		return Logger.getLogger(className);
	}
}
