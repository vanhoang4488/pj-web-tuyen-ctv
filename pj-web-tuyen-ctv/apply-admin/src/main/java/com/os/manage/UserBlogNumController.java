package com.os.manage;

import com.google.gson.Gson;
import com.os.manage.userblognum.UserBlogNumService;
import com.os.result.ResultEntity;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@RequestMapping("/sys/log")
@RequiredArgsConstructor
public class UserBlogNumController {

    private final Gson gson;

    private final UserBlogNumService sysLogService;

    @RequestMapping("/downloadUserBlogExcel")
    public void downloadUserBlogExcel(HttpServletResponse response) throws IOException {
        Response feignResponse = sysLogService.downloadUserBlogExcel();
        int status = feignResponse.status();
        if(status == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            response.getWriter().write(gson.toJson(ResultEntity.failed().message("admin.userblognum.export.excel.fail").build()));
        }
        else{
            response.setContentType("application/vnd.ms-excel; charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=userblognum-list-"+System.currentTimeMillis()+".xlsx");
            Response.Body body = feignResponse.body();
            InputStream inputStream = body.asInputStream();
            OutputStream outputStream = response.getOutputStream();;
            byte[] bytes = new byte[1024];
            int len;
            while((len = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
            }
            inputStream.close();
            outputStream.close();
            outputStream.flush();
        }
    }
}
