package se.sbab.awsdocsservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.sbab.awsdocsservice.GetUserDocs;
import se.sbab.awsdocsservice.response.DocumentMetadata;

import java.util.List;
import java.util.stream.Collectors;

@RestController(value = "document")
@CrossOrigin(origins = "http://localhost:3000")
public class AwsDocsResource {

    private final GetUserDocs getUserDocs;

    @Autowired
    public AwsDocsResource(GetUserDocs getUserDocs) {
        this.getUserDocs = getUserDocs;
    }

    @GetMapping
    public ResponseEntity<List<DocumentMetadata>> getUserDocuments(){
        try {
            return ResponseEntity.ok(getUserDocs.getUserDocuments().stream().map(DocumentMetadata::fromAwsDocumentMetadata).collect(Collectors.toList()));
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
