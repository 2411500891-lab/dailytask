<?php
include "config.php";
$d = json_decode(file_get_contents("php://input"), true);
$stmt = $conn->prepare("INSERT INTO tasks(user_id,title,description,category,priority,due_date,due_time) VALUES(?,?,?,?,?,?,?)");
$stmt->bind_param("issssss",$d['user_id'],$d['title'],$d['description'],$d['category'],$d['priority'],$d['due_date'],$d['due_time']);
echo json_encode($stmt->execute()
  ? ["status"=>"success","message"=>"Tugas ditambahkan"]
  : ["status"=>"error","message"=>"Gagal"]);
?>