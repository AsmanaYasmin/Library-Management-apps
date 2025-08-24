<?php
header("Content-Type: application/json");
include 'db.php';

$query = "SELECT id, name, phone FROM users ORDER BY id DESC";
$result = mysqli_query($conn, $query);

$users = [];

while ($row = mysqli_fetch_assoc($result)) {
    $users[] = $row;
}

echo json_encode([
    "success" => true,
    "users" => $users
]);
?>
