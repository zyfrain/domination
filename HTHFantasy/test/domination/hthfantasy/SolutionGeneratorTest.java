package domination.hthfantasy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.fftoolbox.scraper.FFToolboxDriver;
import domination.solver.BaseTeam;
import domination.solver.RecursiveTeam;
import domination.solver.Team;

public class SolutionGeneratorTest {

	@Test
	public void weekOne() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10290/View";
		int week = 1;
		
		SolutionGenerator.generate(url, week, "");
	}
	
	@Test
	public void weekTwo() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10513/View";
		int week = 2;
		
		SolutionGenerator.generate(url, week, "");
	}
	
	@Test
	public void weekThree() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10563/View";
		int week = 3;
		SolutionGenerator.generate(url, week, "");
	}
	
	@Test
	public void weekFour() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10606/View";
		int week = 4;
		String cookie = "CFID=158407179; CFTOKEN=eacecc10981dd10f-6D3854BF-CF55-F905-C9CEF4C086FCF691; __qca=P0-934549985-1409791514022; RefId=0; BrandId=0; __gads=ID=5537ff298e792bc6:T=1409791518:S=ALNI_MaDzKwzi2ybgIrCUe_NbYJnaP9rLQ; iPM=0; tisession=3Nsg7nE8OcamyuSChoT1xg==; email=zyfrain@yahoo.com; com.silverpop.iMAWebCookie=38dd9ad0-2bc1-1594-0a8a-6cd415999ebd; _gat=1; _gat_newTracker=1; SessionBrandId=0; _ga=GA1.2.1006868885.1411821335; __utma=113414992.1006868885.1411821335.1411824368.1411831342.3; __utmb=113414992.4.10.1411831342; __utmc=113414992; __utmz=113414992.1411821339.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmli=tabnav";			
		SolutionGenerator.generate(url, week, cookie);
	}
	
	@Test
	public void weakSex() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10687/View";
		int week = 6;
		String cookie = "CFID=158407179; CFTOKEN=eacecc10981dd10f-6D3854BF-CF55-F905-C9CEF4C086FCF691; __qca=P0-934549985-1409791514022; RefId=0; BrandId=0; __gads=ID=5537ff298e792bc6:T=1409791518:S=ALNI_MaDzKwzi2ybgIrCUe_NbYJnaP9rLQ; SessionBrandId=0; com.silverpop.iMAWebCookie=38dd9ad0-2bc1-1594-0a8a-6cd415999ebd; _ga=GA1.2.1006868885.1411821335; _gat=1; _gat_newTracker=1; __utmt=1; __utma=113414992.1006868885.1411821335.1413075519.1413079319.6; __utmb=113414992.1.10.1413079319; __utmc=113414992; __utmz=113414992.1411821339.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";			
		SolutionGenerator.generate(url, week, cookie);
	}
	
	@Test
	public void weekSeven() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10748/View";
		int week = 7;
		String cookie = "CFID=158407179; CFTOKEN=eacecc10981dd10f-6D3854BF-CF55-F905-C9CEF4C086FCF691; __qca=P0-934549985-1409791514022; RefId=0; BrandId=0; __gads=ID=5537ff298e792bc6:T=1409791518:S=ALNI_MaDzKwzi2ybgIrCUe_NbYJnaP9rLQ; SessionBrandId=0; _gat_sitetracker1=1; iPM=0; tisession=3Nsg7nE8OcamyuSChoT1xg==; email=zyfrain@yahoo.com; _gat=1; _gat_newTracker=1; __utmt=1; _ga=GA1.2.1006868885.1411821335; __utma=113414992.1006868885.1411821335.1413336159.1413727462.9; __utmb=113414992.7.10.1413727462; __utmc=113414992; __utmz=113414992.1411821339.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); com.silverpop.iMAWebCookie=38dd9ad0-2bc1-1594-0a8a-6cd415999ebd; com.silverpop.iMA.page_visit=-603090041,587011995,1494942,47,; com.silverpop.iMA.session=49333485-6840-4a58-7bc0-6eb9cc558260; __utmli=startcontent";			
		SolutionGenerator.generate(url, week, cookie);
	}
		
	@Test
	public void weekEight() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_10788/View";
		int week = 8;
		String cookie = "CFID=158407179; CFTOKEN=eacecc10981dd10f-6D3854BF-CF55-F905-C9CEF4C086FCF691; __qca=P0-934549985-1409791514022; RefId=0; BrandId=0; __gads=ID=5537ff298e792bc6:T=1409791518:S=ALNI_MaDzKwzi2ybgIrCUe_NbYJnaP9rLQ; SessionBrandId=0; iPM=0; tisession=3Nsg7nE8OcamyuSChoT1xg==; email=zyfrain@yahoo.com; _gat=1; _gat_newTracker=1; __utmt=1; _ga=GA1.2.1006868885.1411821335; __utma=113414992.1006868885.1411821335.1414247226.1414249708.11; __utmb=113414992.4.10.1414249708; __utmc=113414992; __utmz=113414992.1411821339.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); com.silverpop.iMAWebCookie=38dd9ad0-2bc1-1594-0a8a-6cd415999ebd; com.silverpop.iMA.page_visit=-603090041,587011995,1494942,; com.silverpop.iMA.session=a59115e8-8265-d543-b1e1-0713246c2bac; __utmli=startcontent";			
		SolutionGenerator.generate(url, week, cookie);
		SolutionGenerator.generateWithOverrides(url, week, cookie,
				Arrays.<String>asList("Jerick McKinnon RB", "Doug Baldwin WR"));
	}
			
	@Test
	public void testReport() {
		List<Player> players = FFToolboxDriver.readPlayers(2, "");
		Map<PlayerPosition, Player> buildTeam = new HashMap<PlayerPosition, Player>();
		
		for (Player player : players) {
			if (!buildTeam.containsKey(player.getPosition())) {
				buildTeam.put(player.getPosition(), player);
			}
		}
		
		Team team = new BaseTeam();
		for (Entry<PlayerPosition, Player> entry : buildTeam.entrySet()) {
			team = new RecursiveTeam(entry.getValue(), team);
		}
		
		ReportRequest request = new ReportRequest.Builder()
										.setAlphaTeam(team)
										.setBetaTeam(team)
										.setPlayers(players)
										.setBetaCap(0)
										.setFilename("C:\\temp\\test.xlsx")
										.build();
		XSSFReportGenerator.report(request);
	}
}
