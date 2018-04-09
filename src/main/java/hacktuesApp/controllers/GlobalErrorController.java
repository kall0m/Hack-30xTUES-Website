package hacktuesApp.controllers;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Profile("prod")
public class GlobalErrorController  implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public void error(HttpServletResponse response) throws IOException {
        int status = response.getStatus();
        switch (status) {
            case 404:
                response.sendRedirect("/404");
                break;
            case 401:
                response.sendRedirect("/401");
                break;
            case 403:
                response.sendRedirect("/403");
                break;
            case 500:
                response.sendRedirect("/500");
                break;
            default:
                response.sendRedirect("/404");
        }

    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

/*@ControllerAdvice
public class ErrorController {
    /*private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

    @GetMapping("/error")
    public String renderErrorPage(Model model, HttpServletRequest httpRequest) {
        int httpErrorCode = getErrorCode(httpRequest);

        switch (httpErrorCode) {
            case 400: {
                model.addAttribute("view", "error/400");
                //errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                model.addAttribute("view", "error/401");
                //errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                model.addAttribute("view", "error/404");
                //errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                model.addAttribute("view", "error/500");
                //errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }

        model.addAttribute("view", "error/404");
        return "base-layout";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}*/
