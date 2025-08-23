<?php
$host = "localhost";
$username = "farhanat_books";
$password = "Jr9N=!8fT7XC";
$database = "farhanat_books";

$conn = mysqli_connect($host, $username, $password, $database);

if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}
?>