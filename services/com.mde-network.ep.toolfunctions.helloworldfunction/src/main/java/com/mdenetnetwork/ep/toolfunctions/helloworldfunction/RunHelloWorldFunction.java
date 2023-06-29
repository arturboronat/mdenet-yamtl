package com.mdenetnetwork.ep.toolfunctions.helloworldfunction;

import java.io.ByteArrayOutputStream;

import com.google.gson.JsonObject;
import com.mdenetnetwork.ep.toolfunctions.core.MdeNetToolFunction;

import com.mdenetnetwork.ep.toolfunctions.helloworld.HelloWorldTool;


public class RunHelloWorldFunction extends MdeNetToolFunction {

	@Override
	public void serviceImpl(JsonObject request, JsonObject response) throws Exception {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		new HelloWorldTool().
			run(	request.get("input").getAsString() ,
					bos, response);
			
		response.addProperty("output", bos.toString());
	}
	
// mvn function:run -Drun.functionTarget=com.mdenetnetwork.ep.toolfunctions.helloworldfunction.RunHelloWorldFunction -Drun.port=9090
 
}
