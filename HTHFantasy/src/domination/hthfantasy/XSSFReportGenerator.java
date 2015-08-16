package domination.hthfantasy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import domination.common.Player;
import domination.common.PlayerPosition;
import domination.solver.CostComparator;
import domination.solver.ScoreComparator;
import domination.solver.Team;

/**
 * Generates a report in Microsoft Excel format
 */
public abstract class XSSFReportGenerator {

	protected final String filename;
	protected final Workbook report;
	protected final CellStyle boldStyle;

	/**
	 * Constructor
	 * @param filename the output filename
	 */
	protected XSSFReportGenerator(final String filename) {
		this.filename = filename;

		this.report = new XSSFWorkbook(); 
		this.boldStyle = report.createCellStyle();
		
		Font boldFont = report.createFont();
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		this.boldStyle.setFont(boldFont);
	}
	
	/**
	 * Generate the report
	 * @param request the request
	 */
	abstract void generateReport(final ReportRequest request);

	protected void generateEpsilonPage(final Collection<Team> teams) {
		generateTeamListPage("Epsilon", teams);
	}
	
	protected void generateTeamListPage(final String name, final Collection<Team> teams) {
		List<Team> epsilonTeams = new ArrayList<>(teams);
		 Sheet page = report.createSheet(name);
		 Row header = page.createRow(0);
		 header.createCell(0).setCellValue("Cost");
		 header.createCell(1).setCellValue("Score");

		 if (epsilonTeams.size() == 0) {
			 return;
		 }
		 
		 int i = 2;
		 for (Player player : epsilonTeams.get(0).getPlayers()) {
			 header.createCell(i).setCellValue("Player" + (i - 1) + "(" + player.getPosition().toString() + ")");
			 i += 1;
		 }

		 int index = 1;
		 for (Team team : epsilonTeams) {
			 Row values = page.createRow(index++);
			 values.createCell(0).setCellValue(team.getCost());
			 values.createCell(1).setCellValue(team.getScore());

			i = 2;
			for (Player player : team.getPlayers()) {
				values.createCell(i).setCellValue(
						String.format("%s (%.2f:$%.0f)", player.getName(),
								player.getScore(), player.getCost()));
				i += 1;
			}
		}
	}

	protected void generateDensityPage(Collection<Player> players) {
		Map<PlayerPosition, Collection<Player>> playerMap = Player.buildMap(players);		
		Sheet page = report.createSheet("Density");

		int index = 0;
		for (Entry<PlayerPosition, Collection<Player>> entry : playerMap.entrySet()) {
			List<Player> sortedList = new ArrayList<Player>(entry.getValue());
			Collections.sort(sortedList, ScoreComparator.playerComparator);
			index = generateColumn(page, index, entry.getKey().toString(), sortedList) + 1;
		}

		return;
	}

	protected int generateColumn(final Sheet page, final int startIndex, final String name, final List<Player> list) {
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

	void writePlayer(int index, int count, Sheet page, Player player,
			Collection<Player> players) {
		ArrayList<Player> list = new ArrayList<> (players);
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
