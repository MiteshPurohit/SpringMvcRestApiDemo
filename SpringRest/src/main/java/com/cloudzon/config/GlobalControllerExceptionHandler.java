package com.cloudzon.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import com.cloudzon.common.Constant;
import com.cloudzon.dto.ErrorDto;
import com.cloudzon.dto.ValidationErrorDTO;
import com.cloudzon.exception.BaseWebApplicationException;
import com.cloudzon.exception.FieldErrorException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	@ExceptionHandler(BaseWebApplicationException.class)
	@ResponseBody
	public ErrorDto webApplicationException(
			BaseWebApplicationException baseWebApplicationException,
			HttpServletResponse response) {
		response.setStatus(baseWebApplicationException.getStatus());
		return new ErrorDto(baseWebApplicationException.getErrorMessage(),
				baseWebApplicationException.getErrorCode(),
				baseWebApplicationException.getDeveloperMessage());
	}

	@ExceptionHandler(FieldErrorException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, Object> fieldErrorException(
			FieldErrorException fieldErrorException) {
		Map<String, Object> responseData = null;
		responseData = new HashMap<String, Object>();
		responseData.put(Constant.ERROR_KEY, fieldErrorException
				.getValidationErrorDTO().getFieldErrors());
		return responseData;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, Object> processValidationError(
			MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		Map<String, Object> responseData = null;
		responseData = new HashMap<String, Object>();
		responseData.put(Constant.ERROR_KEY, processFieldErrors(fieldErrors)
				.getFieldErrors());
		return responseData;
	}

	private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
		ValidationErrorDTO errorDTO = new ValidationErrorDTO();
		for (FieldError fieldError : fieldErrors) {
			errorDTO.addFieldError(fieldError.getField(),
					fieldError.getDefaultMessage());
		}
		return errorDTO;
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public Map<String, String> httpMessageNotReadableException(
			HttpMessageNotReadableException exception) {
		Map<String, String> responseData = new HashMap<String, String>();
		responseData.put(Constant.MESSAGE_KEY, "Not valid JSON Format");
		return responseData;
	}

	@ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public Map<String, String> httpMediaTypeNotSupportedException(
			HttpMediaTypeNotSupportedException exception) {
		Map<String, String> responseData = new HashMap<String, String>();
		responseData.put(Constant.MESSAGE_KEY, exception.getMessage());
		return responseData;
	}

	@ExceptionHandler(value = HttpClientErrorException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public Map<String, String> httpClientErrorException(
			HttpClientErrorException exception) {
		Map<String, String> responseData = new HashMap<String, String>();
		responseData.put(Constant.MESSAGE_KEY,
				"Unauthorized - Not valid access token");
		exception.printStackTrace();
		return responseData;
	}

}