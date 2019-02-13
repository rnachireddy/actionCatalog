/**
 * 
 */
package com.vzw.cops.action.adapt;

import com.vzw.cops.api.common.Service;
import com.vzw.cops.api.common.ServiceRequest;
import com.vzw.cops.engine.exception.EngineException;
import com.vzw.cops.entity.Device;
import com.vzw.cops.exception.ActionValidationException;
import com.vzw.cops.exception.InternalResourceException;
import com.vzw.cops.exception.ServiceValidationException;
import com.vzw.cops.util.DataValidateUtil;
import com.vzwcorp.dagv.data.model.EquipmentOrderModel;

/**
 * @author vangsa2
 *
 */
public class GetEquipmentUpgradeSearchOutOfXml extends GetEntitiesOutOfXml {
	
	EquipmentOrderModel equipmentOrder = null;
	private String equipOrderNumber = ApplicationConstants.EMPTY_STRING;
	private String locationCode = ApplicationConstants.EMPTY_STRING;
	private String mtn = null;
	
	@Override
	protected void postProcess() throws EngineException, ServiceValidationException, InternalResourceException {
		
		try {
			
			Service service = getInput(Service.class);			
			ServiceRequest serviceRequest = service.getServiceBody().getServiceRequest();
			
			if (null != serviceRequest) {
				List<Element> elementList = serviceRequest.getInputs();
				for (Element el : elementList) {
					String tagName = el.getTagName();
					if(tagName.equals("mtn")) {
						mtn = el.getTextContent();
					}
				}
			}
		}
		DataValidateUtil.validateIsNumeric(mtn, "mtn");
		line = new Line(mtn);
		equipmentOrder = new EquipmentOrder(line, equipOrderNumber, locationCode);
		setOutput(equipmentOrder);
				
		} catch (ActionValidationException e) {
			throw new ServiceValidationException(e.getMessage());
		}
	}
}

}
