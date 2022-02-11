package com.gwhn.elasticsearch.exception;

import java.io.IOException;

/**
 * @author banxian
 * @date 2022/2/10 16:57
 */
public class ESIoException extends IOException {

    public ESIoException(String message,Throwable throwable){
        super(message,throwable);
    }

    public ESIoException(String message){
        super(message);
    }

    public ESIoException(Throwable throwable){
        super(throwable);
    }
}
