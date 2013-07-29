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

# "./toggle_GWT_support.sh check" to check if we're using the custom GWT version
# "./toggle_GWT_support.sh" to enable/disable GWT support

# Note: GNU sed is required (sed or gsed)

SRC="Bouboule/src/be/ac/ucl/lfsab1509/bouboule/game"
if test ! -d $SRC; then
	echo "Not in the right folder"
	exit 1
fi

GlobalSettings="$SRC/gameManager/GlobalSettings.java"
isGWT=`grep ISGWT $GlobalSettings | grep -c true`

if test "$1" = "check"; then
	test $isGWT -eq 1 && echo "GWT support is enabled" || echo "GWT support is disabled"
	exit 0
fi

type gsed > /dev/null && SED="gsed" || SED="sed" ## MacOSX...

if test $isGWT -eq 1; then # disable GWT support
	echo "Disabling GWT support"
	$SED -i "/boolean ISGWT/ s/true/false/" $GlobalSettings

	# uncomment lines
	grep -r -l "// @@COMMENT_GWT@@" $SRC   | xargs $SED -i '/@@COMMENT_GWT@@/s/^\/\///g'
	# comment lines
	grep -r -l "// @@UNCOMMENT_GWT@@" $SRC | xargs $SED -i '/@@UNCOMMENT_GWT@@/s/^/\/\//g'

	echo "GWT support has been disabled"

else # enable GWT support
	echo "Enabling GWT support"
	$SED -i "/boolean ISGWT/ s/false/true/" $GlobalSettings

	# comment lines
	grep -r -l "// @@COMMENT_GWT@@" $SRC   | xargs $SED -i '/@@COMMENT_GWT@@/s/^/\/\//g'
	# uncomment lines
	grep -r -l "// @@UNCOMMENT_GWT@@" $SRC | xargs $SED -i '/@@UNCOMMENT_GWT@@/s/^\/\///g'

	echo "GWT support has been enabled"
fi
echo "Don't forget to refresh Bouboule dirs in Eclipse if you're using it ;)"
