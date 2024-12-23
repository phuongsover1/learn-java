package com.packt.modern.api.config;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class H2ConsoleComponent {
    private Server webServer;

    @Value("${modern.api.h2.console.port}")
    Integer h2ConsolePort;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        this.webServer = org.h2.tools.Server
                .createWebServer("-webPort", h2ConsolePort.toString(), "-tcpAllowOthers").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.webServer.stop();
    }
}
