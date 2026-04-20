#!/bin/sh
set -e

# Copy the nginx config to the right place
cp /app/nginx.conf /etc/nginx/conf.d/default.conf

# Start nginx
exec nginx -g "daemon off;"
