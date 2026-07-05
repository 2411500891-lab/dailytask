<?php
include "config.php";
$user_id = intval($_GET['user_id'] ?? 0);
$stmt = $conn->prepare("SELECT * FROM tasks WHERE user_id=? ORDER BY is_done ASC, due_date ASC");
$stmt->bind_param("i",$user_id); $stmt->execute();
$res = $stmt->get_result();
$tasks = []; while($r=$res->fetch_assoc()) $tasks[]=$r;
echo json_encode(["status"=>"success","data"=>$tasks]);
?>