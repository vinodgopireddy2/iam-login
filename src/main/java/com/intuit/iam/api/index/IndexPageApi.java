package com.intuit.iam.api.index;

import com.intuit.iam.model.requestbody.LoginRequestBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("")
public class IndexPageApi {

    private static final Logger logger = LogManager.getLogger(com.intuit.iam.api.login.LoginApiService.class);

    @GET
    @Path("home")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_HTML)
    public Response login(LoginRequestBody body, @Context HttpServletRequest req, @Context HttpServletResponse res) {
        logger.debug("Home page visited by clientIp: {}", req.getRemoteAddr());
    String indexHtml =  "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "  <link href=\"https://unpkg.com/material-components-web@latest/dist/material-components-web.min.css\" rel=\"stylesheet\">\n" +
            "  <script src=\"https://unpkg.com/material-components-web@latest/dist/material-components-web.min.js\"></script>\n" +
            "  <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/icon?family=Material+Icons\">\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "<img src=\"https://upload.wikimedia.org/wikipedia/commons/thumb/a/ae/Intuit_Logo.svg/1200px-Intuit_Logo.svg.png\" alt=\"Intuit Inc.\" width=\"123\" height=\"40\">\n" +
            "<div style=\"color: #ffffff;display: inline-block;background: #365bef;padding: 6px;vertical-align: super;width: 1450px;\">\n" +
            "<h2 style=\"display:inline;margin-left: 350px;\">Welcome to Intuit's Identity and Access management home page</h2>\n" +
            "</div>\n" +
            "<span style=\"display: block;\">\n" +
            "<button style=\"margin-top: -30px;margin-left: 128px; background-color:#365bef\" class=\"mdc-button mdc-button--raised\" onclick=\"openDocumentation()\">\n" +
            "  <div class=\"mdc-button__ripple\"></div>\n" +
            "  <i class=\"material-icons mdc-button__icon\" aria-hidden=\"true\"\n" +
            "    >bookmark</i\n" +
            "  >\n" +
            "  <span class=\"mdc-button__label\" style=\"font-weight: bold;\">REST API Documentation</span>\n" +
            "</button>\n" +
            "<a href=\"https://github.com/vinodgopireddy2/iam-login\" target=\"_blank\">\n" +
            "<img src=\"https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png\" style=\"width: 50px; vertical-align: sub;\"/>\n" +
            "</a>\n" +
            "</span>\n" +
            "<span style=\"display: block\">\n" +
            "<button id=\"serv-arch\" style=\"margin-top: 15px;margin-left: 128px; background-color:#365bef\" class=\"mdc-button mdc-button--raised\" onclick=\"onServiceArchClicked()\">\n" +
            "  <div class=\"mdc-button__ripple\"></div>\n" +
            "  <i class=\"material-icons mdc-button__icon\" aria-hidden=\"true\"\n" +
            "    >bookmark</i\n" +
            "  >\n" +
            "  <span class=\"mdc-button__label\" style=\"font-weight: bold;\">IAM Service Architecture</span>\n" +
            "</button>\n" +
            "<button id=\"audit-metrics\" style=\"margin-top: 15px;margin-left: 128px; color:#365bef\" class=\"mdc-button mdc-button--outlined\" onclick=\"onAuditMetricsClicked()\">\n" +
            "  <div class=\"mdc-button__ripple\"></div>\n" +
            "  <i class=\"material-icons mdc-button__icon\" aria-hidden=\"true\"\n" +
            "    >bookmark</i\n" +
            "  >\n" +
            "  <span class=\"mdc-button__label\" style=\"font-weight: bold;\">AUDIT METRICS</span>\n" +
            "</button>\n" +
            "</span>\n" +
            "<iframe id=\"serv-arch-iframe\" src=\"https://drive.google.com/file/d/1NEWZgg6n7YA8JUJQUUZyvCUB9NSPdlgt/preview\"  width=\"800\" height=\"600\" style=\"\n" +
            "    transform: rotate(270deg);\n" +
            "    height: 1200px;\n" +
            "    margin-left: 325px;\n" +
            "    margin-top: -175px;\n" +
            "    margin-bottom: -150px;\n" +
            "    border: none;\n" +
            "\"></iframe>\n" +
            "<iframe id=\"audit-metrics-iframe\" src=\"https://www.identity-intuitinc.com/goto/d44e25899b0e80a2c8511868a6f31556\" height=\"1100\" width=\"1400\" style=\"\n" +
            "height: 1100px;\n" +
            "margin-left: 120px;\n" +
            "margin-bottom: -150px;\n" +
            "margin-top: 15px;\n" +
            "border: none;\n" +
            "display: none;\n" +
            "\"></iframe>\n" +
            "<script>\n" +
            "function openDocumentation() {\n" +
            "  window.open(\"https://documenter.getpostman.com/view/1363980/Szzj7cs9?version=latest#dfaa1474-aeaa-f9db-bb61-e9df65d44498\", \"_blank\");\n" +
            "}\n" +
            "function onServiceArchClicked() {\n" +
            "  document.getElementById(\"serv-arch\").classList.add(\"mdc-button--raised\");\n" +
            "  document.getElementById(\"serv-arch\").classList.remove(\"mdc-button--outlined\");\n" +
            "  document.getElementById(\"audit-metrics\").classList.add(\"mdc-button--outlined\");\n" +
            "  document.getElementById(\"audit-metrics\").classList.remove(\"mdc-button--raised\");\n" +
            "  document.getElementById(\"audit-metrics-iframe\").style.display = 'none';\n" +
            "  document.getElementById(\"serv-arch-iframe\").style.display = 'block';\n" +
            "  document.getElementById(\"serv-arch\").style.color = \"#ffffff\";\n" +
            "  document.getElementById(\"serv-arch\").style.backgroundColor = \"#365bef\";\n" +
            "  document.getElementById(\"audit-metrics\").style.backgroundColor = \"\"; // doubt\n" +
            "  document.getElementById(\"audit-metrics\").style.color = \"#365bef\";\n" +
            "}\n" +
            "function onAuditMetricsClicked() {\n" +
            "  document.getElementById(\"audit-metrics\").classList.add(\"mdc-button--raised\");\n" +
            "  document.getElementById(\"audit-metrics\").classList.remove(\"mdc-button--outlined\");\n" +
            "  document.getElementById(\"serv-arch\").classList.add(\"mdc-button--outlined\");\n" +
            "  document.getElementById(\"serv-arch\").classList.remove(\"mdc-button--raised\");\n" +
            "  document.getElementById(\"serv-arch-iframe\").style.display = 'none';\n" +
            "  document.getElementById(\"audit-metrics-iframe\").style.display = 'block';\n" +
            "  document.getElementById(\"audit-metrics\").style.color = \"#ffffff\";\n" +
            "  document.getElementById(\"audit-metrics\").style.backgroundColor = \"#365bef\";\n" +
            "  document.getElementById(\"serv-arch\").style.backgroundColor = \"\"; // doubt\n" +
            "  document.getElementById(\"serv-arch\").style.color = \"#365bef\";\n" +
            "}\n" +
            "</script>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";
    return Response.status(Response.Status.OK)
            .type(MediaType.TEXT_HTML).entity(indexHtml).build();
    }
}