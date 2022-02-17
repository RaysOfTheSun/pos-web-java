package com.raysofthesun.poswebjava.apply.documents.mappers;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.raysofthesun.poswebjava.apply.documents.models.ApplicationDocumentMeta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface DocumentMapper {
    DocumentMapper MAPPER = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "fsFile", target = "ownerId", qualifiedByName = "metaToOwnerId")
    @Mapping(source = "fsFile", target = "uploadDate", qualifiedByName = "metaToUploadDate")
    @Mapping(source = "fsFile", target = "fileName", qualifiedByName = "metaToFileName")
    @Mapping(source = "fsFile", target = "documentId", qualifiedByName = "metaToDocumentId")
    @Mapping(source = "fsFile", target = "documentType", qualifiedByName = "metaToDocumentType")
    @Mapping(source = "fsFile", target = "documentGroup", qualifiedByName = "metaToDocumentGroup")
    ApplicationDocumentMeta mapGridFsFileToDocumentMeta(GridFSFile fsFile);

    @Named("metaToDocumentId")
    default String fsFileMetadataToDocumentId(GridFSFile fsFile) {
        return fsFile.getObjectId().toHexString();
    }

    @Named("metaToFileName")
    default String fsFileMetadataToFileName(GridFSFile fsFile) {
        return fsFile.getFilename();
    }

    @Named("metaToUploadDate")
    default String fsFileUploadDateToString(GridFSFile fsFile) {
        return fsFile.getUploadDate().toInstant().toString();
    }

    @Named("metaToOwnerId")
    default String fsFileMetadataToOwnerId(GridFSFile fsFile) {
        return Optional
                .ofNullable(fsFile.getMetadata().getString("ownerId"))
                .orElseGet(() -> "");
    }

    @Named("metaToDocumentType")
    default String fsFileMetadataToDocumentType(GridFSFile fsFile) {
        return Optional
                .ofNullable(fsFile.getMetadata().getString("documentType"))
                .orElseGet(() -> "");
    }

    @Named("metaToDocumentGroup")
    default String fsFileMetadataToDocumentGroup(GridFSFile fsFile) {
        return Optional
                .ofNullable(fsFile.getMetadata().getString("documentGroup"))
                .orElseGet(() -> "");
    }
}
