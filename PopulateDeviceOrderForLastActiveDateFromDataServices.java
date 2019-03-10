package com.vzw.cops.action.dao.dataservices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vzw.cops.action.dao.base.DataServicesDao;
import com.vzw.cops.api.common.ServiceError;
import com.vzw.cops.api.common.ServiceHeader;
import com.vzw.cops.constants.ApplicationConstants;
import com.vzw.cops.constants.DataServicesConstants.Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDateKeys;
import com.vzw.cops.constants.DataServicesConstants.Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDateMap;
import com.vzw.cops.constants.DataServicesConstants.MapName;
import com.vzw.cops.constants.ErrorConstants;
import com.vzw.cops.engine.exception.EngineException;
import com.vzw.cops.entity.Account;
import com.vzw.cops.entity.CommonHeaderError;
import com.vzw.cops.entity.CommonHeaderMessage;
import com.vzw.cops.entity.Customer;
import com.vzw.cops.entity.Device;
import com.vzw.cops.entity.Line;
import com.vzw.cops.entity.list.CustomerList;
import com.vzw.cops.exception.ActionValidationException;
import com.vzw.cops.exception.ApplicationException;
import com.vzw.cops.exception.DataNotFoundException;
import com.vzw.cops.exception.InternalResourceException;
import com.vzw.cops.exception.SysDataNotFoundException;
import com.vzw.cops.util.DataGridExpression;
import com.vzw.cops.util.DateUtil;
import com.vzw.cops.util.DateUtil.Format;
import com.vzw.cops.util.RoutingUtil;
import com.vzw.cops.util.RoutingUtil.BillingSystem;
import com.vzwcorp.dagv.data.model.CustDvcEqpTransModel;
import com.vzwcorp.dagv.data.model.CustomerDvcProvInfoModel;
import com.vzwcorp.dagv.data.service.Response;

public class PopulateDeviceOrderForLastActiveDateFromDataServices extends DataServicesDao {
	private CustomerList customerList;
	private Customer customer;
	private Line line;
	private Device device;
	private String deviceId;
	private String deviceIdType;
	private ServiceHeader serviceHeader;
	private CommonHeaderMessage commonHeaderMessage;
	private CommonHeaderError commonHeaderError;
	
	public enum BillingSystem {
		VISION_EAST,
		VISION_WEST,
		VISION_NORTH,
		VISION_B2B
	}

	@Override
	protected void prevalidate() throws ApplicationException, EngineException {
		device = getInput(Device.class,"Device");
		deviceId = device.getId();
		deviceIdType = device.getIdType().getCode();
		serviceHeader = getInput(ServiceHeader.class);
	}

	@Override
	protected String getTypedefId() {
		return MapName.Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDate.toString();
	}

	@Override
	protected List<DataGridExpression> getTypedefExpressionList() throws ActionValidationException {
		List<DataGridExpression> paramList = new ArrayList<DataGridExpression>();
		paramList.add(new DataGridExpression(Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDateKeys.deviceId, deviceId));
		paramList.add(new DataGridExpression(Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDateKeys.deviceIdTyp,deviceIdType));
		return paramList;
	}

	@SuppressWarnings("unused")
	@Override
	protected void populateEntities(Response resp) throws DataNotFoundException, ActionValidationException, SysDataNotFoundException, InternalResourceException, EngineException  {
		Map<String, Object> resultMap = resp.getResults();
		if (null == resultMap) {
			return;
		}
		
		customerList = new CustomerList();
		Map<String, Customer> customerMap = new HashMap<String, Customer>();
		
		checkCustomerMap(customerMap);
		String activeAtSwitchIndicator = ApplicationConstants.APP_NO;
		String lastDvcEqpActivationDate = ApplicationConstants.EMPTY_STRING;
		String custId =ZERO_STRING;
		Integer accountNum = 0;
		String mdn = ApplicationConstants.EMPTY_STRING;
		String billSysId = ApplicationConstants.EMPTY_STRING;
		String actvReqDate = ApplicationConstants.EMPTY_STRING;
		String disconnReqDate = ApplicationConstants.EMPTY_STRING;
		String currentTimeStamp = ApplicationConstants.EMPTY_STRING;
		List<CustDvcEqpTransModel> custDvcElementsList = convertToListValue(resultMap.get(Test_COR_checkActiveAndPendingDeviceRetrieveLastActiveDateMap.custDvc.toString()), new TypeReference<ArrayList<CustDvcEqpTransModel>>(){});
		if(custDvcElementsList == null || custDvcElementsList.size() == 0) {
			throw new DataNotFoundException(ErrorConstants.DEVICE_CODE, "DEVICE NOT FOUND");
		}
		List<Integer> billSysIdList = new ArrayList<Integer>();
		List<Date> dvcEqpTransTsList = new ArrayList<Date>();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
		for(CustDvcEqpTransModel custDvcList :  custDvcElementsList){
			billSysIdList.add(custDvcList.getBillSysId());
			Date dvcEqpTransDate = null;
			try {
				dvcEqpTransDate = simpleDateFormat.parse(custDvcList.getDvcEqpTransTs());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			dvcEqpTransTsList.add(dvcEqpTransDate);
		}
		Integer billSysIdMaxValue =Collections.max(billSysIdList);
		Date dvcEqpTransTsValue = Collections.max(dvcEqpTransTsList);
		final CustomerDvcProvInfoModel custDvcProv = convertToSingleValue(resultMap.get("customerDvcProv"),CustomerDvcProvInfoModel.class);
		for(int i=0;i<custDvcElementsList.size();i++){
			Date dvcEqpTransDate = null;
			try {
				dvcEqpTransDate = simpleDateFormat.parse(custDvcElementsList.get(i).getDvcEqpTransTs());
			}catch (ParseException e) {
				e.printStackTrace();
			}
			if(billSysIdMaxValue == custDvcElementsList.get(i).getBillSysId() && dvcEqpTransTsValue.equals(dvcEqpTransDate)){
				if(("IME").equals(deviceIdType)){
					if(custDvcElementsList!=null){
						if (dvcEqpTransDate != null) {
							custId = custDvcElementsList.get(i).getCustIdNo().toString();
							accountNum = custDvcElementsList.get(i).getAcctNo();
							mdn = custDvcElementsList.get(i).getMtn();
							billSysId = custDvcElementsList.get(i).getBillSysId().toString();
							deviceId = custDvcElementsList.get(i).getDeviceId();
							deviceIdType = custDvcElementsList.get(i).getDeviceIdTyp();
							lastDvcEqpActivationDate= custDvcElementsList.get(i).getDvcEqpTransTs().substring(5,7)+"/"+custDvcElementsList.get(i).getDvcEqpTransTs().substring(8,10)+"/"+custDvcElementsList.get(i).getDvcEqpTransTs().substring(0,4);
						}
					}
						else{
						throw new DataNotFoundException(ErrorConstants.DEVICE_CODE, "ERROR while retrieving device- Customer Device");
					}
				}else if(!("IME").equals(deviceIdType)){
					if(custDvcProv!=null){
						custId = custDvcProv.getCustIdNo().toString();
						accountNum = custDvcProv.getAcctNo();
						mdn = custDvcProv.getMtn();
						billSysId = custDvcProv.getBillSysId().toString();
						deviceId = custDvcProv.getDeviceId();
						deviceIdType = custDvcProv.getDeviceIdTyp();
						if(StringUtils.isNotBlank(custDvcProv.getActvReqDt())){
							actvReqDate = custDvcProv.getActvReqDt();
						}
						if(StringUtils.isNotBlank(custDvcProv.getDisconnReqDt())){
							disconnReqDate = custDvcProv.getDisconnReqDt();
						}
						if(("12/31/9999").equals(custDvcProv.getDisconnReqDt())){
							activeAtSwitchIndicator = ApplicationConstants.APP_YES;
							currentTimeStamp = DateUtil.getCurrent(billSysId, Format.DG_TIME);
						}
						if(custDvcElementsList.get(i).getDvcEqpTransTs() != null){
							lastDvcEqpActivationDate= custDvcElementsList.get(i).getDvcEqpTransTs().substring(5,7)+"/"+custDvcElementsList.get(i).getDvcEqpTransTs().substring(8,10)+"/"+custDvcElementsList.get(i).getDvcEqpTransTs().substring(0,4);
						}
					}else{

						throw new SysDataNotFoundException(ErrorConstants.DEVICE_CODE, "ERROR while retrieving device- Device Equipment Transaction");
					}
				}
				customer = new Customer(custId);
				RoutingUtil routingUtil = getUtility(RoutingUtil.class);
				//customer.setBillingSystem(routingUtil.getAndUpdateBillingSystemForCustomerId(custId, serviceHeader));
				customer.setBillingSystem(billSysId);
				customer.setVisionBillingSystem(RoutingUtil.getVisionBillSysFromDataGridBillSysId(customer.getBillingSystem()));
				Account account = new Account(customer, accountNum.toString());
				account.setEstablishedDate(currentTimeStamp);
				line = new Line(mdn);
				device.setSwitchActiveTimestamp(actvReqDate);
				device.setSimCardDisconnectIndicator(activeAtSwitchIndicator);
				device.setDeviceActivationDate(lastDvcEqpActivationDate);
				device.setSwitchDisconnectTimestamp(disconnReqDate);
				line.setDevice(device);
				account.addLine(line);
				customer.addAccount(account);
				customerList.add(customer);
			}
		}
		
		//TODO
		//cretate 3 dummy customers with  other billing system which are not existing 
		//Add commonHeaderMessage/commonHeaderError/errorCode for the remaining customers and it to the customerlist
		if (customerMap != null && !customerMap.isEmpty()) {
			for(String key: customerMap.keySet()) {
				customerList.add(customerMap.get(key));
			}
			
			try {
				if (customerList!=null && customerList.size() > 1) {
					Collections.sort(customerList, new Comparator<Customer>() {
			
						@Override
						public int compare(Customer cust1, Customer cust2) {
							if (cust1.getBillingSystem() != null && cust2.getBillingSystem() != null) {
								Integer custBillSys1 = Integer.parseInt(cust1.getBillingSystem());
								Integer custBillSys2 = Integer.parseInt(cust2.getBillingSystem());
								return custBillSys1.compareTo(custBillSys2);
							}
							return 0;
						}
						
					});
				}
			} catch (Exception e) {
				// do nothing
			}
		}
		
		setOutput(customerList);
	}
	
	private void checkCustomerMap(Map<String, Customer> customerMap) {
		if(customerMap.isEmpty()) {
			// if the map is empty and billingSystem is VISION, we need to add empty customer for each of the regions
			if(VISION.equals(billSystem)) {
				customerMap.put(BillingSystem.VISION_EAST.toString(), createEmptyCustomer(BillingSystem.VISION_EAST.toString()));
				customerMap.put(BillingSystem.VISION_WEST.toString(), createEmptyCustomer(BillingSystem.VISION_WEST.toString()));
				customerMap.put(BillingSystem.VISION_NORTH.toString(), createEmptyCustomer(BillingSystem.VISION_NORTH.toString()));
			}
			// if the map is empty and billingSystem is EAST/WEST/NORTH, we need to add empty customer for the region
			else {
				if(BillingSystem.VISION_EAST.toString().equals(billSystem)) {
					customerMap.put(BillingSystem.VISION_EAST.toString(), createEmptyCustomer(BillingSystem.VISION_EAST.toString()));
				}
				else if(BillingSystem.VISION_WEST.toString().equals(billSystem)) {
					customerMap.put(BillingSystem.VISION_WEST.toString(), createEmptyCustomer(BillingSystem.VISION_WEST.toString()));
				}
				else if(BillingSystem.VISION_NORTH.toString().equals(billSystem)) {
					customerMap.put(BillingSystem.VISION_NORTH.toString(), createEmptyCustomer(BillingSystem.VISION_NORTH.toString()));
				}
				else if(BillingSystem.VISION_B2B.toString().equals(billSystem)) {
					customerMap.put(BillingSystem.VISION_B2B.toString(), createEmptyCustomer(BillingSystem.VISION_B2B.toString()));
				}
			}
		} else {
			// if the map is NOT empty and billingSystem is VISION, we need to check if all three regions are added in the map. 
			// add the ones that is missing
			if(VISION.equals(billSystem)) {
				if(!customerMap.containsKey(BillingSystem.VISION_EAST.toString())) {
					customerMap.put(BillingSystem.VISION_EAST.toString(), createEmptyCustomer(BillingSystem.VISION_EAST.toString()));
				}
				if(!customerMap.containsKey(BillingSystem.VISION_WEST.toString())) {
					customerMap.put(BillingSystem.VISION_WEST.toString(), createEmptyCustomer(BillingSystem.VISION_WEST.toString()));
				}
				if(!customerMap.containsKey(BillingSystem.VISION_NORTH.toString())) {
					customerMap.put(BillingSystem.VISION_NORTH.toString(), createEmptyCustomer(BillingSystem.VISION_NORTH.toString()));
				}
			}
			// if the map is NOT empty and billingSystem is EAST/WEST/NORTH, if we dont have customer for that region we need to add empty customer for the region
			else {
				if(BillingSystem.VISION_EAST.toString().equals(billSystem) && !customerMap.containsKey(BillingSystem.VISION_EAST.toString())) {
					customerMap.put(BillingSystem.VISION_EAST.toString(), createEmptyCustomer(BillingSystem.VISION_EAST.toString()));
				}
				else if(BillingSystem.VISION_WEST.toString().equals(billSystem) && !customerMap.containsKey(BillingSystem.VISION_WEST.toString())) {
					customerMap.put(BillingSystem.VISION_WEST.toString(), createEmptyCustomer(BillingSystem.VISION_WEST.toString()));
				}
				else if(BillingSystem.VISION_NORTH.toString().equals(billSystem) && !customerMap.containsKey(BillingSystem.VISION_NORTH.toString())) {
					customerMap.put(BillingSystem.VISION_NORTH.toString(), createEmptyCustomer(BillingSystem.VISION_NORTH.toString()));
				}
				else if(BillingSystem.VISION_B2B.toString().equals(billSystem) && !customerMap.containsKey(BillingSystem.VISION_B2B.toString())) {
					customerMap.put(BillingSystem.VISION_B2B.toString(), createEmptyCustomer(BillingSystem.VISION_B2B.toString()));
				}
			}
		}
	}
	
	private Customer createEmptyCustomer(String billingSystem) {
		Customer customer = new Customer("");
		customer.setVisionBillingSystem(billingSystem);
		if (BillingSystem.VISION_EAST.toString().equals(billingSystem.toString())) {
			customer.setBillingSystem("1");
		} else if (BillingSystem.VISION_WEST.toString().equals(billingSystem.toString())) {
			customer.setBillingSystem("2");
		} else if (BillingSystem.VISION_NORTH.toString().equals(billingSystem.toString())) {
			customer.setBillingSystem("7");
		} else {
			customer.setBillingSystem("8");
		}
		CommonHeaderMessage cmnHdrMsg = new CommonHeaderMessage(customer);
		CommonHeaderError cmnHdrError = new CommonHeaderError(cmnHdrMsg);
		cmnHdrError.setCicsErrorCode("106");
		cmnHdrError.setErrorCode("106");
		cmnHdrError.setErrorMsg("ORDER NUMBER NOT FOUND");
		cmnHdrMsg.setCommonHeaderError(cmnHdrError);
		customer.setCommonHeaderMessage(cmnHdrMsg);
		return customer;
	}
	
	
}