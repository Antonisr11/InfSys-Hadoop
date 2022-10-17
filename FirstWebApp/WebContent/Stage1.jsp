<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="app.Main"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>Stage 1</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<link rel="stylesheet" href="styles.css">

<script type="text/JavaScript">
	function AutoRefresh(ms) {
		setTimeout("location.reload(true);", ms);
	}
</script>

<%
	boolean enableRun = true;
	boolean enableNext = true;
	
	if (Main.readFile("enableRun").replace("\n", "").equals("false")) {
		enableRun = false;
	}
	
	if (Main.readFile("enableNext").replace("\n", "").equals("false")) {
		enableNext = false;
	}
	
	boolean clickedRun = Main.readFile("clickedRun").replace("\n", "").equals("true");
	
	String logs = Main.readFile("logs");
	
	if (request.getParameter("next") != null) {
		Main.writeFile("clickedNext", "true");
		response.setStatus(response.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", "http://localhost:8080/FirstWebApp/Stage1.jsp");
	}
%>

</head>
<body <%if (!enableNext) {%> onload="JavaScript:AutoRefresh(900);"
	<%}%>>
	<br>
	<br>
	<div class="jumbotron text-center">
		<h2 id="heading">Project in Information Systems Governance 2022</h2>
		<p style="color: grey">Antonis</p>
	</div>
	<br>

	<div class="row mymenu">
		<div class="col-2"></div>
		<div
			class="col-2 d-flex justify-content-center align-items-center active">
			<strong>Start</strong>
		</div>
		<div class="col-2 d-flex justify-content-center align-items-center">
			<strong>Kafka & Flume</strong>
		</div>
		<div class="col-2 d-flex justify-content-center align-items-center">
			<strong>MapReduce</strong>
		</div>
		<div class="col-2 d-flex justify-content-center align-items-center">
			<strong>Mahoot</strong>
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-2"></div>
		<div class="col-8">
			<div class="progress">
				<div class="progress-bar progress-bar-striped progress-bar-animated"
					role="progressbar" aria-valuemin="0" aria-valuemax="100"
					style="width: 25%"></div>
			</div>
		</div>
	</div>
	<br>
	<div class="row">
		<div class="col-2"></div>
		<div class="col-2" style="font-size: 25px; color: #673AB7;">Logs:</div>
		<div class="col-4"></div>
		<div class="col-2"
			style="font-size: 25px; color: gray; font-weight: normal; text-align: right;">Step
			1/4</div>
	</div>
	<br>
	<div class="row">
		<div class="col-2"></div>
		<div class="col-8">
			<textarea id="logsTextarea" style="width: 100%" rows="10" disabled><%=logs.toString()%></textarea>
		</div>
	</div>
	<br>
	<form action="#">
		<div class="row">
			<div class="col-8"></div>
			<div class="col-1 d-flex justify-content-center align-items-center">
				<input type="submit" value="Run" name="run" class="action-button"
					<%if (!enableRun) {%> disabled <%}%>>
			</div>
			<div class="col-1 d-flex justify-content-center align-items-center">
				<button class="action-button" type="button"
					onclick='window.location.href = "http://localhost:8080/FirstWebApp/Stage2.jsp?next=1"'
					<%if (!enableNext) {%> disabled <%}%>>Next</button>
			</div>
		</div>
	</form>

	<script type="text/JavaScript">
		document.getElementById("logsTextarea").scrollTop = document
				.getElementById("logsTextarea").scrollHeight;
	</script>

	<%
		if (request.getParameter("run") != null && !clickedRun) {
		Main.writeFile("clickedRun", "true");
	}
	%>

</body>
</html>