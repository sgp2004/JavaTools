<?php
include("../include/connect.db.php"); //加载研发database

function collectDataInJson ($beginTime, $endTime,$tableName,$condition) {
        $bt = date("Y-m-d H:i:s",$beginTime);
        $et = date("Y-m-d H:i:s",$endTime);
        $sql = "select * from '$tableName' where dt between '$et' and '$bt' order by dt";
        $result = mysql_query($sql);
        while($myrow = mysql_fetch_array($result)){
                $myrow[] = $myrow;
        }
        $recordsOf=array();
        foreach($myrow as $value)
        {
                $recordsOf[$value["dt"]][]=$value;
        }
        $i=0;
        foreach($recordsOf as $key=>$value)
        {

                 foreach($value as $id=>$record){
                       $createTime=$record['dt'];
                       $cTime=strtotime(".$createTime.");
                       $jsRecord[$i]['time']=date("H:i:s",$createTime);
                       if($tableName == "dash_jarfeed")
                            $jsRecord[$i][$record["node"]]=round($record["avgtime"],2);   //应更改 dash_jarfeed 表结构为通用，然后去掉此判断
                       else
                            $jsRecord[$i][$record["ip"]]=round($record["avgtime"],2);
                 }
                 $jsRecords[] = $jsRecord[$i];
                 $i++;
        }
        $recordsInJson = json_encode($jsRecords);
        return $recordsInJson;
}


function getGraphInfo () {
        $sql = "select * from graph_info";
        $result = mysql_query($sql);
        while($myrow = mysql_fetch_array($result)){
                $record[] = $myrow;
        }
        return $record;
}
?>