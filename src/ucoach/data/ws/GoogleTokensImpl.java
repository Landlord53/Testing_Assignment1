package ucoach.data.ws;

import ucoach.data.model.GoogleTokens;
import ucoach.data.model.User;

import javax.jws.WebService;

@WebService(endpointInterface="ucoach.data.ws.GoogleTokensInterface",
    serviceName="GoogleTokensService")
public class GoogleTokensImpl implements GoogleTokensInterface {

	@Override
	public GoogleTokens getTokens(int userId) {
		// Get user
		User user = User.getUserById(userId);
		
		// Return tokens
		return GoogleTokens.getTokensByUser(user);
	}

	@Override
	public GoogleTokens setTokens(int userId, String accessToken, String refreshToken) {
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