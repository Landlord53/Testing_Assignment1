package ucoach.data.ws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import ucoach.data.model.HMType;
import ucoach.data.model.HealthMeasure;

import java.util.List;

@WebService(endpointInterface="ucoach.data.ws.HealthMeasureInterface",
  serviceName="HealthMeasureService")
public class HealthMeasureService implements HealthMeasureInterface {

  @Resource
  WebServiceContext context;

  @Override
  public List<HMType> getAllHMTypes() {
    // Validate client
    boolean isValid = Authorization.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
      return null;
    }
    
    return HMType.getAll();
  }

  @Override
  public HealthMeasure createHealthMeasure(HealthMeasure healthMeasure, int userId, int hmTypeId) {
    // Validate client
    boolean isValid = Authorization.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
      return null;
    }
    
    return HealthMeasure.createHealthMeasure(healthMeasure, userId, hmTypeId);
  }

  @Override
  public List<HealthMeasure> getHealthMeasuresFromUserByHMType(int userId, int hmTypeId){
    // Validate client
    boolean isValid = Authorization.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
      return null;
    }
    
    return HealthMeasure.getHealthMeasuresFromUserByHMType(userId, hmTypeId);
  }

  @Override
  public void deleteHealthMeasure(int healthMeasureId){
    // Validate client
    boolean isValid = Authorization.validateRequest(context);
    if (!isValid) {
      System.out.println("Request not valid. Check AuthenticationKey");
    } else{
      HealthMeasure.deleteHealthMeasure(healthMeasureId);
    }
  }
}
