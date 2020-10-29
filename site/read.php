<?php
	include "conexao.php";
	$sql_read = "SELECT * FROM contatos";
	$dados = $conn->query($sql_read);

	$resultado = array();
	
	if ($dados->num_rows > 0) {
  	// output data of each row
  		while($row = $dados->fetch_assoc()) {
      		
          	$resultado[] = $row;
  		}
    } else {
  		echo "0 results";
	}
echo json_encode($resultado);
	$conn->close();
?>

