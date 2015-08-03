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
public class PhoneFormatException extends Exception {

    public PhoneFormatException(String errorMessage) {
        super(errorMessage);
    }
}
