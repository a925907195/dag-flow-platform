package com.fjsh.dag.flow.common;


import com.google.common.collect.Maps;

import com.fjsh.dag.flow.model.ItemKey;

import org.apache.commons.collections.MapUtils;

import java.util.Collections;
import java.util.Map;

import lombok.Getter;

/**
 *
 */
public class ResultStatus {

    @Getter
    private final StatusCode code;

    @Getter
    private final Map<ItemKey, StatusCode> statusMap;

    public ResultStatus(StatusCode c) {
        this(c, Maps.<ItemKey, StatusCode>newHashMap());
    }

    public ResultStatus(StatusCode c, Map<ItemKey, StatusCode> map) {
        this.code = c;
        this.statusMap = map;
    }

    public boolean isSetStatusMap() {
        return MapUtils.isNotEmpty(statusMap) ? true : false;
    }

    public boolean isSuc() {
        return isSuc(code);
    }

    public static boolean isSuc(StatusCode cd) {
        return (cd == StatusCode.SUCCESS || cd == StatusCode.EMPTY_SUCCESS) ? true : false;
    }

    public static final ResultStatus SUCCESS = new ResultStatus(StatusCode.SUCCESS);
    public static final ResultStatus PART_SUCCESS = new ResultStatus(StatusCode.PART_SUCCESS);
    public static final ResultStatus EMPTY_SUCCESS = new ResultStatus(StatusCode.EMPTY_SUCCESS);
    public static final ResultStatus FAILURE = new ResultStatus(StatusCode.FAILURE);
    public static final ResultStatus EXCEPTION = new ResultStatus(StatusCode.EXCEPTION);
    public static final ResultStatus IO_TIMEOUT = new ResultStatus(StatusCode.IO_TIMEOUT);
    public static final ResultStatus PARAMETERS_ILLEGAL = new ResultStatus(StatusCode.PARAMETERS_ILLEGAL);
    public static final ResultStatus PARAMETERS_EMPTY = new ResultStatus(StatusCode.PARAMETERS_EMPTY);

    public static final ResultStatus UNKOWN = new ResultStatus(StatusCode.UNKOWN);
    public static final ResultStatus FALLBACK = new ResultStatus(StatusCode.FALLBACK);
}
