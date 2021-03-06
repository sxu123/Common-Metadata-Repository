#!/bin/sh

# A temporary helper script for doing deployments until Bamboo deployments are setup.

if [ -z "$1" ]
  then
    echo "Must supply the name of the environment to deploy to: wl, sit."
    exit 1
fi

support/clean-and-install-dependencies.sh

cd ../metadata-db-app
cmr_deploy $1
cd ../index-set-app
cmr_deploy $1
cd ../indexer-app
cmr_deploy $1
cd ../search-app
cmr_deploy $1
cd ../ingest-app
cmr_deploy $1
cd ../bootstrap-app
cmr_deploy $1
