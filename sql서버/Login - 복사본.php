<?php
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");
    mysqli_query($con,'SET NAMES utf8');

    $mem_id = $_POST["mem_id"];
    $mem_pw = $_POST["mem_pw"];
    
    
    $statement = mysqli_prepare($con, "SELECT * FROM HANIUMUSER WHERE mem_id = ? AND mem_pw = ?");
    mysqli_stmt_bind_param($statement, "ss", $mem_id, $mem_pw);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $mem_id, $mem_pw, $mem_name, $mem_phone, $mem_emergency, $mem_imagedata);

    $response = array();
    $response["success"] = false;
    
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["mem_id"] = $mem_id;
        $response["mem_pw"] = $mem_pw;
        $response["mem_name"] = $mem_name;
        $response["mem_phone"] = $mem_phone;
        $response["mem_emergency"] = $mem_emergency;
        $response["mem_imagedata"] = $imagedata;
    }

    echo json_encode($response);



?>