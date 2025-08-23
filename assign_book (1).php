<?php
ob_clean();
header('Content-Type: application/json; charset=UTF-8');

require_once 'db.php';

$input = json_decode(file_get_contents("php://input"), true);

if (!isset($input['book_name']) || !isset($input['member_name']) || !isset($input['book_id'])) {
    echo json_encode(["success" => false, "error" => "Missing parameters"]);
    exit;
}

$book_name   = mysqli_real_escape_string($conn, $input['book_name']);
$member_name = mysqli_real_escape_string($conn, $input['member_name']);
$book_id     = mysqli_real_escape_string($conn, $input['book_id']);

// Check if the book is already assigned
$sql_check = "SELECT * FROM books WHERE id = '$book_id' AND status = 'assign'";
$result = mysqli_query($conn, $sql_check);

if (mysqli_num_rows($result) > 0) {
    echo json_encode(["success" => false, "error" => "Book is already assigned"]);
    exit;
}

// Not assigned yet, so insert into issued_books and update status
$sql_insert = "INSERT INTO issued_books (book_id, book_name, member_name) 
               VALUES ('$book_id', '$book_name', '$member_name')";

if (mysqli_query($conn, $sql_insert)) {
    $sql_update = "UPDATE books SET status = 'assign' WHERE id = '$book_id'";
    if (mysqli_query($conn, $sql_update)) {
        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => "Update failed: " . mysqli_error($conn)]);
    }
} else {
    echo json_encode(["success" => false, "error" => "Insert failed: " . mysqli_error($conn)]);
}

exit;
?>
