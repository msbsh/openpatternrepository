package nl.rug.search.opr;

/**
 *
 * @author daniel
 */
public interface ConfigConstants {
    public final String UPLOAD_SUPPORTED_MIME_TYPES = "supported-mime-types.type";
    public final String UPLOAD_MAX_SIZE_MB = "max-file-size-mb";
    public final String QUERY_GET_TAG_BY_NAME = "getTagByName";
    public final String QUERY_GET_SIMILAR_TAGS = "getSimilarTags";
    public final String QUERY_GET_USED_TAGS = "getUsedTags";
    public final String QUERY_GET_NON_ZERO_TAGS = "getNonZeroTags";
    public final String QUERY_GET_FILE_BY_NAME = "getFileByName";
    public final String QUERY_GET_ROOT_CATEGORY = "rootCategory";
    public final String QUERY_GET_CHILDREN_OF = "getChildrenOf";
    public final String QUERY_GET_BY_NAME = "getByName";
    public final String QUERY_GET_TEMPLATE_BY_NAME = "getTemplateByName";
    public final String QUERY_GET_SORTED_COMPONENTS = "sortedComponents";
    public final String QUERY_GET_SIMILAR = "getSimilar";
    public final String QUERY_GET_BY_UNIQUE_NAME = "selectByUniqueName";
    public final String QUERY_HAS_REFERENCES = "hasReference";
    public final String AUTO_REBUILD_INDEX = "opr.AutoRebuildIndex";
    public final String AUTO_RESTORE_INDEX = "opr.AutoRestoreIndex";
    public final String SEARCH_FACTORY_NAME = "opr.SearchFactory";
    public final String INDEX_TIMEOUT_MS = "opr.IndexIntervalMS";
}
