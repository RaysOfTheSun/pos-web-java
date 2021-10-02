package com.raysofthesun.poswebjava.propose.controllers;

import com.raysofthesun.poswebjava.propose.constants.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.constants.FailedToFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.constants.ProposeApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProposalControllerAdvice {
	@ExceptionHandler({CannotFinalizeProposalException.class, FailedToFinalizeProposalException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object handleProposalError(ProposeApiException runtimeException) {
		return runtimeException.getMessage();
	}
}
