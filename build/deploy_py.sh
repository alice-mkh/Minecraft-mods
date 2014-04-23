#!/bin/bash
cd ../..
python src-mods/build/deploy.py "$@"
read -p "Press enter to continue..."
