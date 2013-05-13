#!/bin/bash

MCPDIR=~/.mcp
VERSION=`date -I`
UPLOADDIR=~/Dropbox/Public/minecraft-mods-daily

cd $MCPDIR
cd src-mods
YESTERDAY=`date --date="yesterday" -I`
LOG=`git log --since=$YESTERDAY`
git log --since=$YESTERDAY > $UPLOADDIR/changelog-latest
if diff -q $UPLOADDIR/changelog-latest $UPLOADDIR/latest/changelog > /dev/null; then
    exit 0
fi
notify-send -i document-properties "Daily Minecraft mod builds" "Starting building $VERSION"
cd ..
bash make.sh
mkdir -p $UPLOADDIR/$VERSION
cp $UPLOADDIR/changelog-latest $UPLOADDIR/$VERSION/changelog
cd reobf/result2/client
cp * $UPLOADDIR/$VERSION
cd $UPLOADDIR/$VERSION
rm unused.zip
cd ..
rm latest
ln -s $VERSION latest
FILES=`ls -v | tail -7`
mkdir temp
for F in $FILES; do
    mv $F temp
done
rm -r `ls -I "[lt]*"`
cd temp
for F in $FILES; do
    mv $F ..
done
cd ..
rm -r temp
notify-send -i document-properties "Daily Minecraft mod builds" "Finished building $VERSION"
