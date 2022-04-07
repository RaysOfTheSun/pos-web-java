//package com.raysofthesun.poswebjava.apply.applications.services.core;
//
//import com.raysofthesun.poswebjava.apply.applications.emums.ApplicationPaymentMethod;
//import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationMeta;
//import com.raysofthesun.poswebjava.apply.applications.models.core.application.ApplicationPaymentInfo;
//import com.raysofthesun.poswebjava.apply.insureds.models.core.insured.Insured;
//import com.raysofthesun.poswebjava.core.common.services.PosWebService;
//import com.raysofthesun.poswebjava.core.configuration.enums.PosDocumentType;
//import com.raysofthesun.poswebjava.core.configuration.models.PosConfig;
//import com.raysofthesun.poswebjava.core.configuration.models.PosDocumentRequirement;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public abstract class ApplicationDocumentService implements PosWebService {
//    private final PosConfig posConfig;
//
//    public ApplicationDocumentService(PosConfig posConfig) {
//        this.posConfig = posConfig;
//    }
//
//    private PosDocumentRequirement getDocumentType(PosDocumentType posDocumentType) {
//        return this.posConfig.getDocumentRequirements().get(posDocumentType.toString());
//    }
//
//    public List<PosDocumentRequirement> getDocumentReqsForApplication(ApplicationMeta applicationMeta) {
//        return this.getRequiredPaymentDocsForApplication(applicationMeta);
//    }
//
//    private List<PosDocumentRequirement> getDocumentReqsForInsured(Insured insured) {
//        return this.getPrimaryIdRequirementForInsured(insured);
//    }
//
//    private List<PosDocumentRequirement> getRequiredPaymentDocsForApplication(ApplicationMeta applicationMeta) {
//        List<PosDocumentRequirement> requiredPaymentDocs = new ArrayList<>();
//        ApplicationPaymentInfo applicationPaymentInfo = applicationMeta.getPaymentInfo();
//
//        Optional.ofNullable(applicationPaymentInfo.getPaymentMethod()).ifPresent((paymentMethod) -> {
//            boolean isReceiptNeeded = paymentMethod.equals(ApplicationPaymentMethod.CASH);
//
//            if (isReceiptNeeded) {
//                requiredPaymentDocs.add(this.getDocumentType(PosDocumentType.PAYMENT_RECEIPT));
//            }
//        });
//
//        return requiredPaymentDocs;
//    }
//
//    private List<PosDocumentRequirement> getPrimaryIdRequirementForInsured(Insured insured) {
//        PosDocumentType identificationInfoType = Optional
//                .ofNullable(insured.getIdentificationInfo().getType())
//                .map(PosDocumentType::valueOf)
//                .orElse(PosDocumentType.NATIONAL_ID);
//
//        PosDocumentRequirement idDocumentReq = this.getDocumentType(identificationInfoType);
//
//        return List.of(idDocumentReq);
//    }
//
//    public Map<String, List<PosDocumentRequirement>> getRequiredDocsForApplication(ApplicationMeta applicationMeta,
//                                                                                   List<Insured> insureds) {
//        List<PosDocumentRequirement> applicationDocReqs = this.getDocumentReqsForApplication(applicationMeta);
//        Map<String, List<PosDocumentRequirement>> insuredToRequirementMap = insureds
//                .stream()
//                .collect(Collectors.toMap(Insured::getId, this::getDocumentReqsForInsured));
//
//        if (applicationDocReqs.size() > 0) {
//            insuredToRequirementMap.get(applicationMeta.getOwnerId()).addAll(applicationDocReqs);
//        }
//
//        return insuredToRequirementMap;
//    }
//}
