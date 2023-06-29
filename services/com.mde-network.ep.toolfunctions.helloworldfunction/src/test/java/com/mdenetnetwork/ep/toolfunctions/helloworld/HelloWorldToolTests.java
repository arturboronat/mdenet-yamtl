package com.mdenetnetwork.ep.toolfunctions.helloworld;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import com.mdenetnetwork.ep.toolfunctions.helloworld.HelloWorldTool;

public class HelloWorldToolTests {
	
	@Test
	void testOuputIsGreetingWithInputParameterAppended() throws Exception{
		
		//Prepare inputs
		final String parameter1 = "P1 input";
		final String expectedOutput="Hello world, the input was: \n" + parameter1;
		
		JsonObject response = new JsonObject();
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();

		HelloWorldTool tool = new HelloWorldTool(); 
		
		// Run the tool function
		tool.run(parameter1, outstream, response);
		
		// Check the result
		assertEquals( expectedOutput, outstream.toString() );
	}	
}