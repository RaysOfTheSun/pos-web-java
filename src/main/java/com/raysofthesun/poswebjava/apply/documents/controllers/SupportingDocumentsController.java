package com.raysofthesun.poswebjava.apply.documents.controllers;

import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentSource;
import com.raysofthesun.poswebjava.apply.documents.models.DocumentUploadRequest;
import com.raysofthesun.poswebjava.apply.documents.models.SupportingDocument;
import com.raysofthesun.poswebjava.apply.documents.services.SupportingDocumentService;
import io.swagger.annotations.Api;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Api(tags = "Apply Document Upload Processes")
@RestController
@RequestMapping("/v1/apply")
public class SupportingDocumentsController {

	private final SupportingDocumentService supportingDocumentService;

	SupportingDocumentsController(SupportingDocumentService supportingDocumentService) {
		this.supportingDocumentService = supportingDocumentService;
	}

	@GetMapping("/{agentId}/applications/{applicationId}/documents")
	public Flux<SupportingDocument> getDocumentsByForOwnerIdByApplicationIdAndAgentId(
			@RequestParam String ownerId,
			@PathVariable String agentId, @PathVariable String applicationId) {

		return supportingDocumentService
				.getDocumentsByForOwnerIdByApplicationIdAndAgentId(ownerId, agentId, applicationId);
	}

	@PostMapping("/{agentId}/applications/{applicationId}/documents")
	public Mono<List<String>> saveDocuments(@PathVariable String agentId, @PathVariable String applicationId,
	                                        @RequestBody DocumentUploadRequest uploadRequest) {
		return supportingDocumentService.saveDocumentsFromRequest(uploadRequest, agentId, applicationId);
	}

	@GetMapping("/documents/{documentId}/source")
	public Mono<ApplicationDocumentSource> getDocumentSourceById(@PathVariable String documentId) {
		return supportingDocumentService.getDocumentSourceById(documentId);
	}

	@GetMapping(value = "/{agentId}/events")
	public Flux<ServerSentEvent<String>> listenToEventsForAgent(@PathVariable String agentId) {
		return supportingDocumentService.listenToEventsForAgent(agentId);
	}
}
