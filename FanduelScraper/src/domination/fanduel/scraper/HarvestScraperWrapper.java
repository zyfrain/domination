package domination.fanduel.scraper;

import java.io.FileNotFoundException;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

public final class HarvestScraperWrapper {

	private final String configFile;
	private final String workingDirectory;

	/**
	 * Public constructor
	 * @param configFile the configuration file to use for web-scraping
	 * @param workingDirectory the working directory for input/output opertations
	 */
	public HarvestScraperWrapper(final String configFile, final String workingDirectory) {
		this.configFile = configFile;
		this.workingDirectory = workingDirectory;
	}
	
	public String getWebContents(final String id, final String code) throws FileNotFoundException {
		final ScraperConfiguration config = new ScraperConfiguration(configFile);
		final Scraper scraper = new Scraper(config, workingDirectory);
		
		scraper.addVariableToContext("id", id);
		scraper.addVariableToContext("code", code);
		
		scraper.execute();

		Variable articles = (Variable) scraper.getContext().get("fileContents");
		
		return articles.toString();
	}
}
