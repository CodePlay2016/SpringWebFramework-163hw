<html>
<head>
    <title>userList</title>
</head>
<body>
<table border="1px">
    <thead>
    <tr>
        <td>userId</td>
        <td>userName</td>
    </tr>
    </thead>
    <tbody>
    <#list userList as user>
    <tr>
        <td>${user.userId}</td>
        <td>${user.balance}</td>
    </tr>
    </#list>
    <p/>
    </tbody>
</table>
</body>
</html>