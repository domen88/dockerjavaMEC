package it.unibo.scotece.domenico;

import com.spotify.docker.client.DockerClient;
import it.unibo.scotece.domenico.services.impl.DockerConnectImpl;
import it.unibo.scotece.domenico.services.impl.ServerSocketSupportImpl;

import java.io.IOException;

import static spark.Spark.*;

public class Programma {

    public static void main(String[] args){

        //Current docker connector
        DockerConnectImpl currentDockerConnector =  new DockerConnectImpl();
        final StringBuffer address = new StringBuffer();

        //Set Java Spark server port
        port(8080);

        before((request, response) -> response.type("application/json"));

        get("/connect", "application/json", (req, res) -> {

            DockerClient docker = currentDockerConnector.setConnection();
            address.append("localhost");
            return "{\"message\":\"Connect OK\"}";

        });

        get("/socket", "application/json", (req, res) -> {

            ServerSocketSupportImpl socketSupport = new ServerSocketSupportImpl();

            Runnable socket = () -> {
                try {
                    socketSupport.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            Thread thread = new Thread(socket);
            thread.start();

            return "{\"message\":\"Socket Opened\"}";

        });

    }
}
