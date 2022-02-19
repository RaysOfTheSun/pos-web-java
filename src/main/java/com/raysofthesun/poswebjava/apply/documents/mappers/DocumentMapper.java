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

    @Mapping(source = ".", target = "ownerId", qualifiedByName = "metaToOwnerId")
    @Mapping(source = ".", target = "uploadDate", qualifiedByName = "metaToUploadDate")
    @Mapping(source = ".", target = "fileName", qualifiedByName = "metaToFileName")
    @Mapping(source = ".", target = "documentId", qualifiedByName = "metaToDocumentId")
    @Mapping(source = ".", target = "documentType", qualifiedByName = "metaToDocumentType")
    @Mapping(source = ".", target = "documentGroup", qualifiedByName = "metaToDocumentGroup")
    @Mapping(source = ".", target = "documentIndex", qualifiedByName = "metaToDocumentIndex")
    ApplicationDocumentMeta mapGridFsFileToDocumentMeta(GridFSFile fsFile);

    @Named("metaToDocumentIndex")
    default String fsFileMetadataToDocumentIndex(GridFSFile fsFile) {
        return fsFile.getMetadata().getString("documentIndex");
    }

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
