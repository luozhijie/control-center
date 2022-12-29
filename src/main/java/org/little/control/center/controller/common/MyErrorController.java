package org.little.control.center.controller.common;


import org.little.control.center.model.MyErrorCode;
import net.xctec.framework.core.entity.MyResponse;
import net.xctec.framework.core.service.IErrorCode;
import net.xctec.framework.core.service.MyErrorMessage;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 统一错误处理，捕获400， 404等错误
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class MyErrorController extends AbstractErrorController {
    private final static String DEFAULT_ERROR_VIEW = "error";//错误信息页

    public MyErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * 情况1：若预期返回类型为text/html,则返回错误信息页(View).
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request) {
        Map<String,Object> map = getErrorAttributes(request, false);
        return new ModelAndView(DEFAULT_ERROR_VIEW, map);
    }

    /**
     * 情况2：其它预期类型 则返回详细的错误信息(JSON).
     */
    @RequestMapping
    @ResponseBody
    public MyResponse error(HttpServletRequest request) {
        IErrorCode errorCode = getErrorCode(request);

        MyResponse commonResponse = new MyResponse();
        commonResponse.setCode(errorCode.getCode());
        commonResponse.setMessage(MyErrorMessage.getMessage(errorCode));
        commonResponse.setData(null);

        return commonResponse;
    }

    @Override
    public String getErrorPath() {//获取映射路径
        return DEFAULT_ERROR_VIEW;
    }

    private IErrorCode getErrorCode(HttpServletRequest request) {
        IErrorCode errorCode = MyErrorCode.getByCode(getStatus(request).value());
        if (errorCode == null) {
            errorCode = MyErrorCode.INTERNAL_SERVER_ERROR;
        }

        return errorCode;
    }
}