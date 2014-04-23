#!/bin/bash
cd ../..
python src-mods/build/runtime/deploy.py "$@"
read -p "Press enter to continue..."