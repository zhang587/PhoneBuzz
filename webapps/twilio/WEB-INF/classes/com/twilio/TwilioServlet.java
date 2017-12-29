import com.twilio.twiml.*;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
package com.twilio;
//////////////////////////////////////////////////////////////////
//Project name: PhoneBuzz
//Author: Yingshi Zhang
//Note: In this project, I used ngrok for testing my local server
///////////////////////////////////////////////////////////////////

public class TwilioServlet extends HttpServlet {
    // Replace with your Account Sid and Token
    public static final String ACCOUNT_SID = "AC6714327317ca312211ac3a0f777a9fce";
    public static final String AUTH_TOKEN = "02253a4e6bd9526860193e7441cf1cdb";

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
       phase_2(request, response);
    }

    //get the correct sequence
    private String buzz_res (int num) {
        String res = "";
        for (int i = 1; i <= num; i++) {
            if (i%3 == 0) {
                res = res + "Fizz";
            }
            else if (i%5 == 0) {
                res = res + "Buzz";
            }
            else if (i%3 == 0 && i%5 == 0) {
                res = res + "Fizz Buzz";
            }
            else {
                return "i";
            }
        }
        return res;

    }

   @Override
    public void phase_2(HttpServletRequest request, HttpServletResponse response) throws IOException {
     
    VoiceResponse.Builder builder = new VoiceResponse.Builder();

    //phase 2
    if (request.getParameter("From") != null) { //if the sender is unknown
         TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
         PhoneNumber knownCaller = new PhoneNumber(request.getParameter("PhoneNumber"));
         PhoneNumber fromNumber = new PhoneNumber("+1 920-626-8293"); // Replace with your Twilio number
         //Replace this url with registered Twilio number
         URI uri = URI.create("http://2e35b6ca.ngrok.io/twilio/twiml"); 
    }
    String digits = request.getParameter("Digits");
    if (digits == null) {
        appendGather(builder);
    } else {
        int number = Integer.parseInt(digits);
        if (number >= 1) {
            String result = this.buzz_res(number);
            builder.say(new Say.Builder(result).build()); 
        } else {
            appendGather(builder);
        }

    }

    response.setContentType("application/xml");
    try {
        response.getWriter().print(builder.build().toXml());
    } catch (TwiMLException e) {
        throw new RuntimeException(e);
    }
    }

    //Handles the case where the user doesn't enter any input after a configurable timeout
    private static void appendGather(VoiceResponse.Builder builder) {
        builder.gather(new Gather.Builder()
                .timeout(5)
                .say(new Say.Builder("Please enter a digit.").build())
                .build()
        )
                .redirect(new Redirect.Builder().url("/twiml").build());
    }

}