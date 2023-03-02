package com.UPBEATg11.API;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There are some incorrect parameters.")
public class IllegalConfig extends Exception{
    private String message;
    public IllegalConfig(String message) { super(message); }
}
