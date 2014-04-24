#!/bin/bash
cd ../..
python src-mods/build/runtime/make.py "$@"
read -p "Press enter to continue..."