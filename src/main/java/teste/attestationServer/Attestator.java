package teste.attestationServer;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import sun.net.www.http.HttpClient;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class Attestator {

    // create request to proxy endpoint
    // ask for redis version + enclave hash
    // ???
    public Attestator(){

    }

    public void get(String uri) throws Exception {
        Client client = ClientBuilder.newBuilder()
                .hostnameVerifier(new InsecureHostnameVerifier())
                .build();

        URI baseURI = UriBuilder.fromUri("https://localhost:8080/").build();
        WebTarget target = client.target(baseURI);
    }


    static public class InsecureHostnameVerifier implements HostnameVerifier {
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    }
}
