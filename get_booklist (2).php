<?php
header("Content-Type: application/json");
include 'db.php';

$sql = "SELECT id, title, authorname, file_url, status 
        FROM books 
        WHERE status = 'return' 
        ORDER BY id DESC";

$result = mysqli_query($conn, $sql);

if (!$result) {
    echo json_encode([
        "success" => false,
        "message" => "Query failed: " . mysqli_error($conn)
    ]);
    exit;
}

$books = [];

if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $books[] = [
            "id" => $row['id'],
            "title" => $row['title'],
            "authorname" => $row['authorname'],
            "file_url" => $row['file_url'],
            "status" => $row['status']
        ];
    }

    echo json_encode([
        "success" => true,
        "books" => $books
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "No books found"
    ]);
}
?>
