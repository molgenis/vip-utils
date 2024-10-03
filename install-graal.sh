#!/bin/bash

HOME=$1
if [ -z "$HOME" ]; then
  echo "Missing home dir parameter";
  exit 1;
else
  if [ ! -d "$HOME/graalvm/graalvm-community-openjdk-21.0.2+13.1" ]; then
    echo "GraalVM is not cached, installing...";
    wget https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-21.0.2/graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz;
    mkdir -p $HOME/graalvm;
    tar -xzf graalvm-community-jdk-21.0.2_linux-x64_bin.tar.gz -C $HOME/graalvm;
    else
    echo "Using cached GraalVM";
  fi


  export JAVA_HOME=$HOME/graalvm/graalvm-community-openjdk-21.0.2+13.1
  export PATH=JAVA_HOME/bin:$PATH
fi