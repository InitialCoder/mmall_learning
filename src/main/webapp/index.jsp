<%@ page language="java"  contentType="text/html; charset=UTF-8" %>
<html>
<body>
<h2>Hello World!</h2>

springMvc 上传文件
<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input name="upload_file" type="file">
    <input type="submit" value="springMvc 上传文件">
</form>

富文本 上传文件
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input name="upload_file" type="file">
    <input type="submit" value="富文本 上传文件">
</form>

</body>
</html>
