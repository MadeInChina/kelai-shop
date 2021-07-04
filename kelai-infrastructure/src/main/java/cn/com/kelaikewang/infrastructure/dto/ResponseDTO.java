package cn.com.kelaikewang.infrastructure.dto;

import java.io.Serializable;

public class ResponseDTO implements Serializable {

    private String status;
    private String message;
    private Object data;

    public ResponseDTO(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public static ResponseDTO fail(String msg,Object data){
        return new ResponseDTO(ResponseStatus.FAIL,msg,data);
    }
    public static ResponseDTO fail(String msg){
        return new ResponseDTO(ResponseStatus.FAIL,msg,null);
    }
    public static ResponseDTO success(String msg){
        return new ResponseDTO(ResponseStatus.SUCCESS,msg,null);
    }
    public static ResponseDTO success(String msg,Object data){
        return new ResponseDTO(ResponseStatus.SUCCESS,msg,data);
    }
}
