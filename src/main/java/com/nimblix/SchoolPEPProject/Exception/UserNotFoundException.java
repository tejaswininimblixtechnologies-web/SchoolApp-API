package com.nimblix.SchoolPEPProject.Exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String s){
        super("User not found");
    }
}
