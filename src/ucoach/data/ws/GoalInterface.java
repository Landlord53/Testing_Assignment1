package ucoach.data.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import ucoach.data.model.Goal;

import java.util.List;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface GoalInterface {
  @WebMethod(operationName="createGoal")
  @WebResult(name="createdGoal")
  public Goal createGoal(@WebParam(name="goal") Goal goal, @WebParam(name="userId") int userId, @WebParam(name="hmTypeId") int hmTypeId);

  @WebMethod(operationName="getGoalsFromUserByHMType")
  @WebResult(name="GoalFromUserByType")
  public List<Goal> getGoalsFromUserByHMType(@WebParam(name="userId") int userId, @WebParam(name="hmTypeId") int hmTypeId);
}
