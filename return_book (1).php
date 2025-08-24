<?php
header("Content-Type: application/json");
include 'db.php';

if (isset($_POST['book_id']) && isset($_POST['issue_id'])) {
    $bookId = intval($_POST['book_id']);   // books table ID
    $issueId = intval($_POST['issue_id']); // issued_books table ID

    // Debug log
    file_put_contents("debug_return.txt", "Book ID: $bookId, Issue ID: $issueId\n", FILE_APPEND);

    // 1️⃣ books টেবিলে status আপডেট
    $updateBookSql = "UPDATE books SET status = 'return' WHERE id = $bookId";
    $updateBookResult = mysqli_query($conn, $updateBookSql);

    if ($updateBookResult && mysqli_affected_rows($conn) > 0) {
        // 2️⃣ issued_books টেবিল থেকে ডিলিট
        $deleteIssuedSql = "DELETE FROM issued_books WHERE id = $issueId";
        mysqli_query($conn, $deleteIssuedSql);

        echo json_encode(["success" => true, "message" => "Book returned and issued record deleted"]);
    } else {
        echo json_encode(["success" => false, "message" => "No book found or already returned"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Book ID or Issue ID missing"]);
}
?>
