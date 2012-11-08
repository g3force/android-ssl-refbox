#!/bin/bash

cmds="Start Stop Halt Ready BeginFirstHalf BeginHalfTime BeginSecondHalf BeginOvertimeHalf1 BeginOvertimeHalf2 BeginPenaltyShootout KickOffTigers KickOffEnemies PenaltyTigers PenaltyEnemies DirectFreeKickTigers DirectFreeKickEnemies IndirectFreeKickTigers IndirectFreeKickEnemies TimeoutTigers TimeoutEnemies TimeoutEnd GoalScoredTigers GoalScoredEnemies DecreaseGoalScoreTigers DecreaseGoalScoreEnemies YellowCardTigers YellowCardEnemies RedCardTigers RedCardEnemies Cancel TimeUpdate UnknownCommand"

for cmd in $cmds
do
    echo "<Button
        android:id=\"@+id/$cmd\"
        android:layout_width=\"wrap_content\"
        android:layout_height=\"wrap_content\"
        android:layout_alignParentLeft=\"true\"
        android:layout_alignParentTop=\"true\"
        android:text=\"@string/$cmd\" />"
    echo
done

echo
echo "#########################################################"
echo

for cmd in $cmds
do
    echo "<string name=\"$cmd\">$cmd</string>"
done

for cmd in $cmds
do
    echo "<item>$cmd</item>"
done
