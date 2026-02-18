package com.eai.user.utilities;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class RequestUtilies {

  private static final String IP_ADDRESS_HEADER = "X-FORWARDED-FOR";
  private static final String USER_AGENT = "user-agent";

  public static String getIpAddress(HttpServletRequest request) {
    String iPaddress = "Unknown Ip Address";
    if (request != null) {
      iPaddress = request.getHeader(IP_ADDRESS_HEADER);
      if (iPaddress == null || "".equals(iPaddress)) {
        iPaddress = request.getRemoteAddr();
      }
    }
    return iPaddress;
  }

  public static String getDevice(HttpServletRequest request) {
   UserAgentAnalyzer agentAnalyzer = UserAgentAnalyzer.newBuilder()
                 .hideMatcherLoadStats()
                 .withCache(1000)
                 .build();
     
     UserAgent agent = agentAnalyzer.parse(request.getHeader(USER_AGENT));           
    // System.out.println(agent);
     return agent.getValue(UserAgent.OPERATING_SYSTEM_NAME) +" - "+agent.getValue(UserAgent.AGENT_NAME) +" - "+ agent.getValue(UserAgent.DEVICE_NAME);
  }
}
