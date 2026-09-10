package com.mmed.ws.agent;

import com.mmed.ws.repository.CheckRepository;
import com.mmed.ws.tool.HistoryToolDefault;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReportAgentDefault implements ReportAgent {

    private final CheckRepository repository;
    private final ChatClient chatClient;
    private static final String SYSTEM_MESSAGE = "You will asked to give a report about the health of a service "
        + "based on given data. That data represent the result of the last 20 or less checking time of the service if it is up or no.\n"
            +   "Generate a report saying what this result shows and what should do at the end";

    public ReportAgentDefault(CheckRepository repository, ChatClient.Builder chatClient) {
        this.repository = repository;
        this.chatClient = chatClient.build();
    }


    @Override
    public String getReport(UUID serviceId) {
        return chatClient
                .prompt()
                .system(SYSTEM_MESSAGE)
                .user("service id: " + serviceId.toString())
                .tools(new HistoryToolDefault(repository))
                .call()
                .content();
    }
}
