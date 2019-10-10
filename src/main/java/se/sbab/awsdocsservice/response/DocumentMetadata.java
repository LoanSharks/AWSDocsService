package se.sbab.awsdocsservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.workdocs.model.DocumentVersionMetadata;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {

    private String name;
    private Long size;
    private String docId;

    public static DocumentMetadata fromAwsDocumentMetadata(software.amazon.awssdk.services.workdocs.model.DocumentMetadata doc) {

        DocumentVersionMetadata md = doc.latestVersionMetadata();
        return DocumentMetadata.builder().docId(doc.id())
                .name(md.name()).size(md.size()).build();
    }
}
