package nl.bos;

import com.cordys.documentstore.*;
import com.cordys.documentstore.exceptions.DocumentStoreException;
import com.cordys.documentstore.exceptions.DocumentStoreRuntimeException;
import com.cordys.documentstore.exceptions.DocumentstoreInitializationException;
import com.cordys.documentstore.messages.OTCSMessages;
import com.cordys.documentstore.otcs.*;
import com.cordys.documentstore.search.GetNodesByPropertiesRequestInfo;
import com.cordys.documentstore.search.GetNodesByPropertiesResponseInfo;
import com.cordys.documentstore.search.SearchRequestInfo;
import com.cordys.documentstore.search.SearchResponseInfo;
import com.eibus.util.logger.CordysLogger;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OthersDocumentStoreHandler implements IDocumentStore {
    private static final CordysLogger LOGGER = CordysLogger.getCordysLogger(OthersDocumentStoreHandler.class);
    protected boolean isStoreInitialized = false;

    @Override
    public boolean initializeStore(StoreConfiguration storeConfiguration) throws DocumentStoreException, DocumentstoreInitializationException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("INTO initializeStore: classPath=%s; implClass=%s; extConfigXML=%s", storeConfiguration.getClassPath(), storeConfiguration.getImplementationClass(), storeConfiguration.getExtConfigXML()));
        }

        if (this.isStoreInitialized) {
            throw new DocumentStoreRuntimeException(OTCSMessages.STORE_WAS_ALREADY_INITIALIZED);
        } else {
            this.isStoreInitialized = true;
            return true;
        }
    }

    @Override
    public String createDocument(int i) throws DocumentStoreException {
        return null;
    }

    @Override
    public String createDocument(Document document) throws DocumentStoreException {
        return null;
    }

    @Override
    public CreateDocumentResponseInfo createDocument(CreateDocumentRequestInfo createDocumentRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public Map<String, OperationResult> deleteNodes(Set<String> set) throws DocumentStoreException {
        return null;
    }

    @Override
    public Map<String, String> createFolders(CreateFoldersRequestInfo createFoldersRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public CopyNodeResponseInfo copyNode(CopyNodeRequestInfo copyNodeRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public boolean deleteDocument(String s) throws DocumentStoreException {
        return false;
    }

    @Override
    public boolean deleteDocument(DeleteDocumentRequestInfo deleteDocumentRequestInfo) throws DocumentStoreException {
        return false;
    }

    @Override
    public boolean isNodeAvailable(String s) throws DocumentStoreException {
        return false;
    }

    @Override
    public Document[] getDocumentsInfo(String s) throws DocumentStoreException {
        return new Document[0];
    }

    @Override
    public Document getDocument(String s) throws DocumentStoreException {
        return null;
    }

    @Override
    public Document getDocument(NodeRequestInfo nodeRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public List<Document> listNodesByParent(String s) throws DocumentStoreException {
        return null;
    }

    @Override
    public List<Document> listNodesByParent(ListNodesByParentRequestInfo listNodesByParentRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public String updateDocument(Document document) throws DocumentStoreException {
        return null;
    }

    @Override
    public String updateDocument(int i) throws DocumentStoreException {
        return null;
    }

    @Override
    public OperationResult deleteParent(String s) {
        return null;
    }

    @Override
    public Map<String, OperationResult> deleteParents(Set<String> set) {
        return null;
    }

    @Override
    public UpdatePropertiesResponse updateProperties(UpdatePropertiesRequest updatePropertiesRequest) throws DocumentStoreException {
        return null;
    }

    @Override
    public CheckInResponseInfo checkin(CheckInRequestInfo checkInRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public CheckOutResponseInfo checkout(CheckOutRequestInfo checkOutRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public ReleaseLockResponseInfo releaseLock(ReleaseLockRequestInfo releaseLockRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public GetCategoriesResponseInfo getCategories(GetCategoriesRequestInfo getCategoriesRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public ListCategoriesResponseInfo listCategories(ListCategoriesRequestInfo listCategoriesRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public GetVersionHistoryResponseInfo getVersionHistory(GetVersionHistoryRequestInfo getVersionHistoryRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public SearchResponseInfo search(SearchRequestInfo searchRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public GetAuditRecordsForNodesResponseInfo getAuditRecordsForNodes(GetAuditRecordsForNodesRequestInfo getAuditRecordsForNodesRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public CreateCategoryResponseInfo createCategory(CreateCategoryRequestInfo createCategoryRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public AssociateCategoriesToNodeResponseInfo associateCategoriesToNode(AssociateCategoriesToNodeRequestInfo associateCategoriesToNodeRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public GetCategoriesAssociatedWithNodeResponseInfo getCategoriesAssociatedWithNode(GetCategoriesAssociatedWithNodeRequestInfo getCategoriesAssociatedWithNodeRequestInfo) throws DocumentStoreException {
        return null;
    }

    @Override
    public GetNodesByPropertiesResponseInfo getNodesByProperties(GetNodesByPropertiesRequestInfo getNodesByPropertiesRequestInfo) throws DocumentStoreException {
        return null;
    }
}
