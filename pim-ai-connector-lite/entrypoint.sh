#!/bin/sh
set -e

# Ensure pim-config.xml is in the correct location
echo "Checking for configuration file..."
if [ -f "/app/pim-config.xml" ]; then
  echo "Configuration file found in /app/"
else
  echo "Configuration file not found in /app/, copying from resources..."
  # Try to copy from resources if exists
  if [ -f "/app/BOOT-INF/classes/pim-config.xml" ]; then
    cp /app/BOOT-INF/classes/pim-config.xml /app/
    echo "Copied configuration from BOOT-INF/classes/"
  else
    echo "WARNING: No pim-config.xml found in resources. Using built-in default values."
  fi
fi

# Set default ACTIVE_PRODUKTTYP_ID if not provided
if [ -z "$ACTIVE_PRODUKTTYP_ID" ]; then
  echo "Setting default ACTIVE_PRODUKTTYP_ID=fashion"
  export ACTIVE_PRODUKTTYP_ID=fashion
fi

echo "Starting application with ACTIVE_PRODUKTTYP_ID=$ACTIVE_PRODUKTTYP_ID"

# Start the application
exec java -jar app.jar