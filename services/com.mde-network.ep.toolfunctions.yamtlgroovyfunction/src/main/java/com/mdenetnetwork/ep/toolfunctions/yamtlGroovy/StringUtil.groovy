package com.mdenetnetwork.ep.toolfunctions.yamtlGroovy

class StringUtil {
	def static String removeEscapeChars(String text) {
		text.replaceAll(/\\"/, '"').replaceAll(/\\t/,'\t').replaceAll(/\\r\\n/, System.lineSeparator()).replaceAll(/^\"|\"$/, '')
	}
}
