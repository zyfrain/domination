package domination.hthfantasy;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import domination.common.DominationFilter;
import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.ScoreComparator;

public class FootballReportGenerator extends XSSFReportGenerator {

	private FootballReportGenerator(final String filename) {
		super(filename);
	}
	
	/**
	 * Generates an Excel report from the request
	 * @param request the report request
	 */
	public static void report(final ReportRequest request) {
		XSSFReportGenerator generator = new FootballReportGenerator(request.getFilename());
		generator.generateReport(request);
	}
	
	/**
	 * Generate the report
	 * @param request the request
	 */
	@Override
	void generateReport(final ReportRequest request) {
		try {
			
			generateResultsPage(request);
			generateTeamListPage("Alpha", request.getAlphaTeam());
			generateTeamListPage("Beta", request.getBetaTeam());
			generateEpsilonPage(request.getEpsilonTeams());
			generateDominationPage(request.getPlayers());
			generateDensityPage(request.getPlayers());
			 
			 FileOutputStream out = new FileOutputStream(filename); 
			 report.write(out);
			 out.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	private void generateResultsPage(ReportRequest request) {
		 Sheet page = report.createSheet("Results");
		 Row alphaHeader = page.createRow(0);
		 alphaHeader.createCell(0).setCellValue("Alpha Team");
		 writeTeam(1, page, request.getAlphaTeam().iterator().next());
		 
		 Row betaHeader = page.createRow(7);
		 betaHeader.createCell(0).setCellValue("Beta Team");
		 betaHeader.createCell(1).setCellValue(request.getBetaCap());
		 if (request.getBetaTeam().size() > 0) {
			 writeTeam(8, page, request.getBetaTeam().iterator().next());
		 
			 Row positionalHeader = page.createRow(16);
			 positionalHeader.createCell(0).setCellValue("Cost");
			 positionalHeader.createCell(1).setCellValue("Score");
			 positionalHeader.createCell(2).setCellValue("Defense");
			 positionalHeader.createCell(4).setCellValue("Cost");
			 positionalHeader.createCell(5).setCellValue("Score");
			 positionalHeader.createCell(6).setCellValue("Kicker");
			 
			 Map<PlayerPosition, Collection<Player>> playerMap = Player.buildMap(request.getPlayers());
			 ArrayList<Player> kickers = new ArrayList<>(playerMap.get(PlayerPosition.K));
			 Collections.sort(kickers, ScoreComparator.playerComparator);
			 ArrayList<Player> defenses = new ArrayList<>(playerMap.get(PlayerPosition.DEF));
			 Collections.sort(defenses, ScoreComparator.playerComparator);
			 final int count = Math.min(kickers.size(), defenses.size());
			 
			 for (int i = 0; i < count; i++) {
				 Row currentRow = page.createRow(17 + i);
				 Player kicker = kickers.get(i);
				 Player defense = defenses.get(i);
				 
				 currentRow.createCell(0).setCellValue(defense.getCost());
				 currentRow.createCell(1).setCellValue(defense.getScore());
				 currentRow.createCell(2).setCellValue(defense.getName());
				 currentRow.createCell(4).setCellValue(kicker.getCost());
				 currentRow.createCell(5).setCellValue(kicker.getScore());
				 currentRow.createCell(6).setCellValue(kicker.getName());
			 }
		 }
	}

	private void generateDominationPage(Collection<Player> players) {
		Map<PlayerPosition, Collection<Player>> map = Player.buildMap(players);
		
		Sheet page = report.createSheet("Domination");
		
		int index = 0;
		index = generateDominationColumn(page, index, "Quarter Backs", map.get(PlayerPosition.QB));
		index = generateDominationColumn(page, index, "Running Backs", map.get(PlayerPosition.RB));
		index = generateDominationColumn(page, index, "Wide Receivers", map.get(PlayerPosition.WR));
		index = generateDominationColumn(page, index, "Tight Ends", map.get(PlayerPosition.TE));
		index = generateDominationColumn(page, index, "Kickers", map.get(PlayerPosition.K));
		index = generateDominationColumn(page, index, "Defenses", map.get(PlayerPosition.DEF));		
	}
	
	private int generateDominationColumn(final Sheet page, final int startIndex, final String name,
			final Collection<Player> list) {		
		List<Player> filteredPlayers = DominationFilter.filter(list);
		
		Collections.sort(filteredPlayers, ScoreComparator.playerComparator);

		int index = generateColumn(page, startIndex, name, filteredPlayers);
		
		return index + 1; // add a space
	}
}
