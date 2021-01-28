<?php 
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");

    $object_id = $_POST["object_id"];
    $object_pw = $_POST["object_pw"];
   
    $statement = mysqli_prepare($con, "SELECT * FROM HANIUMMEM_OBJECT WHERE object_id = ? AND object_pw = ?");
    mysqli_stmt_bind_param($statement, "ss", $object_id, $object_pw);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $object_id, $object_pw, $object_num, $object_data);


    $response = array();
    $response["success"] = false;
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"]=true;
        $response["object_id"]=$object_id;
        $response["object_pw"]=$object_pw;
        $response["object_num"]=$object_num;
        $response["object_data"]=$object_data;
    }
   
    echo json_encode($response);



?>