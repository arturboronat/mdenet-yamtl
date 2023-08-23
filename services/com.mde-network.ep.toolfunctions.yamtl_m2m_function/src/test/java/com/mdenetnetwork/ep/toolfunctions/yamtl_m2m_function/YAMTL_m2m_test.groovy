package com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function

import static org.junit.jupiter.api.Assertions.assertEquals

import org.junit.jupiter.api.Test

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function.RunYAMTL_m2m_groovy
import com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function.StringUtil

import groovy.json.JsonBuilder

class YAMTL_m2m_test {

    @Test
    void test_m2m_cd2db() throws Exception {

		def requestBuilder = new JsonBuilder()
		requestBuilder {
			trafoGroovy 	new File('./model/CD2DB.groovy').text
			inMetamodel 	new File('./model/CD.emf').text
			inModel 		new File('./model/sourceModel.xmi').text
			outMetamodel 	new File('./model/Relational.emf').text
		}
		Gson gson = new Gson()
		JsonObject request = gson.fromJson(requestBuilder.toString(), JsonObject.class)
		
        def response = new JsonObject()
		
		def yamtl_m2m_service = new RunYAMTL_m2m_groovy()
		yamtl_m2m_service.serviceImpl(request, response)
		
        // Check the result
		def expectedOutput = new File('./model/targetModel.xmi').text
        assertEquals(expectedOutput, StringUtil.removeEscapeChars( response['output'].toString() ))
    }
}
