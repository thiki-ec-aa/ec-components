package net.thiki.ec.component.log.autoconfigure;

import net.thiki.ec.component.exception.AssertionException;
import net.thiki.ec.component.exception.FailureResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@ControllerAdvice
public class AssertionExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AssertionExceptionHandler.class);
    /**
     *
     * @param re
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<FailureResponseBody<?>> handlerRE(RuntimeException re) {
        //default:
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        if (re instanceof AssertionException) {
            AssertionException ae = (AssertionException) re;
            final String httpStatusAsStr = ae.getSystemParams().get(AssertionException.SystemParams_Key_HttpStatus);
            if (httpStatusAsStr != null) {
                try {
                    httpStatus = Integer.parseInt(httpStatusAsStr);
                } catch (NumberFormatException ignored) {
                    //ignored
                }
            } else {
                //ignored
            }
            if (logger.isDebugEnabled()){
                logger.debug(re.getMessage());
                logger.debug(Arrays.toString(re.getStackTrace()));
            }
            return ResponseEntity.status(httpStatus)
                    .body(new FailureResponseBody<>(String.valueOf(ae.getCode()), ae.getMessage(), ae.getBizParams()))
                    ;
        } else {
            // other runtime exceptions
            if (logger.isDebugEnabled()){
                logger.debug(re.getMessage());
                logger.debug(Arrays.toString(re.getStackTrace()));
            }
            return ResponseEntity.status(httpStatus)
                    .body(new FailureResponseBody<>(String.valueOf(AssertionException.CodeUnknown), re.getMessage(), re))
                    ;
        }
    }

}
