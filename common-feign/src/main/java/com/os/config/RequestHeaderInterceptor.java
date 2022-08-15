package com.os.config;

import com.os.Indempotency.IdempotencyConstant;
import com.os.tenant.TenantConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class RequestHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            // định danh cho các request gửi đi 1 Token với giá trị = UUID.randomUUID.toString();
            String idempotencyToken = UUID.randomUUID().toString();

            template.header(IdempotencyConstant.PARAMS_REQ_TOKEN_NAME, idempotencyToken);
            Field field = template.getClass().getDeclaredField("uriTemplate");
            field.setAccessible(true);
            String uriTemplate = field.get(template).toString();

            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if(!Objects.isNull(requestAttributes)){
                HttpServletRequest request = requestAttributes.getRequest();

                // Todo gán giá trị của header lang vào template.

                String requestUri = request.getRequestURI();
                log.info("=====> request uri: {}", requestUri);
                String permissionTag = "/permission/"; // đường dẫn gán quyền.
                // nếu không phải một đường dẫn gán quyền, thì gán lại header tenantId.
                if(!requestUri.startsWith(permissionTag)){
                    String tenantId = request.getParameter("tenantId");
                    template.query("tenantId", tenantId);
                    ThreadLocal<String> threadLocal = TenantConstants.TENANT_THREAD_LOCAL;
                    threadLocal.set(tenantId);
                    if(log.isDebugEnabled())
                        log.debug("=====> tenantId: {}", tenantId);
                }
            }

        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
    }
}
