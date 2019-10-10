package se.sbab.awsdocsservice;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.workdocs.WorkDocsClient;
import software.amazon.awssdk.services.workdocs.model.DescribeFolderContentsRequest;
import software.amazon.awssdk.services.workdocs.model.DescribeFolderContentsResponse;
import software.amazon.awssdk.services.workdocs.model.DescribeUsersRequest;
import software.amazon.awssdk.services.workdocs.model.DescribeUsersResponse;
import software.amazon.awssdk.services.workdocs.model.DocumentMetadata;
import software.amazon.awssdk.services.workdocs.model.DocumentVersionMetadata;
import software.amazon.awssdk.services.workdocs.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetUserDocs {

    private static String get_user_folder(WorkDocsClient workDocs, String orgId, String user) throws Exception {
        List<User> wdUsers = new ArrayList<>();


        String marker = null;

        do {
            DescribeUsersResponse result;

            if(marker == null) {
                DescribeUsersRequest request = DescribeUsersRequest.builder()
                        .organizationId(orgId)
                        .query(user)
                        .build();
                result = workDocs.describeUsers(request);
            }
            else {
                DescribeUsersRequest request = DescribeUsersRequest.builder()
                        .organizationId(orgId)
                        .query(user)
                        .marker(marker)
                        .build();
                result = workDocs.describeUsers(request);
            }

            wdUsers.addAll(result.users());
            marker = result.marker();
        } while (marker != null);

        for (User wdUser : wdUsers) {
            return wdUser.rootFolderId();
        }

        return "";
    }

    public List<DocumentMetadata> getUserDocuments() throws Exception {
        // Based on WorkDocs dev guide code at http://docs.aws.amazon.com/workdocs/latest/developerguide/connect-workdocs-role.html


        String orgId = "d-936712ec6b";
        String userEmail = "mohitgrover.prof@gmail.com";

        // Use the default client. Look at Window, Preferences, AWS Toolkit to see the values
        WorkDocsClient workDocs = WorkDocsClient.create();

        String folderId = get_user_folder(workDocs, orgId, userEmail);

        DescribeFolderContentsRequest dfc_request = DescribeFolderContentsRequest.builder().folderId(folderId).build();

        DescribeFolderContentsResponse result = workDocs.describeFolderContents(dfc_request);

        List<DocumentMetadata> userDocs = new ArrayList<>();

        userDocs.addAll(result.documents());

        System.out.println("Docs for user " + userEmail + ":");
        System.out.println("");

        for (DocumentMetadata doc: userDocs) {
            DocumentVersionMetadata md = doc.latestVersionMetadata();
            System.out.println("Name:          " + md.name());
            System.out.println("Size (bytes):  " + md.size());
            System.out.println("Last modified: " + md.modifiedTimestamp());
            System.out.println("Doc ID:        " + doc.id());
            System.out.println("");
        }
        return userDocs;
    }
}
