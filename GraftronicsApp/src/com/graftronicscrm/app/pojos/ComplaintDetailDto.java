package com.graftronicscrm.app.pojos;

public class ComplaintDetailDto {

	private String callID;
	private String customerID;
	private String callType;
	private String complaintDetails;
	private String callLoginDate;
	private String callLoginTime;
	private String status;
	private String feedBack;
	private String contactPerson;
	private String description;
	private String customerName;
	private String customerAddress;
	private String customerCellNo;
	private String customerLocation;
	private String empName;

	public String getCallID() {
		return callID;
	}

	public void setCallID(String callID) {
		this.callID = callID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getComplaintDetails() {
		return complaintDetails;
	}

	public void setComplaintDetails(String complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

	public String getCallLoginDate() {
		return callLoginDate;
	}

	public void setCallLoginDate(String callLoginDate) {
		this.callLoginDate = callLoginDate;
	}

	public String getCallLoginTime() {
		return callLoginTime;
	}

	public void setCallLoginTime(String callLoginTime) {
		this.callLoginTime = callLoginTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFeedBack() {
		return feedBack;
	}

	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerCellNo() {
		return customerCellNo;
	}

	public void setCustomerCellNo(String customerCellNo) {
		this.customerCellNo = customerCellNo;
	}

	public String getCustomerLocation() {
		return customerLocation;
	}

	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	@Override
	public String toString() {
		return "ComplaintDetailDto [callID=" + callID + ", customerID=" + customerID + ", callType=" + callType
				+ ", complaintDetails=" + complaintDetails + ", callLoginDate=" + callLoginDate + ", callLoginTime="
				+ callLoginTime + ", status=" + status + ", feedBack=" + feedBack + ", contactPerson=" + contactPerson
				+ ", description=" + description + ", customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", customerCellNo=" + customerCellNo + ", customerLocation=" + customerLocation
				+ ", empName=" + empName + "]";
	}

}
