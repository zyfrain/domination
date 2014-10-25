package domination.fftoolbox.scraper;

import java.io.FileNotFoundException;

import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;

public final class FFToolboxScraper {

	private final String configFile;
	private final String workingDirectory;
	private final String cookie;
	
	/**
	 * Public constructor
	 * @param configFile the configuration file to use for web-scraping
	 * @param workingDirectory the working directory for input/output opertations
	 */
	public FFToolboxScraper(final String configFile, final String workingDirectory, final String cookie) {
		this.configFile = configFile;
		this.workingDirectory = workingDirectory;
		this.cookie = cookie;
	}
	
	public String getWebContents(final String pos, final String page, final String week) throws FileNotFoundException {
		final ScraperConfiguration config = new ScraperConfiguration(configFile);
		final Scraper scraper = new Scraper(config, workingDirectory);
		
		StringBuilder url = new StringBuilder("http://fftoolbox.scout.com/football/2014/weeklycheatsheets.cfm?player_pos=");
		url.append(pos);
		url.append("&league_ID=842127");
		scraper.addVariableToContext("url", url.toString());
		scraper.addVariableToContext("cookie", cookie);
//		scraper.addVariableToContext("pos", pos);
//		scraper.addVariableToContext("page", page);
//		scraper.addVariableToContext("week", week);
//		scraper.addVariableToContext("league", 842127);
		
		scraper.execute();

		Variable articles = (Variable) scraper.getContext().get("fileContents");
		
		return articles.toString();
	}
}
