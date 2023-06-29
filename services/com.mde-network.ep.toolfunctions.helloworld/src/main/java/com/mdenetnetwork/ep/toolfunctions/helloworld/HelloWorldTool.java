package com.mdenetnetwork.ep.toolfunctions.helloworld;


import java.io.OutputStream;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;


import com.google.gson.JsonObject;

public class HelloWorldTool  { 
	
	public HelloWorldTool() {
		
		// Register resource factories
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	}


	public void run(String input, OutputStream outputStream, JsonObject response) throws Exception {
	
		String result = "";

		/*-------------------------------------
		 *  Metamodel 
		 *-------------------------------------*/
		
		// TODO load metamodel


		/*-------------------------------------
		 *  Model 
		 *-------------------------------------*/

		// TODO load model

	
		/*-------------------------------------
		 *  Tool Function 
		 *-------------------------------------*/	
		
		 // TODO run tool function


		result = result.concat("Hello world, the input was: \n");  
		result = result.concat(input);
		
		 outputStream.write(result.getBytes());
	}

	
	public void convertXToY(String X,  OutputStream outputStream, JsonObject response) throws Exception {
		
		final String emfaticFilename = "emfatic.emfatic";

		String generatedY = "";
		
		/*-------------------------------------
		 *  Load X 
		 *-------------------------------------*/	
		
	

		/*-------------------------------------
		 *  Generate Y
		 *-------------------------------------*/		

		
		 response.addProperty("output",  generatedY );
	}
	
}
	
