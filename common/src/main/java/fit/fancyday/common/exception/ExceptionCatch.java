package fit.fancyday.common.exception;

import fit.fancyday.model.common.dtos.ResponseResult;
import fit.fancyday.model.common.enums.HttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice//生命周期，作用类型，Bean
@Slf4j
@ResponseBody
public class ExceptionCatch {
    /**
     * 处理不可控的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e) {
        e.printStackTrace();//异常捕捉之后，打印出具体位置和原因
        log.error("捕捉的错误是==>{}<==", e.getMessage());
        return ResponseResult.errorResult(HttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 处理自定义的异常
     */
    @ExceptionHandler(CustomException.class)
    public ResponseResult exception(CustomException e) {
        log.error("捕捉的错误是==>{}<==", e.getMessage());
        return ResponseResult.errorResult(e.getHttpCodeEnum());
    }
}
