package com.fjsh.dag.flow.common;

import lombok.Getter;

/**
 */
public enum StatusCode {
    SUCCESS(100),
    EMPTY_SUCCESS(110),
    PART_SUCCESS(200),
    FAILURE(300),
    EXCEPTION(310),
    IO_TIMEOUT(320),
    PARAMETERS_ILLEGAL(330), PARAMETERS_EMPTY(331),
    FALLBACK(350),
    UNKOWN(999);
    
    @Getter
    int code;
    
    StatusCode(int code){
        this.code =code;
    }
    
}
