package com.mmed.ws.agent;

import com.mmed.ws.repository.CheckRepository;
import com.mmed.ws.tool.HistoryToolDefault;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ReportAgentDefault implements ReportAgent {

    private final ChatClient chatClient;
    private static final String SYSTEM_MESSAGE = "You will asked to give a report about the health of a service "
        + "based on given data. That data represent the result of the last 20 or less checking time of the service if it is up or no.\n"
            +   "Generate a report saying what this result shows and what should do at the end";

    private static final String chatId = "spring_ai_agent_chat_memory_id";
    private static final int MAX_MESSAGES = 8;

    public ReportAgentDefault(CheckRepository repository, ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem(SYSTEM_MESSAGE)
                .defaultTools(new HistoryToolDefault(repository))
                .defaultAdvisors(
                        MessageChatMemoryAdvisor
                                .builder(
                                        MessageWindowChatMemory
                                        .builder()
                                                .maxMessages(MAX_MESSAGES)
                                        .build()
                                )
                                .build()
                ).build();
    }


    @Override
    public String getReport(UUID serviceId) {
        return chatClient
                .prompt()
                .user("service id: " + serviceId.toString())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }
}
