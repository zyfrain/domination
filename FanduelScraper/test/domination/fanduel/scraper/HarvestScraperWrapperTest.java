package domination.fanduel.scraper;

import java.util.List;

import org.junit.Test;

public class HarvestScraperWrapperTest {

	@Test
	public void runWrapper() {
		try {
			HarvestScraperWrapper wrapper = new HarvestScraperWrapper("resources/test_wrapper_query.xml", "resources");
			String rawString = wrapper.getWebContents("36404783", "BUJBBYXGE");
		
			FanduelPlayerPicker players = FanduelPlayerPicker.parse(rawString);
			
//			System.out.println(strings.get(0));
		} 
		catch (final Exception ex) {
			System.out.println(ex);
		}
	}
	
}
