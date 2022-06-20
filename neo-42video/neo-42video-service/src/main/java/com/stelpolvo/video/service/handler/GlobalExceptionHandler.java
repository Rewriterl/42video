package com.stelpolvo.video.service.handler;

import com.stelpolvo.video.domain.RespBean;
import com.stelpolvo.video.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public RespBean commonExceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        String message = e.getMessage();
        if (e instanceof ConditionException) {
            String code = ((ConditionException) e).getCode();
            return RespBean.error(code, message);
        }
        if (message == null) {
            message = "未知错误";
        }
        return RespBean.error("500", message);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public RespBean MyExceptionHandle(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(error -> {
                System.out.println(error.getField() + error.getDefaultMessage());
                errorMsg.append(error.getDefaultMessage()).append("!");
            });
        }

        exception.printStackTrace();
        return RespBean.error(errorMsg.toString());
    }
}
