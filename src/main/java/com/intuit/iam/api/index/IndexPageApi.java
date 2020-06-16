package com.intuit.iam.api.index;

import com.intuit.iam.model.requestbody.LoginRequestBody;

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
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_HTML)
    public Response login(LoginRequestBody body, @Context HttpServletRequest req, @Context HttpServletResponse res) {
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
            "<button style=\"display:block;margin-top: 15px;margin-left: 128px; color:#365bef\" class=\"mdc-button mdc-button--outlined\">\n" +
            "  <div class=\"mdc-button__ripple\"></div>\n" +
            "  <span class=\"mdc-button__label\" style=\"font-weight: bold;\">IAM Service Architecture</span>\n" +
            "</button>\n" +
            "<iframe src=\"https://drive.google.com/file/d/1_52F63AqBnseyGe5xg9yuVIagAoY6muH/preview\"  width=\"800\" height=\"600\" style=\"\n" +
            "    transform: rotate(270deg);\n" +
            "    height: 1200px;\n" +
            "    margin-left: 325px;\n" +
            "    margin-top: -175px;\n" +
            "    margin-bottom: -150px;\n" +
            "    border: none;\n" +
            "\"></iframe>\n" +
            "\n" +
            "<script>\n" +
            "function openDocumentation() {\n" +
            "  window.open(\"https://documenter.getpostman.com/view/1363980/Szzj7cs9?version=latest#dfaa1474-aeaa-f9db-bb61-e9df65d44498\", \"_blank\");\n" +
            "}\n" +
            "</script>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";
    return Response.status(Response.Status.OK)
            .type(MediaType.TEXT_HTML).entity(indexHtml).build();
    }
}