#!/bin/bash

APPDIR="$(dirname -- "${0}")"
cd $APPDIR

CLASSPATH="@jarName@"
export CLASSPATH

java UI.MainFrame
