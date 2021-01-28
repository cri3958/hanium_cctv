<?php 
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");
    mysqli_query($con,'SET NAMES utf8');

    $mem_id = $_POST["mem_id"];
    $mem_pw = $_POST["mem_pw"];
    $mem_name = $_POST["mem_name"];
    $mem_phone = $_POST["mem_phone"];
    $mem_emergency = $_POST["mem_emergency"];

    $statement = mysqli_prepare($con, "INSERT INTO HANIUMUSER VALUES (?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssss", $mem_id, $mem_pw, $mem_name, $mem_phone, $mem_emergency);
    mysqli_stmt_execute($statement);


    $response = array();
    $response["success"] = true;
 
   
    echo json_encode($response);



?>