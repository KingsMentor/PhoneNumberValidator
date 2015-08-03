/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appzonegroup.com.phonenumberverifier;

/**
 *
 * @author CrowdStar
 */
public class PhoneModel {

    private String phoneNumber = "";
    private boolean isValidPhoneNumber = false;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isValidPhoneNumber() {
        return isValidPhoneNumber;
    }

    public void setIsValidPhoneNumber(boolean isValidPhoneNumber) {
        this.isValidPhoneNumber = isValidPhoneNumber;
    }

}
