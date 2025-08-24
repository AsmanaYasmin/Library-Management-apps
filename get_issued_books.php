<?php
// Ensure no extra output
ob_clean();
header('Content-Type: application/json; charset=UTF-8');

// DB connection
require_once 'db.php';

// Fetch data
$sql = "SELECT * FROM issued_books ORDER BY id DESC";
$result = mysqli_query($conn, $sql);

$data = [];
if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        $data[] = $row;
    }
    echo json_encode(["success" => true, "data" => $data]);
} else {
    echo json_encode(["success" => false, "error" => mysqli_error($conn)]);
}
exit;
