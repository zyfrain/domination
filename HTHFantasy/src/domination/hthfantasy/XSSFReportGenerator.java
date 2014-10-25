package domination.hthfantasy;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import domination.common.DominationFilter;
import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.CostComparator;
import domination.solver.ScoreComparator;
import domination.solver.Team;

/**
 * Generates a report in Microsoft Excel format
 */
public class XSSFReportGenerator {

	private final String filename;
	private final Workbook report;
	private final CellStyle boldStyle;

	/**
	 * Constructor
	 * @param filename the output filename
	 */
	private XSSFReportGenerator(final String filename) {
		this.filename = filename;

		this.report = new XSSFWorkbook(); 
		this.boldStyle = report.createCellStyle();
		
		Font boldFont = report.createFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		this.boldStyle.setFont(boldFont);
	}
	
	/**
	 * Generates an Excel report from the request
	 * @param request the report request
	 */
	public static void report(final ReportRequest request) {
		XSSFReportGenerator generator = new XSSFReportGenerator(request.getFilename());
		generator.generateReport(request);
	}
	
	/**
	 * Generate the report
	 * @param request the request
	 */
	void generateReport(final ReportRequest request) {
		try {
			
			generateResultsPage(request);
			generateDominationPage(request.getPlayers());
			generateDensityPage(request.getPlayers());
			generateTeamPage("Alpha", request.getAlphaTeam(), request.getPlayers());
			generateTeamPage("Beta", request.getBetaTeam(), request.getPlayers());
			
			 
			 FileOutputStream out = new FileOutputStream(filename); 
			 report.write(out);
			 out.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	private void generateTeamPage(String name, Team team,
			List<Player> players) {
		 Sheet page = report.createSheet(name);

		 writeTeam(0, page, team);

		 Map<PlayerPosition, List<Player>> playerMap = buildMap(players);
		 int index = 3;
		 for (Player player : team.getPlayers()) {
			 writePlayer(index, 10, page, player, playerMap.get(player.getPosition()));
			 index += 11;
		 }
	}

	private void generateDominationPage(List<Player> players) {
		Map<PlayerPosition, List<Player>> map = buildMap(players);
		
		Sheet page = report.createSheet("Domination");
		
		int index = 0;
		index = generateDominationColumn(page, index, "Quarter Backs", map.get(PlayerPosition.QB));
		index = generateDominationColumn(page, index, "Running Backs", map.get(PlayerPosition.RB));
		index = generateDominationColumn(page, index, "Wide Receivers", map.get(PlayerPosition.WR));
		index = generateDominationColumn(page, index, "Tight Ends", map.get(PlayerPosition.TE));
		index = generateDominationColumn(page, index, "Kickers", map.get(PlayerPosition.K));
		index = generateDominationColumn(page, index, "Defenses", map.get(PlayerPosition.DEF));		
	}

	private int generateColumn(final Sheet page, final int startIndex, final String name, final List<Player> list) {
		int index = startIndex;
		Row headerRow = page.createRow(index++);
		headerRow.createCell(0).setCellValue("Cost");
		headerRow.createCell(1).setCellValue("Score");
		headerRow.createCell(2).setCellValue(name);
		headerRow.createCell(3).setCellValue("Point Density");

		
		for (Player player : list) {
			Row playerRow = page.createRow(index++);
			playerRow.createCell(0).setCellValue(player.getCost());
			playerRow.createCell(1).setCellValue(player.getScore());
			playerRow.createCell(2).setCellValue(player.getName());
			playerRow.createCell(3).setCellValue((player.getScore() / player.getCost()) * 10000);
		}
		
		return index;
	}
	private int generateDominationColumn(final Sheet page, final int startIndex, final String name,
			final List<Player> list) {		
		List<Player> filteredPlayers = new ArrayList<Player>(list);
		DominationFilter.filter(filteredPlayers);
		Collections.sort(filteredPlayers, ScoreComparator.playerComparator);

		int index = generateColumn(page, startIndex, name, filteredPlayers);
		
		return index + 1; // add a space
	}

	private void generateResultsPage(ReportRequest request) {
		 Sheet page = report.createSheet("Results");
		 Row alphaHeader = page.createRow(0);
		 alphaHeader.createCell(0).setCellValue("Alpha Team");
		 writeTeam(1, page, request.getAlphaTeam());
		 
		 Row betaHeader = page.createRow(4);
		 betaHeader.createCell(0).setCellValue("Beta Team");
		 betaHeader.createCell(1).setCellValue(request.getBetaCap());
		 writeTeam(5, page, request.getBetaTeam());
		 
		 Row positionalHeader = page.createRow(9);
		 positionalHeader.createCell(0).setCellValue("Cost");
		 positionalHeader.createCell(1).setCellValue("Score");
		 positionalHeader.createCell(2).setCellValue("Defense");
		 positionalHeader.createCell(4).setCellValue("Cost");
		 positionalHeader.createCell(5).setCellValue("Score");
		 positionalHeader.createCell(6).setCellValue("Kicker");
		 
		 Map<PlayerPosition, List<Player>> playerMap = buildMap(request.getPlayers());
		 List<Player> kickers = playerMap.get(PlayerPosition.K);
		 Collections.sort(kickers, ScoreComparator.playerComparator);
		 List<Player> defenses = playerMap.get(PlayerPosition.DEF);
		 Collections.sort(defenses, ScoreComparator.playerComparator);
		 final int count = Math.min(kickers.size(), defenses.size());
		 
		 for (int i = 0; i < count; i++) {
			 Row currentRow = page.createRow(10 + i);
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

	private void generateDensityPage(List<Player> players) {
		Map<PlayerPosition, List<Player>> playerMap = buildMap(players);		
		Sheet page = report.createSheet("Density");

		int index = 0;
		for (Entry<PlayerPosition, List<Player>> entry : playerMap.entrySet()) {
			List<Player> sortedList = new ArrayList<Player>(entry.getValue());
			Collections.sort(sortedList, ScoreComparator.playerComparator);
			index = generateColumn(page, index, entry.getKey().toString(), sortedList) + 1;
		}

		return;
	}

	static Map<PlayerPosition, List<Player>> buildMap(List<Player> players) {
		Map<PlayerPosition, List<Player>> playerMap = new HashMap<PlayerPosition, List<Player>>();
		for (Player player : players) {
			if (!playerMap.containsKey(player.getPosition())) {
				playerMap.put(player.getPosition(), new ArrayList<Player>());
			}
			playerMap.get(player.getPosition()).add(player);
		}
		return playerMap;
	}

	void writePlayer(int index, int count, Sheet page, Player player,
			List<Player> list) {
		Collections.sort(list, CostComparator.playerComparator);
		int indexOfPlayer = list.indexOf(player);

		int halfCount = count / 2;
		
		int startIndex = indexOfPlayer - halfCount;
		int endIndex = indexOfPlayer + halfCount;
		
		if (indexOfPlayer < count) {
			startIndex = 0;
			endIndex = count;
		}
		
		if (indexOfPlayer > list.size() - count) {
			startIndex = list.size() - count;
			endIndex = list.size();
		}
		
		int counter = index;
		for (int i = startIndex; i < endIndex; i++) {
			Player rowPlayer = list.get(i);
			Row row = page.createRow(counter++);
			row.createCell(0).setCellValue(rowPlayer.getCost());
			row.createCell(1).setCellValue(rowPlayer.getScore());
			Cell nameCell = row.createCell(2);
			nameCell.setCellValue(rowPlayer.getName());
			if (i == indexOfPlayer) {
				nameCell.setCellStyle(boldStyle);
			}
		}
	}

	void writeTeam(int index, Sheet page, Team team) {
		
		Row header = page.createRow(index);
		Row values = page.createRow(index + 1);
		header.createCell(0).setCellValue("Cost");
		values.createCell(0).setCellValue(team.getCost());
		
		header.createCell(1).setCellValue("Score");
		values.createCell(1).setCellValue(team.getScore());
		
		int i = 2;
		for (Player player : team.getPlayers()) {
			header.createCell(i).setCellValue("Player" + (i - 1));
			values.createCell(i).setCellValue(String.format("%s (%.2f:$%.0f)", player.getName(), player.getScore(), player.getCost()));
			i += 1;
		}
	}
	
}
