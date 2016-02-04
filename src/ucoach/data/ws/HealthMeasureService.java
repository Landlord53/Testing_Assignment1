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
    return HMType.getAll();
  }

  @Override
  public HealthMeasure createHealthMeasure(HealthMeasure healthMeasure, int userId, int hmTypeId) {
    return HealthMeasure.createHealthMeasure(healthMeasure, userId, hmTypeId);
  }
}
