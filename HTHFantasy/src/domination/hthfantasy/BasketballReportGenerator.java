package domination.hthfantasy;

import java.io.FileOutputStream;

public class BasketballReportGenerator extends XSSFReportGenerator {

	private BasketballReportGenerator(final String filename) {
		super(filename);
	}
	
	/**
	 * Generates an Excel report from the request
	 * @param request the report request
	 */
	public static void report(final ReportRequest request) {
		XSSFReportGenerator generator = new BasketballReportGenerator(request.getFilename());
		generator.generateReport(request);
	}
	
	/**
	 * Generate the report
	 * @param request the request
	 */
	@Override
	void generateReport(final ReportRequest request) {
		try {
			generateTeamListPage("Alpha", request.getAlphaTeam());
			generateDensityPage(request.getPlayers());
			 
			 FileOutputStream out = new FileOutputStream(filename); 
			 report.write(out);
			 out.close();
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
}
