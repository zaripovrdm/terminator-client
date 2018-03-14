This is simple replication routine.  Main idea - get client from one data source and after some modification write it to three another ones. In addition there is need to send messages about successful replication in JMS topic. (see ClientFlow::clientIntegrationFlow)

Application runs as war inside servlet container and has no web interface.

Based on Spring Integration.

Entry point - ru.raiffeisen.terminator.client.Main

Unfortunately this project has dependencies from my current company internal repository and you cannot compile and run it.