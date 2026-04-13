package com.kcnf.ai.controller;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.action.InterruptionMetadata;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AgentController {

    @Autowired
    private ReactAgent reactAgent;

    @GetMapping("/invoke")
    @ResponseBody
    public List<InterruptionMetadata.ToolFeedback> invoke(@RequestParam("query") String query,
                                                          @RequestParam("threadId") String threadId) throws Exception {
        RunnableConfig runnableConfig = RunnableConfig.builder().threadId(threadId).build();
        InterruptionMetadata metadata = (InterruptionMetadata) reactAgent.invokeAndGetOutput(query, runnableConfig).orElseThrow();

        return metadata.toolFeedbacks();
    }

}