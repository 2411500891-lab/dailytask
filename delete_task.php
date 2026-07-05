<?php
include "config.php";
$d = json_decode(file_get_contents("php://input"), true);
$stmt = $conn->prepare("DELETE FROM tasks WHERE id=?");
$stmt->bind_param("i",$d['id']);
echo json_encode($stmt->execute()
  ? ["status"=>"success","message"=>"Terhapus"]
  : ["status"=>"error","message"=>"Gagal"]);
?>