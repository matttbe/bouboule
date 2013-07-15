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

# "./switch_assets.sh check" to check if we're using HD or Non HD version
# "./switch_assets.sh" to switch version

ANDROID="Bouboule-android"
if test ! -d $ANDROID; then
	echo "Not in the right folder"
	exit 1
fi

if test ! -L $ANDROID/assets; then
	echo "$ANDROID/assets is not a symlink"
	exit 1
fi

GlobalSettings="Bouboule/src/be/ac/ucl/lfsab1509/bouboule/game/gameManager/GlobalSettings.java"
isHD=`grep ISHD $GlobalSettings | grep -c true`

if test "$1" = "check"; then
	test $isHD -eq 1 && echo "Version HD" || echo "Version Non HD"
	exit 0
fi

rm $ANDROID/assets # symlink

if test $isHD -eq 1; then # HD -> NHD
	echo "Switch from HD to Non HD version"
	sed -i "/float APPWIDTH/ s/1311f/800f/" $GlobalSettings
	sed -i "/float APPHEIGHT/ s/2048f/1250f/" $GlobalSettings
	sed -i "/boolean ISHD/ s/true/false/" $GlobalSettings
	sed -i "/float HD/ s/1.6384f/1f/" $GlobalSettings
	cd $ANDROID
	ln -s assets_NHD assets
	cd ..
	echo "You're now using the Non HD version"
else # NHD -> HD
	echo "Switch from Non HD to HD version"
	sed -i "/float APPWIDTH/ s/800f/1311f/" $GlobalSettings
	sed -i "/float APPHEIGHT/ s/1250f/2048f/" $GlobalSettings
	sed -i "/boolean ISHD/ s/false/true/" $GlobalSettings
	sed -i "/float HD/ s/1f/1.6384f/" $GlobalSettings
	cd $ANDROID
	ln -s assets_HD assets
	cd ..
	echo "You're now using the HD version"
fi
