package com.example.panservice.Entity;

public class Panrequest {
	
	private String number;
    private String returnIndividualTaxComplianceInfo;

  

    // Getters and setters
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

	public String getReturnIndividualTaxComplianceInfo() {
		return returnIndividualTaxComplianceInfo;
	}

	public void setReturnIndividualTaxComplianceInfo(String returnIndividualTaxComplianceInfo) {
		this.returnIndividualTaxComplianceInfo = returnIndividualTaxComplianceInfo;
	}

	@Override
	public String toString() {
		return "Panrequest [number=" + number + ", returnIndividualTaxComplianceInfo="
				+ returnIndividualTaxComplianceInfo + "]";
	}
	

}
