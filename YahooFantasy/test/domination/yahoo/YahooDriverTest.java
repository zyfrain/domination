package domination.yahoo;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import domination.common.Player;

/**
 * A test for the {@link YahooDriver} class
 */
public class YahooDriverTest {
	
	@Test
	public void testFootballRetrieval() throws IOException {
		try {
			String token = "A=3ggRmDf_lF383dc2nwnhtf2j2sJ1k_IUuxDCkg_PL7yaowwvWiPxs9MADjFufSK9u9DfRAJDRCgN6VxZtiVq9MgQAcUrS6ny5PMbCXuedyoZI.RsC5rTn4jo3YUeTMFks9GueGZJUCG6sU7Pfq2wK8HcA88.IciJvc6hcMQK578X2CGGa7SaOtM_8OZeKNxiBNkXcGm1phkAmEbO3owIGylO2HWqjFWk3ZNhzJRaWjmV0mEAX233w6qv6FdFHXc3riNre10ORpHQ4x_Lz2zU3QKN1QvDYpxH5qiUPWPjMtGxg9vOKVDPf_j0ilGkeTYUs4uOGnguVRbqoZQYiebzdKgf.DOKCQFt1mpowb10_Bt9eNWFEDzdu5CLDMzCquwR2TVaX.OcLal_ZMYjDE_X1RKn6VOxV76bQX4fE4xTPzvYDCM9.Y76oiSVMpyyyQMvcVI1u1cQtjp1bSHdhq6WwSwHYVTQFFsJtZF7gMWI10QeKcv9urfqJ9SpdxUTq4T.8WOrVRFBGyD5PFjOh5JaRwarjgaYAr4iCHT9JSfAcSUMDfZE337CXj9hftPqyZtLyqePxhU1tq24xIJvlAgcNHpxD61nqMVsBd5HRUImqLrXIVr73wEEmcE83Vm8w8cAaIlSAPAjLz6nh9FEzjY9iALMNJxkd8srVs3PxVz4DfGgtjZ1XUU8hnIwdfTeTo5Vn1sywvTSlTpMCX2PS2bZ2lPKE4OWbi8URVzgNPpdKjifZXYpfYJ.q02XVgn5edO2jjpZJrS.t0dQFvWruklH6nn_NvhqqdA-";
			String tokenSecret = "9239173d1d263c47dd132ac0fced826210dff9bf";

			YahooDriver driver = new YahooDriver();
			driver.authenticate(token, tokenSecret);
			Collection<Player> players = driver.retrieveFootballActuals(12);
			
			System.out.println(players.size());
			for (Player player : players) {
				System.out.println(Player.toCsv(player));
			}
		}
		catch (IOException ex) {
			System.out.println(ex.toString());
		}
	}
}
