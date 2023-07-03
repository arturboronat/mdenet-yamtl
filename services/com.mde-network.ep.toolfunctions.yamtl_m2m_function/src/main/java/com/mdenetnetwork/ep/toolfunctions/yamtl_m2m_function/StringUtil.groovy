package com.mdenetnetwork.ep.toolfunctions.yamtl_m2m_function

class StringUtil {
	def static String removeEscapeChars(String text) {
		text.replaceAll(/\\"/, '"').replaceAll(/\\t/,'\t').replaceAll(/\\r\\n/, System.lineSeparator()).replaceAll(/^\"|\"$/, '')
	}
}
