#!/bin/sh
#
# This file is part of Bouboule.
#
# Copyright 2013 UCLouvain
#
# Authors:
#  * Group 7 - Course: http://www.uclouvain.be/en-cours-2013-lfsab1509.html
#    Matthieu Baerts <matthieu.baerts@student.uclouvain.be>
#    Baptiste Remy <baptiste.remy@student.uclouvain.be>
#    Nicolas Van Wallendael <nicolas.vanwallendael@student.uclouvain.be>
#    Helene Verhaeghe <helene.verhaeghe@student.uclouvain.be>
#  
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 3 of the License, or
#  (at your option) any later version.
#  
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#  
#  You should have received a copy of the GNU General Public License
#  along with this program; if not, write to the Free Software
#  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
#  MA 02110-1301, USA.

############
## USAGE: ##
############

# "./switch_version_HD_NHD.sh check" to check if we're using HD or Non HD version
# "./switch_version_HD_NHD.sh" to switch version

# Note: GNU sed is required (sed or gsed)

ANDROID="Bouboule-android"
if test ! -d $ANDROID; then
	echo "Not in the right folder"
	exit 1
fi

if test ! -L $ANDROID/assets -o ! -L $ANDROID/res/drawable-xhdpi; then
	echo "$ANDROID/assets or $ANDROID/res/drawable-xhdpi are not symlinks"
	exit 1
fi

GlobalSettings="Bouboule/src/be/ac/ucl/lfsab1509/bouboule/game/gameManager/GlobalSettings.java"
isHD=`grep ISHD $GlobalSettings | grep -c true`

if test "$1" = "check"; then
	test $isHD -eq 1 && echo "Version HD" || echo "Version Non HD"
	exit 0
fi

rm $ANDROID/assets # symlink
rm $ANDROID/res/drawable-xhdpi # symlink

type gsed > /dev/null && SED="gsed" || SED="sed" ## MacOSX...

if test $isHD -eq 1; then # HD -> NHD
	echo "Switch from HD to Non HD version"
	$SED -i "/float APPWIDTH/ s/1311f/800f/" $GlobalSettings
	$SED -i "/float APPHEIGHT/ s/2048f/1250f/" $GlobalSettings
	$SED -i "/boolean ISHD/ s/true/false/" $GlobalSettings
	$SED -i "/float HD/ s/1.6384f/1f/" $GlobalSettings
	cd $ANDROID
	ln -s assets_NHD assets
	cd res
	ln -s ../drawable-xhdpi_NHD drawable-xhdpi
	cd ../..
	echo "You're now using the Non HD version"
else # NHD -> HD
	echo "Switch from Non HD to HD version"
	$SED -i "/float APPWIDTH/ s/800f/1311f/" $GlobalSettings
	$SED -i "/float APPHEIGHT/ s/1250f/2048f/" $GlobalSettings
	$SED -i "/boolean ISHD/ s/false/true/" $GlobalSettings
	$SED -i "/float HD/ s/1f/1.6384f/" $GlobalSettings
	cd $ANDROID
	ln -s assets_HD assets
	cd res
	ln -s ../drawable-xhdpi_HD drawable-xhdpi
	cd ../..
	echo "You're now using the HD version"
fi
echo "Don't forget to refresh Bouboule and Bouboule-android dirs in Eclipse if you're using it ;)"
