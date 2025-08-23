<?php
header("Content-Type: application/json");
include 'db.php';

/* 
 🔹 বড় সাইজ ফাইল আপলোডের জন্য PHP সেটিংস override
    চাইলে 200M → 500M বা তার থেকেও বেশি করতে পারেন
*/
ini_set('upload_max_filesize', '500M');
ini_set('post_max_size', '500M');
ini_set('max_execution_time', 600); // 10 মিনিট
ini_set('max_input_time', 600);
ini_set('memory_limit', '1024M');

$title  = $_POST['title'] ?? '';
$author = $_POST['authorname'] ?? '';

if (!isset($_FILES['pdf']) || empty($title) || empty($author)) {
    echo json_encode(["success" => false, "message" => "Missing data."]);
    exit;
}

// 🔹 আপলোড এরর চেক
if ($_FILES['pdf']['error'] !== UPLOAD_ERR_OK) {
    echo json_encode([
        "success" => false,
        "message" => "Upload error code: " . $_FILES['pdf']['error']
    ]);
    exit;
}

$pdf = $_FILES['pdf'];
$filename = time() . "_" . basename($pdf['name']);
$destination = __DIR__ . "/uploads/" . $filename; // নিরাপদভাবে path resolve

// 🔹 ফাইল আপলোড
if (move_uploaded_file($pdf['tmp_name'], $destination)) {
    $file_url = "https://farhana42.top/uploads/" . $filename;

    $stmt = $conn->prepare("INSERT INTO books (title, authorname, file_url) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $title, $author, $file_url);

    if ($stmt->execute()) {
        echo json_encode([
            "success" => true,
            "message" => "PDF uploaded successfully",
            "file_url" => $file_url
        ]);
    } else {
        echo json_encode(["success" => false, "message" => "DB Error"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "File upload failed"]);
}
