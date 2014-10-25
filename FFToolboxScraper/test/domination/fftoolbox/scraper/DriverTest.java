package domination.fftoolbox.scraper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import domination.common.Player;

public class DriverTest {

	@Test
	public void testRun() throws IOException {
		FileWriter outputFile = null;
		try {
			int week = 8;
			String cookie = "CFID=158407179; CFTOKEN=eacecc10981dd10f-6D3854BF-CF55-F905-C9CEF4C086FCF691; __qca=P0-934549985-1409791514022; RefId=0; BrandId=0; __gads=ID=5537ff298e792bc6:T=1409791518:S=ALNI_MaDzKwzi2ybgIrCUe_NbYJnaP9rLQ; SessionBrandId=0; iPM=0; tisession=3Nsg7nE8OcamyuSChoT1xg==; email=zyfrain@yahoo.com; _gat=1; _gat_newTracker=1; __utmt=1; _ga=GA1.2.1006868885.1411821335; __utma=113414992.1006868885.1411821335.1414247226.1414249708.11; __utmb=113414992.4.10.1414249708; __utmc=113414992; __utmz=113414992.1411821339.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); com.silverpop.iMAWebCookie=38dd9ad0-2bc1-1594-0a8a-6cd415999ebd; com.silverpop.iMA.page_visit=-603090041,587011995,1494942,; com.silverpop.iMA.session=a59115e8-8265-d543-b1e1-0713246c2bac; __utmli=startcontent";			
			List<Player> players = FFToolboxDriver.readPlayers(week, cookie);
			
			outputFile = new FileWriter("C:\\Temp\\Domination\\FFToolbox_Week" + week + ".csv");
			for (Player player : players) {
				outputFile.write(Player.toCsv(player) + "\n");
			}
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		finally {
			if (outputFile != null) 
				outputFile.close();
		}
	}
}
