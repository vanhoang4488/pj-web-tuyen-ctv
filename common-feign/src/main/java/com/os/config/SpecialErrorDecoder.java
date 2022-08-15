package com.os.config;

import com.google.gson.Gson;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
public class SpecialErrorDecoder implements ErrorDecoder {

    @Autowired
    private Gson gson;

    @Override
    public Exception decode(String methodKey, Response response) {
        Exception exception = null;
        log.error("=====> methodKey: {}", methodKey);
        try{
            InputStream in = response.body().asInputStream();
            int estimate = in.available();
            byte[] b = new byte[estimate];
            int len = 0;
            while(len < estimate){
                len += in.read(b, len, estimate - len) ;
            }
            String message = new String(b);
            log.error("=======> message: {}", message);
//            Map<String, Object> result = gson.fromJson(message,
//                    new TypeToken<Map<String, String>>(){}.getType());
            /*
             * Vẫn chả hiểu thằng result này sinh ra để làm gì.
             * message không thể kiểm tra được hay sao mà phải dùng
             * result != null để kiểm tra xem có phản hồi trả về hay không.
             */
            if(StringUtils.isNotBlank(message))
                exception =  new RuntimeException(message);
            else
                log.error("no error message returned");
        }
        catch (Exception e){
            log.error("{}", e.getMessage(), e);
        }
        return exception;
    }
}
