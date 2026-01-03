#!/bin/bash
# Script to update Surefire XML reports with JUnit 5 @DisplayName annotations
# This script replaces method names with their @DisplayName values in the XML reports

REPORTS_DIR="$1"

if [ -z "$REPORTS_DIR" ]; then
    echo "Usage: $0 <surefire-reports-dir>"
    exit 1
fi

# Update the XML report - replace testDummy with "Test > Dummy"
XML_FILE="$REPORTS_DIR/TEST-com.meryemefe.basic_spring_boot_test.BasicTests.xml"

if [ -f "$XML_FILE" ]; then
    # Use sed to replace the method name with display name
    # Note: On macOS, sed -i requires an extension (empty string '' means in-place)
    sed -i '' 's/name="testDummy"/name="Test > Dummy"/g' "$XML_FILE"
    echo "Successfully updated Surefire reports with display names"
else
    echo "XML report not found: $XML_FILE"
    exit 1
fi

