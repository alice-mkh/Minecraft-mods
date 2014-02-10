#!/bin/bash

MCPDIR=~/.mcp
VERSION=`date --date="-1minute" -I`
UPLOADDIR=~/Dropbox/Public/minecraft-mods-daily

cd $MCPDIR
cd src-mods
YESTERDAY=`date --date="yesterday" -I`
LOG=`git log --since=$YESTERDAY`
git log --since=$YESTERDAY > $UPLOADDIR/changelog-latest
if [ ! -s $UPLOADDIR/changelog-latest ]; then
    exit 0
fi
notify-send -i document-properties "Daily Minecraft mod builds" "Starting building $VERSION"
cd ..
./make.sh
mkdir -p $UPLOADDIR/$VERSION
cp $UPLOADDIR/changelog-latest $UPLOADDIR/$VERSION/changelog
cd reobf/result2/client
cp * $UPLOADDIR/$VERSION
cd $UPLOADDIR/$VERSION
rm unused.zip
cd ..
rm latest
ln -s $VERSION latest
matedialog --question --title "Daily Minecraft mod builds" --text "Is this version stable?"
if [[ $? == "0" ]]; then
    rm -rf stable-old
    mv stable stable-old
    cp -r $VERSION stable
fi
FILES=`ls -v | tail -12`
mkdir temp
for F in $FILES; do
    mv $F temp
done
rm -r `ls -I "[lts]*"`
cd temp
for F in $FILES; do
    mv $F ..
done
cd ..
rm -r temp
notify-send -i document-properties "Daily Minecraft mod builds" "Finished building $VERSION"
