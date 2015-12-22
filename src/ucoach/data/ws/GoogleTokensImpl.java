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

    System.out.println("Getting tokens for user " + userId);

    // Validate client
    boolean isValid = Authentication.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
      return null;
    }

    // Get user
    User user = User.getUserById(userId);

    // Return tokens
    return GoogleTokens.getTokensByUser(user);
  }

  @Override
  public GoogleTokens setTokens(int userId, String accessToken, String refreshToken) {

    System.out.println("Setting tokens for user " + userId);

    // Validate client
    boolean isValid = Authentication.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
      return null;
    }

    // Get user
    User user = User.getUserById(userId);
    if (user == null) {
      return null;
    }

    // Create new google tokens
    GoogleTokens tokens = new GoogleTokens();
    tokens.setUser(user);
    tokens.setAccessToken(accessToken);
    tokens.setRefreshToken(refreshToken);

    // Persist to database
    return GoogleTokens.newTokens(tokens);
  }
}