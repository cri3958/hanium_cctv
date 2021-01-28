<?php
    $con = mysqli_connect("localhost", "leehojin0207", "bsat1511!", "leehojin0207");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $userPassword = $_POST["userPassword"];
    
    
    $statement = mysqli_prepare($con, "SELECT * FROM HANIUMUSER WHERE userID = ? AND userPassword = ?");
    mysqli_stmt_bind_param($statement, "ss", $userID, $userPassword);
    mysqli_stmt_execute($statement);


    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $userID, $userPassword, $userName);

    $response = array();
    $response["success"] = false;
    
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["userID"] = $userID;
        $response["userPassword"] = $userPassword;
        $response["userName"] = $userName;
    }

    echo json_encode($response);



?>