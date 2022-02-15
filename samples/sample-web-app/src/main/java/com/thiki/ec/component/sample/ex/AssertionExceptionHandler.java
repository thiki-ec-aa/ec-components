package com.thiki.ec.component.sample.ex;

import com.thiki.ec.component.sample.FailureResponseBody;
import net.thiki.ec.component.exception.AssertionException;
import net.thiki.ec.component.log.starter.Greeter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ControllerAdvice
public class AssertionExceptionHandler {

    public AssertionExceptionHandler(Greeter greeter) {
        greeter.greet();
    }

    /**
     *
     * @param ae
     * @return
     */
    @ExceptionHandler(AssertionException.class)
    @ResponseBody
    public ResponseEntity<FailureResponseBody<Map<String, String>>> handlerAE(AssertionException ae) {
        //default:
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        final String httpStatusAsStr = ae.getSystemParams().get(AssertionException.SystemParams_Key_HttpStatus);
        if (httpStatusAsStr != null){
            try{
                httpStatus = Integer.parseInt(httpStatusAsStr);
            }catch (NumberFormatException ignored){
                //ignored
            }
        }else{
            //ignored
        }

        return ResponseEntity.status(httpStatus)
                .body(new FailureResponseBody<>(String.valueOf(ae.getCode()), ae.getMessage(), ae.getBizParams()))
                ;

    }
}
