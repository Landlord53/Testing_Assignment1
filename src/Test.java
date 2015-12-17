import java.util.Date;

import ucoach.data.model.*;

public class Test {
  public static void main(String[] args) throws Exception{
    User user = new User();

    user.setFirstname("Ana");
    User createdUser = User.createUser(user);

    System.out.println(">>>>>>>>>>>>>>>>" + user.getFirstname());
  }
}
