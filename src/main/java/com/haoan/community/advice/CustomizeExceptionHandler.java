package com.haoan.community.advice;

import com.haoan.community.dto.ResultDTO;
import com.haoan.community.exception.CustomizeErrorCode;
import com.haoan.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    Object handle(HttpServletRequest request, Throwable e, Model model) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            //向页面反馈json数据
            if (e instanceof CustomizeException){
                return ResultDTO.errorOf((CustomizeException)e);
            }else {
                return ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }

        }else {
            //返回错误页面
            if (e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }else {
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }

}
