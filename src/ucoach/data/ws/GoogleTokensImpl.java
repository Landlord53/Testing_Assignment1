package ucoach.data.ws;

import ucoach.data.model.GoogleTokens;
import ucoach.data.model.User;
import ucoach.data.ws.Authentication;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

@WebService(endpointInterface="ucoach.data.ws.GoogleTokensInterface",
    serviceName="GoogleTokensService")
public class GoogleTokensImpl implements GoogleTokensInterface {

	@Resource
    WebServiceContext context;

	@Override
	public GoogleTokens getTokens(int userId) {
		
		// Validate client
		boolean isValid = Authentication.validateRequest(context);
		if (!isValid) {
            return null;
		}

		// Get user
		User user = User.getUserById(userId);
		
		// Return tokens
		return GoogleTokens.getTokensByUser(user);
	}

	@Override
	public GoogleTokens setTokens(int userId, String accessToken, String refreshToken) {
		
		// Validate client
		boolean isValid = Authentication.validateRequest(context);
		if (!isValid) {
            return null;
		}

		// Get user
		User user = User.getUserById(userId);

		// Create new google tokens
		GoogleTokens tokens = new GoogleTokens();
		tokens.setUser(user);
		tokens.setAccessToken(accessToken);
		tokens.setRefreshToken(refreshToken);
		
		// Persist to database
		return GoogleTokens.newTokens(tokens);
	}
}