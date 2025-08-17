<?php
include 'db.php';

$name = $_POST['name'];
$phone = $_POST['phone'];
$password = $_POST['password'];



$check = mysqli_query($conn, "SELECT * FROM users WHERE phone = '$phone'");
if (mysqli_num_rows($check) > 0) {
    echo json_encode(["success" => false, "message" => "Phone already registered"]);
} else {
    $sql = "INSERT INTO users (name, phone, password) VALUES ('$name', '$phone','$password')";
    if (mysqli_query($conn, $sql)) {
        echo json_encode(["success" => true, "message" => "User registered"]);
    } else {
        echo json_encode(["success" => false, "message" => "Registration failed"]);
    }
}
?>