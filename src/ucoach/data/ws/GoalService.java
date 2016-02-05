package ucoach.data.ws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import ucoach.data.model.Goal;

import java.util.List;

@WebService(endpointInterface="ucoach.data.ws.GoalInterface",
  serviceName="GoalService")
public class GoalService implements GoalInterface {

  @Resource
  WebServiceContext context;

  @Override
  public Goal createGoal(Goal goal, int userId, int hmTypeId){
    return Goal.createGoal(goal, userId, hmTypeId);
  }

  @Override
  public List<Goal> getGoalsFromUserByHMType(int userId, int hmTypeId){
    return Goal.getGoalsFromUserByHMType(userId, hmTypeId);
  }

  @Override
  public void deleteGoal(int goalId){
    Goal goal = Goal.getGoalById(goalId);
    Goal.deleteGoal(goal);
  }
}
