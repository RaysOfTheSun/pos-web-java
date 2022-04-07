package com.raysofthesun.poswebjava.propose.proposals.advices;

import com.raysofthesun.poswebjava.propose.proposals.exceptions.CannotFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.FailedToFinalizeProposalException;
import com.raysofthesun.poswebjava.propose.proposals.exceptions.ProposeApiException;
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
