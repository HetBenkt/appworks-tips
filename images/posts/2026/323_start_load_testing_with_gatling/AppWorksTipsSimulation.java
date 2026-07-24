package nl.bos;

import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.core.CoreDsl.xpath;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AppWorksTipsSimulation extends Simulation {

    private static final String OTDS_AUTHENTICATION_URL =
            "http://192.168.56.102:8181/otdsws/rest/authentication/credentials";
    private static final String OTDS_USERNAME = "opadev";
    private static final String OTDS_PASSWORD = "admin";
    private static final String OTDS_TARGET_RESOURCE_ID = "296c66f2-c3cc-4c59-918e-883dbd882c22";
    private static final String SOAP_GATEWAY_URL =
            "http://192.168.56.102:8080/home/opa_tips/com.eibus.web.soap.Gateway.wcp";
    private static final String CASE_ENTITY_URL =
            "http://192.168.56.102:8080/home/opa_tips/app/entityRestService/api/opa_tipsgeneric/entities/case";

    public AppWorksTipsSimulation() {
        HttpProtocolBuilder httpProtocol = http
                .baseUrl("https://appworks-tips.com")
                .acceptHeader("application/json")
                .contentTypeHeader("application/json");

        setUp(scenario("OPA Simulation")
                .exec(http("Open homepage")
                        .get("/")
                        .check(status().is(200)))
                .exec(http("Authenticate with OTDS credentials")
                        .post(OTDS_AUTHENTICATION_URL)
                        .body(StringBody("""
                                {
                                  "userName": "%s",
                                  "password": "%s",
                                  "targetResourceId": "%s"
                                }
                                """.formatted(OTDS_USERNAME, OTDS_PASSWORD, OTDS_TARGET_RESOURCE_ID)))
                        .asJson()
                        .check(
                                status().is(200),
                                jsonPath("$.ticket").saveAs("ticket")
                        ))
                .exec(session -> {
                    System.out.println("ticket: " + session.getString("ticket"));
                    return session;
                })
                .exec(http("Get SAML artifact")
                        .post(SOAP_GATEWAY_URL)
                        .requestTimeout(Duration.ofSeconds(30))
                        .header("Accept", "text/xml, application/xml")
                        .header("Content-Type", "text/xml; charset=UTF-8")
                        .body(StringBody("""
                                <SOAP:Envelope xmlns:SOAP="http://schemas.xmlsoap.org/soap/envelope/">
                                    <SOAP:Header>
                                        <OTAuthentication xmlns="urn:api.bpm.opentext.com">
                                            <AuthenticationToken>#{ticket}</AuthenticationToken>
                                        </OTAuthentication>
                                    </SOAP:Header>
                                    <SOAP:Body>
                                        <samlp:Request xmlns:samlp="urn:oasis:names:tc:SAML:1.0:protocol" MajorVersion="1" MinorVersion="1">
                                            <samlp:AuthenticationQuery>
                                                <saml:Subject xmlns:saml="urn:oasis:names:tc:SAML:1.0:assertion">
                                                    <saml:NameIdentifier Format="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"/>
                                                </saml:Subject>
                                            </samlp:AuthenticationQuery>
                                        </samlp:Request>
                                    </SOAP:Body>
                                </SOAP:Envelope>
                                """))
                        .check(
                                status().is(200),
                                xpath("//*[local-name()='AssertionArtifact']").saveAs("assertionArtifact")
                        ))
                .exec(session -> {
                    System.out.println("assertionArtifact: " + session.getString("assertionArtifact"));
                    return session;
                })
                .exec(http("Create case entity")
                        .post(CASE_ENTITY_URL)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .header("SAMLart", "#{assertionArtifact}")
                        .body(StringBody("""
                                {
                                  "Properties": {
                                    "case_name": "string"
                                  }
                                }
                                """))
                        .asJson()
                        .check(
                                status().is(201),
                                jsonPath("$.Identity.Id").saveAs("caseId")
                        ))
                .exec(session -> {
                    System.out.println("caseId: " + session.getString("caseId"));
                    return session;
                })
                .exec(http("Delete case entity")
                        .delete(CASE_ENTITY_URL + "/items/#{caseId}")
                        .header("Accept", "*/*")
                        .header("SAMLart", "#{assertionArtifact}")
                        .check(status().is(204)))
                .injectOpen(atOnceUsers(1))
        ).protocols(httpProtocol);
    }
}
