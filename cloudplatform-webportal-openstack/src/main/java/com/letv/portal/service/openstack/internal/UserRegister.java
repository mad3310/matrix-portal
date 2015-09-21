package com.letv.portal.service.openstack.internal;

import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import com.letv.portal.service.openstack.exception.OpenStackException;
import com.letv.portal.service.openstack.util.Contants;
import com.letv.portal.service.openstack.util.Params;
import com.letv.portal.service.openstack.util.Util;

@SuppressWarnings("deprecation")
public class UserRegister {

//    private static final Logger logger = LoggerFactory
//            .getLogger(UserRegister.class);

    private final String endpoint;
    private final String userName;
    private final String password;
    private final String email;
    private final String registerToken;

    public UserRegister(String adminEndpoint, String userName, String password, String email, String registerToken) {
        this.endpoint = adminEndpoint;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.registerToken = registerToken;
    }

    @SuppressWarnings("resource")
    public void run() throws OpenStackException {
        HttpClient client = new DefaultHttpClient();
        try {
            HttpPost req = new HttpPost(this.endpoint
                    + "OS-KSREG/register/project/user");

            req.addHeader("User-Agent", Contants.OPEN_STACK_USER_AGENT);
            req.addHeader("Content-Type", "application/json");
            req.addHeader("Accept", "application/json");
            req.addHeader("X-Auth-Token", this.registerToken);

            Params body = new Params();
            Params bodyUser = new Params();
            body.p("user", bodyUser);
            bodyUser.p("enabled", true).p("name", this.userName)
                    .p("password", password).p("description", "matrix "+this.userName).p("email",this.email);
            Params bodyTenant = new Params();
            body.p("tenant", bodyTenant);
            bodyTenant.p("enabled", true).p("name", this.userName)
                    .p("description", "matrix " + this.userName);

            ObjectMapper objectMapper = new ObjectMapper();
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                    .createJsonGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator, body);
            req.setEntity(new StringEntity(stringWriter.toString(), HTTP.UTF_8));

            HttpResponse resp = client.execute(req);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                Util.throwExceptionOfResponse(resp);
            }
        } catch (OpenStackException ose) {
            throw ose;
        } catch (Exception ex) {
            throw new OpenStackException("后台服务异常",ex);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
