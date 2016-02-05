package ucoach.data.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import ucoach.data.model.HMType;
import ucoach.data.model.HealthMeasure;

import java.util.List;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface HealthMeasureInterface {

  @WebMethod(operationName="getHMTypes")
  @WebResult(name="HMType") 
  public List<HMType> getAllHMTypes();

  @WebMethod(operationName="createHealthMeasure")
  @WebResult(name="createdHealthMeasure")
  public HealthMeasure createHealthMeasure(@WebParam(name="healthMeasure") HealthMeasure healthMeasure, @WebParam(name="userId") int userId, @WebParam(name="hmTypeId") int hmTypeId);

  @WebMethod(operationName="getHealthMeasuresFromUserByHMType")
  @WebResult(name="healthMeasureFromUserByType")
  public List<HealthMeasure> getHealthMeasuresFromUserByHMType(@WebParam(name="userId") int userId, @WebParam(name="hmTypeId") int hmTypeId);

  @WebMethod(operationName="deleteHealthMeasure")
  public void deleteHealthMeasure(@WebParam(name="healthMeasureId") int healthMeasureId);
}
