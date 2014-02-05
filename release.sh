#!/bin/sh
echo "Starting new gpg-agent"
gpg --armor --detach-sign --use-agent --sign pom.xml
if [ $? -ne 0 ] ; then
	echo "ERROR: Unable to run gpg properly"
	exit 1
fi

gpg --verify pom.xml.asc
if [ $? -ne 0 ]; then
	rm pom.xml.asc
    echo "It appears that you fat-fingered your GPG passphrase"
	exit 1
fi
rm pom.xml.asc

mvn release:prepare --batch-mode

