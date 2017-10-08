/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belvi.validator;

/**
 *
 * @author CrowdStar
 */
public class PhoneFormatException extends Exception {

    public PhoneFormatException(String errorMessage) {
        super(errorMessage);
    }
}
