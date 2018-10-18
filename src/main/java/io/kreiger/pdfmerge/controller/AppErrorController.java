package io.kreiger.pdfmerge.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppErrorController implements ErrorController {
    private final Logger logger = LogManager.getLogger(getClass());
    private final static String ERROR_PATH = "/error";

    @RequestMapping("/error")
    public ModelAndView handleError(ModelAndView mav, HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object errorRequestURI = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.error("Status Code: " + request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
                logger.error("Request URI: " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

                mav.addObject("errorMessage", "That page was not found on this server");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("Exception: " + request.getAttribute(RequestDispatcher.ERROR_EXCEPTION));
                logger.error("Status Code: " + request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
                logger.error("Message: " + request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                logger.error("Request URI: " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

                mav.addObject("errorMessage", errorMessage.toString());
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                logger.error("Status Code: " + request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
                logger.error("Message: " + request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                logger.error("Request URI: " + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

                mav.addObject("errorMessage", errorMessage.toString());
            }
        }

        mav.addObject("statusCode", status.toString());
        mav.addObject("errorRequestURI", errorRequestURI.toString());
        mav.setViewName("error/error");

        return mav;
    }

    @Override
    public String getErrorPath() {
        return this.ERROR_PATH;
    }
}
