<?php
include "config.php";
$d = json_decode(file_get_contents("php://input"), true);
$stmt = $conn->prepare("UPDATE tasks SET title=?,description=?,category=?,priority=?,due_date=?,due_time=?,is_done=? WHERE id=?");
$stmt->bind_param("ssssssii",$d['title'],$d['description'],$d['category'],$d['priority'],$d['due_date'],$d['due_time'],$d['is_done'],$d['id']);
echo json_encode($stmt->execute()
  ? ["status"=>"success","message"=>"Tugas diperbarui"]
  : ["status"=>"error","message"=>"Gagal"]);
?>