//package com.raysofthesun.poswebjava.apply.documents.controllers;
//
//import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentSource;
//import com.raysofthesun.poswebjava.apply.documents.models.DocumentUploadRequest;
//import com.raysofthesun.poswebjava.apply.documents.models.SupportingDocument;
//import com.raysofthesun.poswebjava.apply.documents.services.SupportingDocumentService;
//import io.swagger.annotations.Api;
//import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
//import org.springframework.http.codec.ServerSentEvent;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
//@Api(tags = "Apply Document Upload Processes")
//@RestController
//@RequestMapping("/v1/apply")
//public class SupportingDocumentsController {
//
//    private final ReactiveGridFsTemplate gridFsTemplate;
//    private final SupportingDocumentService supportingDocumentService;
//
//    SupportingDocumentsController(SupportingDocumentService supportingDocumentService, ReactiveGridFsTemplate gridFsTemplate) {
//        this.gridFsTemplate = gridFsTemplate;
//        this.supportingDocumentService = supportingDocumentService;
//    }
//
//    @GetMapping("/{agentId}/applications/{applicationId}/documents")
//    public Flux<SupportingDocument> getDocumentsByForOwnerIdByApplicationIdAndAgentId(
//            @RequestParam String ownerId,
//            @PathVariable String agentId, @PathVariable String applicationId) {
//
//        return supportingDocumentService
//                .getDocumentsByForOwnerIdByApplicationIdAndAgentId(ownerId, agentId, applicationId);
//    }
//
//    @PostMapping("/{agentId}/applications/{applicationId}/documents")
//    public Mono<List<String>> saveDocuments(@PathVariable String agentId, @PathVariable String applicationId,
//                                            @RequestBody DocumentUploadRequest uploadRequest) {
//        return supportingDocumentService.saveDocumentsFromRequest(uploadRequest, agentId, applicationId);
//    }
//
//    @GetMapping("/documents/{documentId}/source")
//    public Mono<ApplicationDocumentSource> getDocumentSourceById(@PathVariable String documentId) {
//        return supportingDocumentService.getDocumentSourceById(documentId);
//    }
//
//    @GetMapping(value = "/{agentId}/events")
//    public Flux<ServerSentEvent<String>> listenToEventsForAgent(@PathVariable String agentId) {
//        return supportingDocumentService.listenToEventsForAgent(agentId);
//    }
//
////    @PostMapping(value = "/multipart")
////    public Flux<Map<String, String>> uploadFileMultipart(@RequestPart("files") Flux<FilePart> filePartMono,
////                                                         @RequestPart("fileIndexes") Flux<RawDocumentMetadata> fileIndexes) {
////
////        return Flux.zip(filePartMono, fileIndexes)
////                .flatMap((fileIndexPair) -> {
////                    final FilePart filePart = fileIndexPair.getT1();
////                    final RawDocumentMetadata meta = fileIndexPair.getT2();
////                    return gridFsTemplate
////                            .store(filePart.content(), filePart.filename(), meta)
////                            .map((objectId -> Map.of("id", objectId.toHexString(),
////                                    "index", Integer.toString(meta.fileInd))));
////
////                }).log();
////
////
////    }
////
////    @GetMapping("/multipart/{id}")
////    public Flux<Void> getFilesById(@PathVariable String id, ServerWebExchange exchange) {
////        return gridFsTemplate
////                .findOne(query(where("_id").is(id)))
////                .flatMap(gridFsTemplate::getResource)
////                .flatMapMany(resource -> exchange.getResponse().writeWith(resource.getDownloadStream()));
////
////    }
//}
