<?php
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");
    mysqli_query($con,'SET NAMES utf8');

    $mem_id = $_POST["mem_id"];
    $mem_name = $_POST["mem_name"];
    $mem_phone = $_POST["mem_phone"];
    $mem_emergency = $_POST["mem_emergency"];    
    
    $statement = mysqli_query($con, "UPDATE HANIUMUSER SET mem_name = '$mem_name', mem_phone = '$mem_phone', mem_emergency = '$mem_emergency' WHERE mem_id = '$mem_id'");
    

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>