<?php
header("Content-Type: application/json");
include 'db.php'; 

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

    $phone = $_POST['phone'];
    $password = $_POST['password'];

   
    $stmt = $conn->prepare("SELECT id, name, phone FROM users  WHERE phone = ? AND password = ?");
    $stmt->bind_param("ss", $phone, $password);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();
        $response = array(
            "status" => "VALID LOGIN",
            "id" => $user['id'],
            "name" => $user['name'],
            "phone" => $user['phone']
        );
        echo json_encode($response);
    } else {
        echo json_encode(array("status" => "Invalid Credentials"));
    }

    $stmt->close();
    mysqli_close($conn);

} else {
    echo json_encode(array("status" => "Invalid Request"));
}
?>