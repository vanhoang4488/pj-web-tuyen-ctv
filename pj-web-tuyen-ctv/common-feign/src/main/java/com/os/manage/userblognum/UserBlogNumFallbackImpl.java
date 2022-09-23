package com.os.manage.userblognum;

import com.google.gson.Gson;
import com.os.result.ResultEntity;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
@RequiredArgsConstructor
public class UserBlogNumFallbackImpl implements UserBlogNumService {

    private final Gson gson;

    @Override
    public Response downloadUserBlogExcel() {
        String content = gson.toJson(ResultEntity.failed().message("common.request.timeout").build());
        return Response.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(content, Charset.forName("UTF-8"))
                .build();
    }
}
