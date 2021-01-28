<?php 
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");

    $mem_id = $_POST["mem_id"];
   
    $statement = mysqli_prepare($con, "SELECT mem_id FROM HANIUMUSER WHERE mem_id = ?");
    mysqli_stmt_bind_param($statement, "s", $mem_id);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $mem_id);


    $response = array();
    $response["success"] = true;
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"]=false;
        $response["mem_id"]=$mem_id;
    }
   
    echo json_encode($response);



?>