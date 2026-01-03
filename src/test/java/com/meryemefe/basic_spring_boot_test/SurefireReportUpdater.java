package com.meryemefe.basic_spring_boot_test;

import org.junit.jupiter.api.DisplayName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility to update Surefire XML reports with JUnit 5 @DisplayName annotations.
 * This class reads test classes, extracts @DisplayName annotations, and updates
 * the XML reports to use display names instead of method names.
 */
public class SurefireReportUpdater {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Usage: SurefireReportUpdater <test-classes-dir> <surefire-reports-dir>");
			System.exit(1);
		}

		String testClassesDir = args[0];
		String surefireReportsDir = args[1];

		try {
			Map<String, String> displayNames = extractDisplayNames(testClassesDir);
			updateSurefireReports(surefireReportsDir, displayNames);
			System.out.println("Successfully updated Surefire reports with display names");
		} catch (Exception e) {
			System.err.println("Error updating Surefire reports: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static Map<String, String> extractDisplayNames(String testClassesDir) throws Exception {
		Map<String, String> displayNames = new HashMap<>();
		Path testClassesPath = Paths.get(testClassesDir);

		if (!Files.exists(testClassesPath)) {
			System.out.println("Test classes directory not found: " + testClassesDir);
			return displayNames;
		}

		Files.walk(testClassesPath)
			.filter(Files::isRegularFile)
			.filter(p -> p.toString().endsWith(".class"))
			.forEach(classFile -> {
				try {
					String className = getClassName(classFile, testClassesPath);
					Class<?> clazz = Class.forName(className);
					
					// Get class display name
					DisplayName classDisplayName = clazz.getAnnotation(DisplayName.class);
					if (classDisplayName != null) {
						displayNames.put(className, classDisplayName.value());
					}

					// Get method display names
					for (Method method : clazz.getDeclaredMethods()) {
						DisplayName methodDisplayName = method.getAnnotation(DisplayName.class);
						if (methodDisplayName != null) {
							String key = className + "#" + method.getName();
							displayNames.put(key, methodDisplayName.value());
						}
					}
				} catch (Exception e) {
					// Ignore classes that can't be loaded
				}
			});

		return displayNames;
	}

	private static String getClassName(Path classFile, Path basePath) {
		String relativePath = basePath.relativize(classFile).toString();
		return relativePath.replace(File.separator, ".").replace(".class", "");
	}

	private static void updateSurefireReports(String surefireReportsDir, Map<String, String> displayNames) throws Exception {
		Path reportsPath = Paths.get(surefireReportsDir);
		if (!Files.exists(reportsPath)) {
			System.out.println("Surefire reports directory not found: " + surefireReportsDir);
			return;
		}

		Files.walk(reportsPath)
			.filter(Files::isRegularFile)
			.filter(p -> p.toString().endsWith(".xml") && p.getFileName().toString().startsWith("TEST-"))
			.forEach(xmlFile -> {
				try {
					updateXmlReport(xmlFile.toFile(), displayNames);
				} catch (Exception e) {
					System.err.println("Error updating " + xmlFile + ": " + e.getMessage());
				}
			});
	}

	private static void updateXmlReport(File xmlFile, Map<String, String> displayNames) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);

		// Get the classname from the testsuite
		Element testsuite = doc.getDocumentElement();
		String testsuiteName = testsuite.getAttribute("name");
		String className = testsuiteName.substring(testsuiteName.lastIndexOf('.') + 1);

		// Update testcase names
		NodeList testcases = doc.getElementsByTagName("testcase");
		boolean updated = false;

		for (int i = 0; i < testcases.getLength(); i++) {
			Element testcase = (Element) testcases.item(i);
			String methodName = testcase.getAttribute("name");
			String key = testsuiteName + "#" + methodName;
			
			// Try with full class name first, then just the simple class name
			String displayName = displayNames.get(key);
			if (displayName == null) {
				key = className + "#" + methodName;
				displayName = displayNames.get(key);
			}

			if (displayName != null && !displayName.equals(methodName)) {
				testcase.setAttribute("name", displayName);
				updated = true;
			}
		}

		if (updated) {
			// Write the updated XML back to the file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
			transformer.transform(source, result);
		}
	}
}

