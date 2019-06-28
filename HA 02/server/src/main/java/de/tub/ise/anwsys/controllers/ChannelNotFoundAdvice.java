// used to handle not found exceptions (convert it to 404 error)

package de.tub.ise.anwsys.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class ChannelNotFoundAdvice {

    @ResponseBody //signals that this advice is rendered straight into the response body
    @ExceptionHandler(ChannelNotFoundException.class) // configures the advice to only respond if an ChannelNotFoundException is thrown.
    @ResponseStatus(HttpStatus.NOT_FOUND) // says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.

    // The body of the advice generates the content. In this case, it gives the message of the exception.
    String channelNotFoundHandler(ChannelNotFoundException ex) {
//        return ex.getMessage(); // return error message only as body
        return ""; // return nothing in body, like original server
    }
}