package domination.yahoo;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import domination.common.Player;

public class OAuthConnectionTest {
	
	private static String BOWLBY_LEAGUE_ID = "331.l.687124";

	@Test
	public void runTest() throws IOException {
		OAuthConnection oAuth = new OAuthConnection();
		oAuth.authenticate();
		// 5 - passing tds, 10 - rushing tds, 13 - receiving tds
//	    String url = "http://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games;game_keys=331/leagues";
//	    String url = String.format("http://fantasysports.yahooapis.com/fantasy/v2/league/%s/players;count=300", BOWLBY_LEAGUE_ID);
//	    String url = "http://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games;game_keys=331/leagues";
		String url = "http://fantasysports.yahooapis.com/fantasy/v2/game/nfl/players;start=75;count=25/stats;type=week;week=13";
//		String url = "http://fantasysports.yahooapis.com/fantasy/v2/player/331.p.24791/stats";
//		String url = "http://fantasysports.yahooapis.com/fantasy/v2/game/nfl/stat_categories";
		System.out.println(oAuth.retrieveQuery(url));
	}
	
	@Test
	public void runTestAuthenticated() {
		String token = "A=6JIU8kTDlC7v3lwZLyqNzne0zM1H7PZFkg1BCSZepNKgb5dvPtcbFCxYGY4z2xsNIBEJhSEJhYNFHoVNb.fgnVomUMCzjdWv6y5RoiwVBWhVyw32oupX9bmOP2krNZL.CBHa4Sx3nUqlSSfg6PKgeuxR8XM8pz.xcUZVk9ytyt.LQ0S4uJ0lmvLqo_CKAQpK3v8S0c9FtXPTNR6n_m0zBNcUGErdIokJPjQHN1Aulq_ZeuZL.9sU6.f5d7ujEittOMNR8faDxoiLKzLxW8IqpcukiH4R7d1Knfy9Zzn74DXHv0C8_63IRtfc8Bi.9R6d5RHrAyhAD4XDzXDaOvilbdhlXNjCSiNZ7p5ElzIBU8oVbsxOuO5i1pwN2b8XmALUDz71nsUwh.ZZY63QKiUjlZG4mRhHnt.mB2l8tZeFUUfHAU6jYN8JaD79c2GDFk6c2TlCUhVPrPtQwPVK6qO82tGZJFUEAkqDNtbE2pEsSRgTsqZH7f3wjrq2aOHDdTRGpTOfa_UBxe0etztgFBEFliQdAI3D7SC0NkyIzsTdfowwknQNEvCMHE6GZj8nWaTQOgLXixbEK6VJ3.rYzLDoASnCXUJKs8J6jqAr0cPdJMnLOoZLMjxs_H2tVbP3XwVhlGS7_hzuPcKG083k1sTcWQ7GD_hCTR5E7o4xDj2xdkraXxoKqTWrC6hU8AAGeigGw8Ps6HC_yTpS6EbyzVlEOEhNL8ZVCeDMknUGmSOR9cp9Az24yiUqPWUpWxyPswoy_SWC.zi.tvuDVIFgoFEpP6vPTOe2Zbk-";
		String tokenSecret = "7a21f83704d7aca16a3a3d28ccc8dff012f7af7c";
		
		OAuthConnection oAuth = new OAuthConnection();
		oAuth.authenticate(token, tokenSecret);
		
		String url = "http://fantasysports.yahooapis.com/fantasy/v2/game/nfl/players;start=0;count=25/stats;type=week;week=13";
		System.out.println(oAuth.retrieveQuery(url));
		
	}
	
	@Test
	public void getLotsaPlayers() throws IOException {
		OAuthConnection oAuth = new OAuthConnection();
		String token = "A=3ggRmDf_lF383dc2nwnhtf2j2sJ1k_IUuxDCkg_PL7yaowwvWiPxs9MADjFufSK9u9DfRAJDRCgN6VxZtiVq9MgQAcUrS6ny5PMbCXuedyoZI.RsC5rTn4jo3YUeTMFks9GueGZJUCG6sU7Pfq2wK8HcA88.IciJvc6hcMQK578X2CGGa7SaOtM_8OZeKNxiBNkXcGm1phkAmEbO3owIGylO2HWqjFWk3ZNhzJRaWjmV0mEAX233w6qv6FdFHXc3riNre10ORpHQ4x_Lz2zU3QKN1QvDYpxH5qiUPWPjMtGxg9vOKVDPf_j0ilGkeTYUs4uOGnguVRbqoZQYiebzdKgf.DOKCQFt1mpowb10_Bt9eNWFEDzdu5CLDMzCquwR2TVaX.OcLal_ZMYjDE_X1RKn6VOxV76bQX4fE4xTPzvYDCM9.Y76oiSVMpyyyQMvcVI1u1cQtjp1bSHdhq6WwSwHYVTQFFsJtZF7gMWI10QeKcv9urfqJ9SpdxUTq4T.8WOrVRFBGyD5PFjOh5JaRwarjgaYAr4iCHT9JSfAcSUMDfZE337CXj9hftPqyZtLyqePxhU1tq24xIJvlAgcNHpxD61nqMVsBd5HRUImqLrXIVr73wEEmcE83Vm8w8cAaIlSAPAjLz6nh9FEzjY9iALMNJxkd8srVs3PxVz4DfGgtjZ1XUU8hnIwdfTeTo5Vn1sywvTSlTpMCX2PS2bZ2lPKE4OWbi8URVzgNPpdKjifZXYpfYJ.q02XVgn5edO2jjpZJrS.t0dQFvWruklH6nn_NvhqqdA-";
		String tokenSecret = "9239173d1d263c47dd132ac0fced826210dff9bf";
		oAuth.authenticate(token, tokenSecret);
		String urlTemplate = "http://fantasysports.yahooapis.com/fantasy/v2/game/nfl/players;start=%d;count=25"
				+ ";position=QB;position=RB;position=WR;position=TE;position=K;position=DEF"
				+ "/stats;type=week;week=12";
		
		FileWriter outputFile = null;
		try {
			outputFile = new FileWriter("C:\\Temp\\Domination\\Yahoo\\testing.txt");
			for (int i = 5; i < 6; i++) {
				String url = String.format(urlTemplate, i * 25);
				
				String result = oAuth.retrieveQuery(url);

				outputFile.write(result);
			}
		}
		catch (Exception ex) {
			System.out.println("Exception caught writing output file: " + ex.toString());
		}
		finally {
			if (outputFile != null) 
				outputFile.close();
		}		
		
	}
	
	

}
