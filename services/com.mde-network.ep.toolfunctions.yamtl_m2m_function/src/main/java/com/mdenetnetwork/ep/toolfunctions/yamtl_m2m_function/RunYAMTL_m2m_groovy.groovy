package com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function

import org.eclipse.emf.ecore.EPackage
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.gson.JsonObject
import com.mdenetnetwork.ep.toolfunctions.core.MdeNetToolFunction

import groovy.io.FileType
import yamtl.core.YAMTLModule
import yamtl.groovy.YAMTLGroovyExtensions_dynamicEMF

class RunYAMTL_m2m_groovy extends MdeNetToolFunction {
	private static final Logger logger = LoggerFactory.getLogger(RunYAMTL_m2m_groovy.class);
	
	/**
	 * 
	 * Function that executes basic M2M transformation where one model is received as input and 
	 * another one is produced.
	 * 
	 * The tool function is flexible: trafo, metamodels and models can be changed.
	 * 
	 * Constraints: the transformation will only execute with the dependencies
	 * included in this project. Additional dependencies required by new transformations
	 * require modifying this function.
	 * 
	 * Parameters:
	 * trafoGroovy: groovy class specializing YAMTLModule
	 * inMetamodel: xmi of input metamodel
	 * inModel: xmi of input model
	 * outMetamodel: xmi of output metamodel
	 * returns response with a field outModel: xmi of output model
	 */ 
    @Override
    void serviceImpl(JsonObject request, JsonObject response) {
		
		logger.info("Received request: ${request}")
		
		try {
			String trafoGroovy = StringUtil.removeEscapeChars(request['trafoGroovy'].toString())
			def matcher = (trafoGroovy =~ /(?s).*class\s+(\w+)\s+extends\s+YAMTLModule.*/)
			String className = matcher[0][1]
			if (!className) throw new RuntimeException("The script must contain a YAMTLModule specialization defining a model transformation.")
			
				
			// create tmp file for transformation under ./model
			initDirectory(className)
				
				
			GroovyClassLoader classLoader = new GroovyClassLoader();
			Class<?> xformModuleClass = classLoader.parseClass(new GroovyCodeSource(trafoGroovy, ("${className}.groovy"), className));
	
			// running YAMTL using dynamic EMF
			
			// Load metamodels
			def inPk = loadMetamodel(className, request, "inMetamodel", xformModuleClass) as EPackage
			def outPk = loadMetamodel(className, request, "outMetamodel", xformModuleClass) as EPackage
			
			// Initialise engine
			def xform = xformModuleClass.newInstance(inPk, outPk) as YAMTLModule
			YAMTLGroovyExtensions_dynamicEMF.init(xform)
			
			// Load input model
			def inDomainName = xform.getInDomains().find { it.value == inPk.getNsURI() }.key
			String inModelPath = "./model/${className}/inModel.xmi"
			def file = new File(inModelPath)
			file << StringUtil.removeEscapeChars(request['inModel'].toString()) 
			xform.loadInputModels([(inDomainName):inModelPath])
			
			// Execute transformations
			xform.execute()
			
			// Get the output model
			def outDomainName = xform.getOutDomains().find { it.value == outPk.getNsURI() }.key
			String outModelPath = "./model/${className}/outModel.xmi"
			xform.saveOutputModels([(outDomainName):outModelPath])
			
			// Return the xmi contents
			response.addProperty("output", new File(outModelPath).text )
			//response.addProperty("generatedText", xform.toStringStats())
			
			logger.info("Generated response: ${response}");

		} catch(Exception e) {
			response.addProperty("output", e.message )
		}
    }
	
	def loadMetamodel(String className, JsonObject request, String requestFieldName, Class<?> xform ) {
		def metamodelXmi = request[requestFieldName] as String
		String metamodelPath = "./model/${className}/${requestFieldName}_metamodel.ecore"
		def file = new File(metamodelPath)
		file << StringUtil.removeEscapeChars(metamodelXmi)
		def metamodel = YAMTLModule.loadMetamodel(metamodelPath)
		metamodel
	}
	
	def initDirectory(String directoryName) {
		def directory = new File("./model/${directoryName}")
		
		if (directory.exists()) {
			// Delete the existing directory and its contents recursively
			directory.eachFileRecurse(FileType.FILES) { file ->
				file.delete()
			}
			directory.deleteDir()
		}
		
		// Create the directory
		directory.mkdirs()
	}
	
}
