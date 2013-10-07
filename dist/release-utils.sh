#!/bin/bash

REQUIRED_BASH_VERSION=3.0.0

if [[ $BASH_VERSION < $REQUIRED_BASH_VERSION ]]; then
  echo "You must use Bash version 3 or newer to run this script"
  exit
fi

DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)


# DEFINE

VERSION_REGEX='([0-9]*)\.([0-9]*)([a-zA-Z0-9\.]*)'
FILEMGMT="jdf@filemgmt.jboss.org:docs_htdocs"
URL_BASE="http://docs.jboss.org"

# SCRIPT

usage()
{
cat << EOF
usage: $0 options

This script aids in releasing TicketMonster 

OPTIONS:
   -u      Updates version numbers in all POMs, used with -o and -n . Also updates timestamps in the import.sql script.
   -o      Old version number to update from
   -n      New version number to update to
   -p      Publish docs for the given version
   -h      Shows this message
EOF
}

parse_git_branch() {
   git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/'
}

update()
{
   cd $DIR/../
   echo "Updating versions from $OLDVERSION TO $NEWVERSION for all Java and XML files under $PWD"
   perl -pi -e "s/${OLDVERSION}/${NEWVERSION}/g" `find . -name \*.xml -or -name \*.java`
   echo "Updating import.sql script with new dates"
   perl $DIR/../demo/update_import_sql.pl $DIR/../demo/src/main/resources/import.sql
}

publish_docs()
{
   if [[ $VERSION =~ $VERSION_REGEX ]]; then
      MAJOR_VERSION=${BASH_REMATCH[1]}
      MINOR_VERSION=${BASH_REMATCH[2]}
   fi

   if [ "$MAJOR_VERSION" == "UNDEFINED" -o  "$MINOR_VERSION" == "UNDEFINED" ]
   then
      echo "\nUnable to extract major and minor versions\n"
      exit
   fi

   BRANCH=$(parse_git_branch)
   git checkout $VERSION
   echo "Generating guide"
   cd $DIR/../tutorial
   ./generate-guides.sh
   cd $DIR
   git checkout $BRANCH
   RPATH="jdf/$MAJOR_VERSION.$MINOR_VERSION"
   RFILE="ticket-monster-$VERSION"
   SPATH="$DIR/../tutorial/target/guides"
   LPATH="$DIR/target/upload"

   mkdir -p $LPATH/$RPATH
   cp $SPATH/pdf/ticket-monster.pdf $LPATH/$RPATH/ticket-monster-$VERSION.pdf
   cp $SPATH/epub/ticket-monster.epub $LPATH/$RPATH/ticket-monster-$VERSION.epub

   echo "Uploading guides to $URL_BASE/$RPATH"
   rsync -Pvr --protocol=28 $LPATH/* $FILEMGMT

}

OLDVERSION="1.0.0-SNAPSHOT"
NEWVERSION="1.0.0-SNAPSHOT"
VERSION="1.0.0-SNAPSHOT"
CMD="usage"

while getopts “muo:n:r:p:” OPTION

do
     case $OPTION in
         u)
             CMD="update"
             ;;
         h)
             usage
             exit
             ;;
         p)
             VERSION=$OPTARG
             CMD="publish_docs"
             ;;
         o)
             OLDVERSION=$OPTARG
             ;;
         n)
             NEWVERSION=$OPTARG
             ;;
         [?])
             usage
             exit
             ;;
     esac
done

$CMD

