package com.raysofthesun.poswebjava.apply.services;

import com.raysofthesun.poswebjava.apply.constants.EventType;
import com.raysofthesun.poswebjava.apply.models.document.ApplicationDocumentSource;
import com.raysofthesun.poswebjava.apply.models.document.DocumentUploadRequest;
import com.raysofthesun.poswebjava.apply.models.document.SupportingDocument;
import com.raysofthesun.poswebjava.apply.repositories.SupportingDocumentRepository;
import com.raysofthesun.poswebjava.apply.repositories.SupportingDocumentSourceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SupportingDocumentService {

	private final Logger logger = Logger.getLogger(SupportingDocumentService.class.getName());
	private final Sinks.Many<String> documentEventsSink = Sinks.many().multicast().directAllOrNothing();
	private final SupportingDocumentRepository documentRepository;
	private final SupportingDocumentSourceRepository documentSourceRepository;

	SupportingDocumentService(SupportingDocumentRepository documentRepository,
	                          SupportingDocumentSourceRepository documentSourceRepository) {
		this.documentRepository = documentRepository;
		this.documentSourceRepository = documentSourceRepository;
	}

	public Flux<SupportingDocument> getDocumentsByForOwnerIdByApplicationIdAndAgentId(String ownerId, String agentId,
	                                                                                  String applicationId) {
		return documentRepository.findAllByOwnerIdAndApplicationIdOrderByIndex(ownerId, applicationId);
	}

	public Mono<List<String>> saveDocumentsFromRequest(DocumentUploadRequest request, String agentId,
	                                                   String applicationId) {
		logger.log(Level.INFO, "=== starting saving of {0} documents",
				new Object[]{request.getDocuments().size()});

		return Flux
				.fromIterable(request.getDocuments())
				.parallel()
				.runOn(Schedulers.parallel())
				.map(supportingDocument -> {
					ApplicationDocumentSource documentSource =
							new ApplicationDocumentSource(supportingDocument.getId(), supportingDocument.getSrc());
					final int documentIndex = request
							.getDocuments()
							.indexOf(supportingDocument);

					supportingDocument.setSrc(null);
					supportingDocument.setIndex(documentIndex);
					supportingDocument.setAgentId(agentId);
					supportingDocument.setApplicationId(applicationId);

					return List.of(supportingDocument, documentSource);
				})
				.flatMap(documentAndSource -> {
					SupportingDocument document = (SupportingDocument) documentAndSource.get(0);
					ApplicationDocumentSource documentSource = (ApplicationDocumentSource) documentAndSource.get(1);

					return documentRepository
							.save(document)
							.flatMap((s) -> documentSourceRepository.save(documentSource))
							.map(ApplicationDocumentSource::getId);
				})
				.sequential()
				.collectList()
				.doOnSuccess((ids) -> {
					logger.log(Level.INFO, "=== Saved {0} documents", new Integer[]{request.getDocuments().size()});
					notifyClientsNeedsRefresh(agentId, applicationId, request.getOwnerIds());
				});
	}

	public Flux<ServerSentEvent<String>> listenToEventsForAgent(String agentId) {
		return documentEventsSink
				.asFlux()
				.filterWhen((message) -> Mono.just(message.startsWith(agentId)))
				.map((message) -> ServerSentEvent
						.<String>builder()
						.data(message)
						.build()
				);
	}

	private void notifyClientsNeedsRefresh(String agentId, String applicationId, List<String> ownerIds) {
		logger.info("=== notifying client");
		String ownerIdsToAppend = String.join(",", ownerIds);
		documentEventsSink.tryEmitNext(String.format("%s:%s:%s:%s",
				agentId, applicationId, ownerIdsToAppend, EventType.DOCUMENTS_UPLOADED));
		logger.info("=== client notified");
	}

}
