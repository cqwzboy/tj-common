package com.tj.common.lang;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * json数据结果构建类
 * @author silongz
 *
 */
public class JsonResultBuilder {

	/**
	 * 构建成功结果
	 * @param data - 返回的数据结构
	 * @return
	 */
	public static <T> JsonResult<T> buildSuccess(T data) {
		JsonResult<T> result = new JsonResult<T>() ;
		result.setData(data);
		return result ;
	}
	
	/**
	 * 构建带返回信息的成功结果
	 * @param data
	 * @param msg
	 * @return
	 */
	public static <T> JsonResult<T> buildSuccess(T data , String msg) {
		JsonResult<T> result = new JsonResult<T>() ;
		result.setData(data);
		result.setMsg(msg);
		return result ;
	}
	
	/**
	 * 构建成功结果的json串
	 * @param data - 返回的数据结构
	 * @return
	 */
	public static <T> String buildSuccessJson(T data) {
		return JSON.toJSONString(buildSuccess(data)) ;
	}
	
	/**
	 * 构建带错误信息的结果
	 * @param errorMsg
	 * @return
	 */
	public static <T> JsonResult<T> buildFailure(String errorMsg){
		return buildFailure(errorMsg, null) ;
	}
	
	/**
	 * 构建带错误信息，以及错误数据结构的结果
	 * @param errorMsg
	 * @param data
	 * @return
	 */
	public static <T> JsonResult<T> buildFailure(String errorMsg ,T data){
		JsonResult<T> result = new JsonResult<T>() ;
		result.setSuccess(false);
		result.setMsg(errorMsg);
		result.setData(data);
		return result ;
	}
	
	/**
	 * 构建带错误信息的结果Json串
	 * @param errorMsg
	 * @param data
	 * @return
	 */
	public static <T> String buildFailureJson(String errorMsg){
		return JSON.toJSONString(buildFailure(errorMsg)) ;
	}
	
	/**
	 * 构建带错误信息，以及错误数据结构的结果Json串
	 * @param errorMsg
	 * @param data
	 * @return
	 */
	public static <T> String buildFailureJson(String errorMsg , T data){
		return JSON.toJSONString(buildFailure(errorMsg, data)) ;
	}
	
	public static class JsonResult<T> implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -6473740887564515007L;

		private String msg = null ;
		
		private boolean success = true ;
		
		private T data = null ;
		
		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	
}
