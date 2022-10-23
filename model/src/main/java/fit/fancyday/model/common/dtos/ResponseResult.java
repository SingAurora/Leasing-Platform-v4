package fit.fancyday.model.common.dtos;

import fit.fancyday.model.common.enums.HttpCodeEnum;

import java.io.Serializable;

/**
 * 响应结果
 * 通用返回结果类
 *
 * @author asus
 * @date 2022/10/10
 */
public class ResponseResult<T> implements Serializable {
    //=============================成员属性
    private String host;

    private Integer code;

    private String errorMessage;

    private T data;

    //=============================构造方法
    public ResponseResult() {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }

    //====================================静态方法

    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setHttpCodeEnum(HttpCodeEnum.SUCCESS, HttpCodeEnum.SUCCESS.getErrorMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult okResult() {
        ResponseResult result = new ResponseResult();
        return result.ok(200, null, "SUCCESS");
    }

    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    public static ResponseResult errorResult(HttpCodeEnum enums) {
        return setHttpCodeEnum(enums, enums.getErrorMessage());
    }

    public static ResponseResult errorResult(HttpCodeEnum enums, String errorMessage) {
        return setHttpCodeEnum(enums, errorMessage);
    }

    public static ResponseResult setHttpCodeEnum(HttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getErrorMessage());
    }

    private static ResponseResult setHttpCodeEnum(HttpCodeEnum enums, String errorMessage) {
        return okResult(enums.getCode(), errorMessage);
    }

    //=============================泛型构造方法

    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }

    //===============================setter and getter
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "\nhost='" + host + '\'' +
                "\ncode=" + code +
                "\nerrorMessage='" + errorMessage + '\'' +
                "\ndata=" + data +
                '}';
    }
}
