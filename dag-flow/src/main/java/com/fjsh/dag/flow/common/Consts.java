package com.fjsh.dag.flow.common;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
public class Consts {
    public static final String JOB_KEY = "key";
    public static final String JOB_TYPE = "type";
    public static final String JOB_IGF = "inputGlobalFields";
    public static final String JOB_IIF = "inputItemFields";
    public static final String JOB_OGF = "outputGlobalFields";
    public static final String JOB_OIF = "outputItemFields";
    public static final String JOB_CLASS = "class";
    public static final String JOB_TIMEOUT = "timeout";

    public static final int JOB_TIMEOUT_DEFAULT_THRESHOLD = 100;

    public static final String JOB_LOADERS = "loaders";
    public static final String JOB_EXTRACTORS = "extractors";


    public static class LoaderConsts {
        public static final String LOADER_NAME = "name";
        public static final String FIELD_NAME_MAPPER = "FieldNameMapping";

    }

    public static class ExtractorConsts {
        public static final String EXT_ID = "id";
        public static final String EXT_NAME = "name";
        public static final String EXT_TARGET_GLOBAL = "global";
        public static final String EXT_TARGET_ITEM = "item";

        public static final String EXT_CLASS = "class";
        public static final String EXT_EL_VALUETYPE = "valueType";
        public static final String EXT_EL_EXPRESSION = "expression";


    }


    public enum UDSNameSpace {
        homepage,
        theme
    }


    public enum JobTypeEnum {
        CUSTOM, UDS, EXTRACTOR;


        public static Set<String> supportJobTypes() {
            return Arrays.stream(JobTypeEnum.values()).map(type -> type.name()).collect(Collectors.toSet());
        }
    }


}
