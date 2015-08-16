package domination.yahoo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.tuple.Pair;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.basic.HttpURLConnectionRequestAdapter;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpRequest;


/**
 * Manages the interaction through OAuth with the Yahoo! APIs
 */
public class OAuthConnection {

	private static String CONSUMER_KEY = "dj0yJmk9OWphYkU2MlM5Zng0JmQ9WVdrOWQzQklUMnRSTjJrbWNHbzlNVGd4TlRZME5ESTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD05Mw--";
	private static String CONSUMER_SECRET = "63e1e955519079b596abdd1c1e42b21259973b64";
	
	private static String YAHOO_REQUEST_API = "https://api.login.yahoo.com/oauth/v2/get_request_token";
	private static String YAHOO_AUTH_API = "https://api.login.yahoo.com/oauth/v2/request_auth";
	private static String YAHOO_ACCESS_API = "https://api.login.yahoo.com/oauth/v2/get_token";
	
	private OAuthConsumer consumer;
	
	public OAuthConnection() {
		this.consumer = new DefaultOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	}
	
	public Pair<String, String> authenticate() throws IOException {
		try {
		    OAuthProvider provider = new DefaultOAuthProvider(YAHOO_REQUEST_API,
		    		YAHOO_ACCESS_API,
		            YAHOO_AUTH_API) {
				private static final long serialVersionUID = 1L;
	
				@Override
				protected HttpRequest createRequest(String endpointUrl) throws MalformedURLException, IOException {
					HttpURLConnection connection = (HttpURLConnection)new URL(endpointUrl).openConnection();
					connection.setRequestMethod("GET");
					connection.setAllowUserInteraction(false);
					connection.setRequestProperty("Content-Length", "0");
					return new HttpURLConnectionRequestAdapter(connection);
				}
			};
	
		    // we do not support callbacks, thus pass OOB
		    String authUrl = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
		
		    System.out.println("Request token: " + consumer.getToken());
		    System.out.println("Token secret: " + consumer.getTokenSecret());
		
		    System.out.println("Now visit:\n" + authUrl
		            + "\n... and grant this app authorization");
		    System.out.println("Enter the verification code and hit ENTER when you're done");
		
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    String code = br.readLine();
		
		    provider.retrieveAccessToken(consumer, code);
		
		    System.out.println("Access token: " + consumer.getToken());
		    System.out.println("Token secret: " + consumer.getTokenSecret());
		}
		catch (OAuthExpectationFailedException 
				| OAuthCommunicationException 
				| OAuthNotAuthorizedException
				| OAuthMessageSignerException ex) {
			throw new IOException("Failed to authenticate with server", ex);
		}
		
		return Pair.<String, String>of(consumer.getToken(), consumer.getTokenSecret());
	}
	
	public void authenticate(final String token, final String tokenSecret) {
		consumer.setTokenWithSecret(token, tokenSecret);
	}
	
	public String retrieveQuery(final String urlString) {
        StringBuilder queryResults = new StringBuilder();
		try {
		
		    URL url = new URL(urlString);
		    HttpURLConnection request = (HttpURLConnection) url.openConnection();
	        request.setRequestMethod("GET");
	        request.setAllowUserInteraction(false);
	        request.setRequestProperty("Content-Length", "0");

		    consumer.sign(request);
		    request.connect();
	        
	        InputStreamReader reader = new InputStreamReader(request.getInputStream());
	        BufferedReader buff = new BufferedReader(reader);

	        String line = null;
	        while ((line = buff.readLine()) != null) {
	        	queryResults.append(line + "\n");
	        }
		}
		catch (Exception ex) {
			System.out.println(ex);
		}
		
		return queryResults.toString();
	}

}
