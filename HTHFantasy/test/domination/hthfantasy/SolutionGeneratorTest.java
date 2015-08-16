package domination.hthfantasy;

import java.util.Arrays;
import java.util.Collections;
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
	public void weekThirteen() {
		int week = 13;
		final String thursUrl = "https://www.fanduel.com/e/Game/11053";
		final String sunUrl = "https://www.fanduel.com/e/Game/11084";

		final String outFilePre = "C:\\temp\\domination\\Week_" + week + "_";

		final String averageFile = "C:\\Temp\\Domination\\FantasyPros_Week13.csv";
		final String topFile = "C:\\Temp\\Domination\\TopExperts_Week13.csv";

		SolutionGenerator.generate2(thursUrl, week, averageFile, outFilePre + "thursAve");
		SolutionGenerator.generate2(thursUrl, week, topFile, outFilePre + "tursTop");
		SolutionGenerator.generate2(sunUrl, week, averageFile, outFilePre + "sunAve");
		SolutionGenerator.generate2(sunUrl, week, topFile, outFilePre + "sunTop");
	}

	@Test
	public void basketballThirteen() {
		final String baseDir = "C:\\temp\\domination\\basketball\\";

		List<String> overrides = Collections.<String>emptyList(); //Arrays.<String>asList("DeMarcus Cousins C");
		
//		SolutionGenerator.generateBB(baseDir + "NBA_Floor.csv", baseDir + "20141205_Floor", overrides);
		SolutionGenerator.generateBB(baseDir + "NBA_Average.csv", baseDir + "20141213_Average", overrides);
//		SolutionGenerator.generateBB(baseDir + "NBA_Ceiling.csv", baseDir + "20141205_Ceiling", overrides);		
	}
	
	@Test
	public void footballFanduel() {
		String url = "https://www.fanduel.com/e/Game/NFL_Salary_Cap_11429/View";
		String baseDir = "C:\\Temp\\Domination\\";

		SolutionGenerator.generate2(url, 20, baseDir + "NFL_Playoff_Wk3_Fantasy_Pros.csv", baseDir + "Week_20");
	}
	
	@Test
	public void football() {
		List<String> overrides = Collections.<String>emptyList();
		String baseDir = "C:\\Temp\\Domination\\";
		SolutionGenerator.generateFF(baseDir + "FantasyPros_Playoff_Week1.csv", baseDir + "Week_18", overrides);
	}
	
	@Test
	public void actuals() {
//		String token = "A=FL7bKgnaoCJqIStxL6mVBA4IUct_XrX7.3bi__fnoEWrdixQs2av0QChaLVHAcDtyEriUSdFUXctrvWDsOfSjgl4OjLR.e.urbNnoD49.WrgNq6H2Lx.67aW6N3NFzHcZicOH25CIr.MMd3sOx4LIQcNHTilxgSfsod13C5AMmAGkbdxUz5vkW9KlTXrvt8OzAU6HKWNIWO1TDp1ZvwBbUbN3O.nRdBlT4mYzdElNAnP_McDDqmt33rYvioYXHwGzGJjk4ztPqgC50imqOBVMios84DjnlMgRGK23_7ZgZEDwgqHp2jjlzwpiWzXHM4nTBq.6FZwPKHx0J.l_ORkY34p0el45.JWYO9KZgiEmEggFt1pIH2qAzVVxDNcGuNqPNzUGUi9TgSySTD7ryc1JW_RImnugMCkOVYp3IFXfjQKw.CHHN1Sm9b_rU_qMgqRo9qCrwYwUZdK5cUaZCcJ2mBrO68cFJtkpk3xiOODrvLUVVfgikyZ5FMK.VL3sh5jbQJD5JTLBac23g4yiq84iuJzwgbybc9rICkYKdzoyhDYI6NaRRZsw61dy_FPd.f7P0Cazgs_oG9KJiC0s4N7wYtoFT5jqh_gfDLIG1R4vyYER5CzESqOlXdVajiS9sWurRqK9l3Yt6RQ92ZP87K8pGc.kK54aziAG7mVqGExqVS.5Ff58kh783Elc_G3cZRR4T.mdlLdGYF5S9ppgkKZyrD3EdtNkGoBAgJ4lOvdNN54xFe7NSt8serXiVBTtPHqEfqDgrpMup0pc9lHJISA03qwvFwoLUg-";
//		String tokenSecret = "cce8e64dff9d390f33ba6faa3586f3a6a1ef89f4";
		final String baseDir = "C:\\temp\\domination\\";
		final int week = 13;
		SolutionGenerator.generateActuals(week, baseDir + "Fanduel_Week13.csv", baseDir + "Actuals_Week13");

//		final int week = 11;
//		SolutionGenerator.generateActuals(week, baseDir + "Fanduel_Week12.csv", baseDir + "Actuals_Week12", token, tokenSecret);

		//		for (int i = 8; i < 13; i++) {
//			SolutionGenerator.generateActuals(i, baseDir + "Fanduel_Week" + i + ".csv", baseDir + "Actuals_Week" + i, token, tokenSecret);
//		}
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
										.setAlphaTeam(Arrays.<Team>asList(team))
										.setBetaTeam(Arrays.<Team>asList(team))
										.setPlayers(players)
										.setBetaCap(0)
										.setFilename("C:\\temp\\test.xlsx")
										.build();
		FootballReportGenerator.report(request);
	}
	
	@Test
	public void testFileRead() {
		String filename = "C:\\Temp\\Domination\\FantasyPros_Week11.csv";
		
		List<Player> players = SolutionGenerator.getFilePlayers(filename);
		
		for (Player player : players) {
			System.out.println(player.getKey());
		}
	}
}
