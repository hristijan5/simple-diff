package eu.xyan.demo.simplediff.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when there is no such data pair
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataPairException extends Exception {

    public NoDataPairException(Long id) {
        super("The data with id " + id + " is missing");
    }

}
