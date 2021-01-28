<?php
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");
    mysqli_query($con,'SET NAMES utf8');

    $mem_id = $_POST["mem_id"];    
    
    $statement = mysqli_query($con, "DELETE FROM HANIUMUSER WHERE mem_id = '$mem_id'");
    

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>