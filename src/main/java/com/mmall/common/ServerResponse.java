package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private final static long serialVersionUID=1L;

    private int status;

    /**
     * true/false
     */
    private boolean flag;
    /**
     * 提示或者描述信息
     */
    private String msg;
    /**
     * 其他类型的返回数据
     */
    private T data;

    private ServerResponse(int status){
        this.status=status;
        setFlag();
    }

    private ServerResponse(int status, String msg){
        this.status = status;
        this.msg = msg;
        setFlag();
    }

    private ServerResponse(int status, T data){
        this.status = status;
        this.data = data;
        setFlag();
    }

    private ServerResponse(int status, String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
        setFlag();
    }

    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public boolean  getflag() {
        return flag;
    }

    public ServerResponse removeData(){
        this.data=null;
        return this;
    }

    /**
     * 将本对象状态转换至 false 状态
     * @return
     */
    public ServerResponse<T> toError(){
        this.status = ResponseCode.ERROR.getCode();
        setFlag();
        return this;
    }

    /**
     * 将本对象状态转换至 false 状态，并添加描述
     * @param desc
     * @return
     */
    public ServerResponse<T> toError(String desc){
        this.status = ResponseCode.ERROR.getCode();
        this.msg = desc;
        setFlag();
        return this;
    }

    /**
     * 将本对象状态装换至 false 状态，并添加一个对象作为其他返回数据
     * @param data
     * @return
     */
    public ServerResponse<T> toError(T data){
        this.status = ResponseCode.ERROR.getCode();
        this.data = data;
        setFlag();
        return this;
    }

    /**
     * 将本对象状态装换至 false 状态，并添加描述、添加一个对象作为其他返回数据
     * @param desc
     * @param data
     * @return
     */
    public ServerResponse<T> toError(String desc, T data){
        this.status = ResponseCode.ERROR.getCode();
        this.data = data;
        this.msg = desc;
        setFlag();
        return this;
    }

    /**
     * 将本对象状转换至 true 状态
     * @return
     */
    public ServerResponse<T> toSuccess(){
        this.status = ResponseCode.SUCCESS.getCode();
        setFlag();
        return this;
    }

    /**
     * 将本对象状转换至 true 状态,并添加描述
     * @param desc
     * @return
     */
    public ServerResponse<T> toSuccess(String desc){
        this.status = ResponseCode.SUCCESS.getCode();
        this.msg = desc;
        setFlag();
        return this;
    }

    /**
     * 将本对象状态转换至 true 状态，并添加一个对象作为其他返回数据
     * @param data
     * @return
     */
    public ServerResponse<T> toSuccess(T data){
        this.status = ResponseCode.SUCCESS.getCode();
        this.data =  data;
        setFlag();
        return this;
    }

    /**
     * 将本对象状态转换至 true 状态，并添加描述和一个对象作为其他返回数据
     * @param desc
     * @param data
     * @return
     */
    public ServerResponse<T> toSuccess(String desc, T data){
        this.status = ResponseCode.SUCCESS.getCode();
        this.data =  data;
        this.msg = desc;
        setFlag();
        return this;
    }

    /**
     * 按true 创建一个model
     * @param <T>
     * @return
     */
    public static<T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDesc());
    }

    /**
     * 按true 创建一个model，并添加描述
     * @param msg
     * @param <T>
     * @return
     */
    public static<T> ServerResponse<T> createBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    /**
     * 按true 创建一个model，并添一个对象作为返回数据
     * @param <T>data
     * @return
     */
    public static<T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    /**
     * 按true 创建一个model，并添加描述和一个对象作为返回数据
     * @param msg
     * @param <T> data
     * @return
     */
    public static<T> ServerResponse<T> createBySuccess(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    /**
     * 按false 创建一个model
     * @return
     */
    public static<T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    /**
     * 按false 创建一个model,并且添加错误描述
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static<T> ServerResponse<T> createByError(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    /**
     * 按false 创建一个model,并自定义错误的状态码和添加错误描述
     * @param errorcode
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static<T> ServerResponse<T> createByError(int errorcode, String errorMessage){
        return new ServerResponse<T>(errorcode,errorMessage);
    }

    /**
     * 按false 创建一个model，并自定义错误的状态码和添加错误描述以及一个对象作为其他返回数据
     * @param errorcode
     * @param errorMessage
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ServerResponse<T> createByError(int errorcode, String errorMessage,T data){
        return new ServerResponse<T>(errorcode,errorMessage,data);
    }

    private void setFlag(){
        this.flag = isSuccess();
    }


}
